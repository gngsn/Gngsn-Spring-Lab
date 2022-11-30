package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.subscriber.TestSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Level;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(cancelled).hasValue(1);
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


}

