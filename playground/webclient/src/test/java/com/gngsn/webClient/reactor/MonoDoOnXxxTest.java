package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.subscriber.TestSubscriber;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class MonoDoOnXxxTest {

    /* ===================================
                doAfterTerminate
     ===================================== */

    @Test
    public void afterTerminateCalledWhenComplete() {
        AtomicBoolean onAfterTerminate = new AtomicBoolean();
        Flux<Integer> f = Flux.just(1)
            .log()
            .doAfterTerminate(() -> onAfterTerminate.set(true));

        StepVerifier.create(f)
            .expectFusion()
            .expectNext(1)
            .verifyComplete();

        Assertions.assertTrue(onAfterTerminate.get());
    }

    @Test
    public void afterTerminateFailureWhenError() {
        Flux<Integer> f = Flux.just(1, 0, 3)
            .map(i -> 100 / i)
            .doAfterTerminate(() -> {
                throw new IllegalStateException("doAfterTerminate boom");
            });

        StepVerifier.create(f)
            .expectFusion()
            .expectNext(100)
            .verifyErrorSatisfies(e ->
                assertThat(e).isInstanceOf(IllegalStateException.class)
                    .hasMessage("doAfterTerminate boom")
                    .hasSuppressedException(new ArithmeticException("/ by zero")));
    }


    /* ===================================
                   doFinally
     ===================================== */


    private SignalType signalType;
    private int calls;

    @BeforeEach
    public void init() {
        signalType = null;
        calls = 0;
    }

    @Test
    public void doFinallyWhenComplete() {
        Flux<Integer> f = Flux.just(1).doFinally(signal -> {
            signalType = signal;
            calls++;
        });

        StepVerifier.create(f)
            .expectNoFusionSupport()
            .expectNext(1)
            .expectComplete()
            .verify();

        assertThat(calls).isEqualTo(1);
        assertThat(signalType).isEqualTo(SignalType.ON_COMPLETE);
    }

    @Test
    public void doFinallyWhenError() {
        StepVerifier.create(Flux.error(new IllegalArgumentException())
                .doFinally(sigType -> {
                    signalType = sigType;
                    calls++;
                })
                .filter(i -> true))
            .expectNoFusionSupport()
            .expectError(IllegalArgumentException.class)
            .verify();

        assertThat(calls).isEqualTo(1);
        assertThat(signalType).isEqualTo(SignalType.ON_ERROR);
    }

    @Test
    public void doFinallyWhenCancel() {
        StepVerifier.create(Flux.range(1, 10)
                .doFinally(sigType -> {
                    signalType = sigType;
                    calls++;
                })
                .filter(i -> true)
                .take(5, false))
            .expectNoFusionSupport()
            .expectNext(1, 2, 3, 4, 5)
            .expectComplete()
            .verify();

        assertThat(calls).isEqualTo(1);
        assertThat(signalType).isEqualTo(SignalType.CANCEL);
    }

    /* ===================================
                   doFirst
     ===================================== */

    @Test
    public void orderIsReversed_NoFusion() {
        List<String> order = new ArrayList<>();

        @SuppressWarnings("divzero")
        Function<Integer, Integer> divZero = i -> i / 0;

        StepVerifier.create(
                Flux.just(1)
                    .map(divZero)
                    .doFirst(() -> order.add("one"))
                    .doFirst(() -> order.add("two"))
                    .doFirst(() -> order.add("three"))
            )
            .expectFusion()
            .verifyError(ArithmeticException.class);

        assertThat(order).containsExactly("three", "two", "one");
    }

    @Test
    public void mixedWithOnSubscribe() {
        List<String> order = new ArrayList<>();

        StepVerifier.create(
                Flux.just(1, 2)
                    .doOnNext(i -> order.add("doOnNext" + i))
                    .doFirst(() -> order.add("doFirst1"))
                    .doOnSubscribe(sub -> order.add("doOnSubscribe1"))
                    .doFirst(() -> order.add("doFirst2"))
                    .doOnSubscribe(sub -> order.add("doOnSubscribe2"))
            )
            .expectFusion()
            .expectNext(1, 2)
            .verifyComplete();

        assertThat(order).containsExactly(
            "doFirst2",
            "doFirst1",
            "doOnSubscribe1",
            "doOnSubscribe2",
            "doOnNext1",
            "doOnNext2"
        );
    }

    /* ===================================
                  doOnCancel
     ===================================== */

    @Test
    void fluxCancelledByMonoProcessor() {
        AtomicLong cancelCounter = new AtomicLong();
        Flux.range(1, 10)
            .log()
            .doOnCancel(cancelCounter::incrementAndGet)
            .shareNext()
            .subscribe(System.out::println);

        assertThat(cancelCounter).hasValue(1);
    }

    public void cancelTwiceCancelsOtherOnce() {
        AtomicInteger cancelled = new AtomicInteger();
        Flux<Integer> when = Flux.range(1, 10)
            .log()
            .doOnCancel(cancelled::incrementAndGet);

        Flux.just(1)
            .repeatWhen(other -> when)
            .subscribe(new BaseSubscriber<Integer>() {
                @Override
                protected void hookOnSubscribe(Subscription subscription) {
//                    subscription.request(1);
                    subscription.cancel();
                }
            });
    }


    /* ===================================
                testSignalListener
     ===================================== */
    @Test
    void testSignalListener() {
        TestSignalListener<Integer> testSignalListener = new TestSignalListener<>();

        Flux<Integer> fullFlux = Flux.just(1, 2, 3).hide();

        fullFlux.tap(() -> testSignalListener)
            .subscribeWith(TestSubscriber.create());

        System.out.println(Arrays.toString(testSignalListener.events.toArray()));
        System.out.println(testSignalListener.listenerErrors);

        assertThat(testSignalListener.listenerErrors).as("listener errors").isEmpty();

        assertThat(testSignalListener.events)
            .containsExactly(
                "doFirst",
                "doOnSubscription",
                "doOnRequest:unbounded",
                "doOnNext:1",
                "doOnNext:2",
                "doOnNext:3",
                "doOnComplete",
                "doAfterComplete",
                "doFinally:ON_COMPLETE"
            );
    }


    @Test
    public void whenProcessorIsStreamed() {
//		"When a processor is streamed"
//		given: "a source composable and a async downstream"
        Sinks.Many<Integer> source = Sinks.many().replay().all();
        Scheduler scheduler = Schedulers.newParallel("test", 2);

        try {
            Mono<List<Integer>> res = source.asFlux()
                .subscribeOn(scheduler)
                .delaySubscription(Duration.ofMillis(1L))
                .log("streamed")
                .map(it -> it * 2)
                .buffer()
                .shareNext();

            res.subscribe();

//		when: "the source accepts a value"
            source.emitNext(1, FAIL_FAST);
            source.emitNext(2, FAIL_FAST);
            source.emitNext(3, FAIL_FAST);
            source.emitNext(4, FAIL_FAST);
            source.emitComplete(FAIL_FAST);

//		then: "the res is passed on"
            assertThat(res.block()).containsExactly(2, 4, 6, 8);
        } finally {
            scheduler.dispose();
        }
    }

    /* ===================================
                  doOnDiscard
     ===================================== */

    @Test
    public void concatMapIterableDoOnDiscardTestDrainSync() {
        ReferenceCounted referenceCounted1 = new ReferenceCounted(1);
        ReferenceCounted referenceCounted2 = new ReferenceCounted(4);
        ReferenceCounted referenceCounted3 = new ReferenceCounted(6);

        Flux<ReferenceCounted> source =
            Flux.just(1, 2, 3, 4, 5, 6) //drain sync
                .filter(i -> i < 3)
                .log()
                .concatMapIterable(i -> Arrays.asList(
                    referenceCounted1,
                    referenceCounted2,
                    referenceCounted3
                ))
                .doOnDiscard(ReferenceCounted.class, ReferenceCounted::release);

        StepVerifier.create(source)
            .consumeNextWith(ReferenceCounted::release)
            .thenCancel()
            .verify();

        assertThat(referenceCounted1.getRefCount()).as("ref1").isEqualTo(0);
        assertThat(referenceCounted2.getRefCount()).as("ref2").isEqualTo(0);
        assertThat(referenceCounted3.getRefCount()).as("ref3").isEqualTo(0);
    }

    @Test
    public void doOnDiscardTestSync() {
        AtomicInteger index = new AtomicInteger();
        List<ReferenceCounted> refList = new ArrayList<>();

        Flux<ReferenceCounted> source = Flux.<ReferenceCounted>generate(sink -> {
                ReferenceCounted ref = new ReferenceCounted(index.incrementAndGet());

                refList.add(ref);
                sink.next(ref);
            })
            .log()
            .filter(i -> i.index < 4)
            .doOnDiscard(ReferenceCounted.class, System.out::println)
            .doOnDiscard(ReferenceCounted.class, referenceCounted -> refList.forEach(ReferenceCounted::release));

        StepVerifier.create(source)
            .consumeNextWith(ReferenceCounted::release)
            .consumeNextWith(ReferenceCounted::release)
            .consumeNextWith(ReferenceCounted::release)
            .consumeNextWith(ReferenceCounted::release)
            .thenCancel()
            .verify();

        assertThat(refList).as("ref1").isEmpty();
    }

    static class ReferenceCounted {

        int refCount = 1;
        final int index;

        ReferenceCounted(int index) {
            this.index = index;
        }

        public int getRefCount() {
            return this.refCount;
        }

        public void release() {
            this.refCount = 0;
        }

        @Override
        public String toString() {
            return "ReferenceCounted{index=" + index + "}";
        }
    }
}

