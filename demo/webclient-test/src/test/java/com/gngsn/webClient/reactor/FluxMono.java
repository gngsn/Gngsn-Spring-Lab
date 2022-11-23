package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

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
    }

    @Test
    public void testColdVSHot() {
        Flux<String> flux = Flux.range(0, 4).map(FluxMono::indexToName);
        ConnectableFlux<String> connectFlux = flux.publish(); // cold

        connectFlux.subscribe(v -> log.info("First received {}", v), Throwable::printStackTrace);
        connectFlux.connect();
        connectFlux.subscribe(v -> log.info("Second received {}", v), Throwable::printStackTrace);

//        StepVerifier.create(connectFlux)
//            .expectNext("Finding a name kyeongsun for 0 from thread main").expectNext("First received kyeongsun")
//            .expectNext("Finding a name sangho for 1 from thread main")
//            .expectNext("First received sangho")
//            .expectNext("Finding a name yongmi for 2 from thread main")
//            .expectNext("First received yongmi")
//            .expectNext("Finding a name jinnan for 3 from thread main")
//            .expectNext("First received jinnan")
//            .verifyComplete();
    }

    @Test
    public void testOrder() {
        AtomicInteger integer = new AtomicInteger();
        Flux<Integer> flux = Flux.generate(sink -> {
            if (integer.get() > 50) throw new RuntimeException();
            sink.next(integer.incrementAndGet());
        });

        flux.doOnNext(i -> System.out.println("doOnNext: " + i))
            .doOnEach(i -> System.out.println("doOnEach: " + i))
            .doOnRequest(i -> System.out.println("doOnRequest: " + i))
            .doOnError(i -> System.out.println("doOnError: " + i))
//            .flatMap(i -> {
//                System.out.println("flatMap: " + i);
//                return (i * i);
//            })
            .subscribe(i -> System.out.println("subscribe: " + i))
        ;

    }
}
