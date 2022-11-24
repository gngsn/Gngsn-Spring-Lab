package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

public class FluxMono {
    static Logger log = LoggerFactory.getLogger(FluxMono.class);

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
    public void test() {
        Flux<String> flux = Flux.range(0, 4).map(FluxMono::indexToNameWithPrint);
        flux.subscribe();

        Flux<String> flux2 = Flux.range(0, 4).map(FluxMono::indexToName);
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
    public void testColdVSHot() {
        Flux<String> flux = Flux.range(0, 4).map(FluxMono::indexToName);
        ConnectableFlux<String> connectFlux = flux.publish(); // to hot

        connectFlux.subscribe(v -> log.info("First received {}", v), Throwable::printStackTrace);
        connectFlux.connect();
        connectFlux.subscribe(v -> log.info("Second received {}", v), Throwable::printStackTrace);
    }

    @Test
    public void fluxMethod() {
        AtomicInteger integer = new AtomicInteger();
        Flux<Integer> flux = Flux.generate(sink -> {
            if (integer.get() == 5) throw new RuntimeException("Integer 5");
            sink.next(integer.incrementAndGet());
        });

        flux.take(200)
            .doOnRequest(i -> System.out.println("doOnRequest: " + i)) // 1
            .doOnNext(i -> System.out.println("doOnNext: " + i)) // 2 (onComplete)
            .doOnEach(signal -> System.out.println("doOnEach: " + signal)) // 3 (onComplete/onError: Signal Consumer)
            .doOnTerminate(() -> System.out.println("doOnTerminate (completing successfully or failing with an error) ")) // (onComplete/onError: propagated downstream)
            .doOnError(thr -> System.out.println("doOnError: " + thr.getMessage())) // (onError)
            .subscribe(i -> System.out.println("subscribe: " + i)) // 4 (onComplete)
        ;
    }
}
