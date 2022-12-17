package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class FluxZipTest {

    @Test
    public void zip8() {
        StepVerifier.create(Flux.zip(Flux.just(1),
                Flux.just(2),
                Flux.just(3),
                Flux.just(4),
                Flux.just(5),
                Flux.just(6),
                Flux.just(7),
                Flux.just(8)))
            .expectNext(Tuples.of(1, 2, 3, 4, 5, 6, 7, 8))
            .verifyComplete();
    }


    @Test
    public void zip4() {
        AtomicInteger cnt = new AtomicInteger(1);
        Flux[] list = new Flux[]{Flux.just(1), Flux.just(2), Flux.just(3), Flux.just(4), Flux.just(5), Flux.just(6)};
        Flux<Integer> f = Flux.zip(obj -> cnt.getAndIncrement(), 2, list);

        f.subscribe(System.out::println);
    }

    @Test
    public void createZipWithPrefetch() {
        AtomicInteger cnt = new AtomicInteger(1);

        Flux<Integer>[] list = new Flux[]{Flux.range(1, 4000000)};
        Flux<Integer> f = Flux.zip(obj -> cnt.getAndIncrement(), 400000, list);

        f.subscribe(System.out::println);
//        assertThat(f.getPrefetch()).isEqualTo(123);
    }

    @Test
    public void createZipWithPrefetchIterable() {
        List<Flux<Integer>> list = Arrays.asList(Flux.just(1), Flux.just(2));
        Flux<Integer> f = Flux.zip(list, 123, obj -> 0);
        assertThat(f.getPrefetch()).isEqualTo(123);
    }
}
