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

    Flux<Integer> evenNumbers = Flux
        .range(1, 5)
        .filter(x -> x % 2 == 0); // i.e. 2, 4

    Flux<Integer> oddNumbers = Flux
        .range(1, 5)
        .filter(x -> x % 2 > 0);  // ie. 1, 3, 5

    @Test
    public void givenFluxes_whenZipIsInvoked_thenZip() {
        Flux<Integer> fluxOfIntegers = Flux.zip(
            evenNumbers,
            oddNumbers,
            (a, b) -> a + b);
        fluxOfIntegers.subscribe(System.out::println);

        StepVerifier.create(fluxOfIntegers)
            .expectNext(3) // 2 + 1
            .expectNext(7) // 4 + 3
            .expectComplete()
            .verify();
    }

    @Test
    public void givenFluxes_whenZipWithIsInvoked_thenZipWith() {
        Flux<Integer> fluxOfIntegers = evenNumbers
            .zipWith(oddNumbers, (a, b) -> a * b);
        fluxOfIntegers.subscribe(System.out::println);
        StepVerifier.create(fluxOfIntegers)
            .expectNext(2)  // 2 * 1
            .expectNext(12) // 4 * 3
            .expectComplete()
            .verify();
    }

    @Test
    public void normalSameSize() {

        Flux<Integer> f = Flux.range(1, 5)
            .zipWithIterable(Arrays.asList(10, 20, 30, 40, 50), (a, b) -> a + b);
        // 11 22 33 44 55
        f.subscribe(System.out::println);
//        ts.assertValues(11, 22, 33, 44, 55)
//            .assertComplete()
//            .assertNoError();
    }
}
