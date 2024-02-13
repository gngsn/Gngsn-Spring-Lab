package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class FluxTest {
    static Logger log = LoggerFactory.getLogger(FluxTest.class);

    private static String[] NAME = new String[]{
        "kyeongsun",
        "sangho",
        "yongmi",
        "jinnan",
    };

    public static String indexToNameWithPrint(Integer i) {
        log.info("Finding a name {} for {} from thread {}", NAME[i], i, Thread.currentThread().getName());
        return NAME[i];
    }

    public static String indexToName(Integer i) {
        return NAME[i];
    }

    @Test
    public void basic() {
        Flux<String> flux = Flux.range(0, 4).map(FluxTest::indexToNameWithPrint);
        flux.subscribe();

        Flux<String> flux2 = Flux.range(0, 4).map(FluxTest::indexToName);
        flux2.subscribe(v -> log.info("First received {}", v), Throwable::printStackTrace);
        flux2.subscribe(v -> log.info("Second received {}", v), Throwable::printStackTrace);

        StepVerifier.create(flux2)
            .expectNext("kyeongsun")
            .expectNext("sangho")
            .expectNext("yongmi")
            .expectNext("jinnan")
            .verifyComplete();
    }

    @Test
    public void interval() {
        AtomicInteger integer = new AtomicInteger();
        Flux<Integer> flux = Flux.generate(sink -> sink.next(integer.incrementAndGet()));

        Flux.interval(Duration.ZERO).take(10).publish(longFlux -> s -> log.info(String.valueOf(s))).subscribe();
    }

    @Test
    public void testColdVSHot() {
        Flux<String> flux = Flux.range(0, 4).map(FluxTest::indexToName);
        ConnectableFlux<String> connectFlux = flux.publish(); // to hot

        connectFlux.subscribe(v -> log.info("First received {}", v), Throwable::printStackTrace);
        connectFlux.connect();
        connectFlux.subscribe(v -> log.info("Second received {}", v), Throwable::printStackTrace);
    }

    /* ====================================
             ALL Behavior method TEST
     ==================================== */
    @Test
    public void fluxMethod() {
        AtomicInteger integer = new AtomicInteger();
        Flux<Integer> flux = Flux.generate(sink -> {
            if (integer.get() == 5) throw new RuntimeException("Integer 5");
            sink.next(integer.incrementAndGet());
        });

        flux.doOnRequest(i -> System.out.println("doOnRequest: " + i)) // 1
            .doOnNext(i -> System.out.println("doOnNext: " + i)) // 2 (onComplete)
            .doOnEach(signal -> System.out.println("doOnEach: " + signal)) // 3 (onComplete/onError: Signal Consumer)
            .doOnTerminate(() -> System.out.println("doOnTerminate (completing successfully or failing with an error) ")) // (onComplete/onError: propagated downstream)
            .doOnError(thr -> System.out.println("doOnError: " + thr.getMessage())) // (onError)
            .subscribe(i -> System.out.println("subscribe: " + i)) // 4 (onComplete)
        ;
    }

    /* ====================================
                    ERROR TEST
     ==================================== */
    public <T> Flux<T> appendBoomError(Flux<T> source) {
        return source.concatWith(Mono.error(new IllegalArgumentException("boom")));
    }

    @Test
    public void testAppendBoomError() {
        Flux<String> source = Flux.just("thing1", "thing2");

        StepVerifier.create(
                appendBoomError(source))
            .expectNext("thing1")
            .expectNext("thing2")
            .expectErrorMessage("boom")
            .verify();
    }
}

