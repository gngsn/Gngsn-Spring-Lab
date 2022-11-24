package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Test
    public void monoMethod() {
        final List<String> basket1 = Arrays.asList("kiwi", "orange", "lemon", "orange", "lemon", "kiwi");
        final List<String> basket2 = Arrays.asList("banana", "lemon", "lemon", "kiwi");
        final List<String> basket3 = Arrays.asList("strawberry", "orange", "lemon", "grape", "strawberry");
        final List<List<String>> baskets = Arrays.asList(basket1, basket2, basket3);
        final Flux<List<String>> basketFlux = Flux.fromIterable(baskets);

        basketFlux.concatMap(basket -> {

            log.info("basket : {}", basket);
            final Mono<List<String>> distinctFruits = Flux.fromIterable(basket).distinct().collectList().subscribeOn(Schedulers.parallel());

            log.info("distinctFruits : {}", distinctFruits);

            final Mono<Map<String, Long>> countFruitsMono = Flux.fromIterable(basket)
                .groupBy(fruit -> fruit) // 바구니로 부터 넘어온 과일 기준으로 group을 묶는다.
                .concatMap(groupedFlux -> groupedFlux.count()
                    .map(count -> {
                        final Map<String, Long> fruitCount = new LinkedHashMap<>();
                        fruitCount.put(groupedFlux.key(), count);
                        return fruitCount;
                    }) // 각 과일별로 개수를 Map으로 리턴
                ) // concatMap으로 순서보장
                .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() { {
                    putAll(accumulatedMap);
                    putAll(currentMap);
                }}) // 그동안 누적된 accumulatedMap에 현재 넘어오는 currentMap을 합쳐서 새로운 Map을 만든다. // map끼리 putAll하여 하나의 Map으로 만든다.
                .subscribeOn(Schedulers.parallel());


            log.info("countFruitsMono : {}", countFruitsMono);
            return Flux.zip(distinctFruits, countFruitsMono, FruitInfo::new);
        }).subscribe(System.out::println);
    }
}

