package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class FluxCreate {

    @Test
    public void fluxFromStringArray() {
        String source = "Santa tell me";
        Flux<String> st = Flux.fromArray(source.split(" "));

        StepVerifier.create(st)
            .expectNext("Santa")
            .expectNext("tell")
            .expectNext("me")
            .verifyComplete();
    }

    @Test
    public void fluxFromEmptyArray() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
            Flux.fromArray((Integer[]) null);
        });
    }

    @Test
    public void fluxGenerate() {
        AtomicInteger at = new AtomicInteger(0);

        Flux<Integer> flux = Flux.generate(sink -> {
            int i = at.getAndIncrement();
            if (i == 3) {
                sink.next(i);
                sink.complete();
            }
            sink.next(i);
        });

        StepVerifier.create(flux)
            .expectNext(0)
            .expectNext(1)
            .expectNext(2)
            .expectNext(3)
            .verifyComplete();
    }

    @Test
    public void fluxGenerateError() {
        AtomicInteger at = new AtomicInteger(0);

        Flux<Integer> flux = Flux.generate(sink -> {
            int i = at.getAndIncrement();
            if (i % 2 == 0) {
                sink.next(i);
            } else {
                sink.error(new IllegalStateException("Error at " + i)); // <4>
            }
        });

        StepVerifier.create(flux).expectNext(0).expectError().verify();
    }

    @Test
    public void fluxGenerateCallable() {
        Flux<String> flux = Flux.generate(
            AtomicLong::new,
            (state, sink) -> {
                long i = state.getAndIncrement();
                sink.next("3 x " + i + " = " + 3*i);
                if (i == 3) sink.complete();
                return state;
            });

        StepVerifier.create(flux)
            .expectNext("3 x 0 = 0")
            .expectNext("3 x 1 = 3")
            .expectNext("3 x 2 = 6")
            .expectNext("3 x 3 = 9")
            .verifyComplete();
    }

    @Test
    public void fluxGenerateCallableWithStateConsumer() {
        Flux<String> flux = Flux.generate(
            AtomicLong::new,
            (state, sink) -> { // Again, we generate a mutable object as the state.
                long i = state.getAndIncrement(); //We mutate the state here.
                sink.next("3 x " + i + " = " + 3 * i);
                if (i == 3) sink.complete();
                return state; // We return the *same* instance as the new state.
            }, (state) -> System.out.println("state: " + state)); // We see the last state value (4) as the output of this `Consumer` lambda.

        StepVerifier.create(flux)
            .expectNext("3 x 0 = 0")
            .expectNext("3 x 1 = 3")
            .expectNext("3 x 2 = 6")
            .expectNext("3 x 3 = 9")
            .verifyComplete();
    }
}

