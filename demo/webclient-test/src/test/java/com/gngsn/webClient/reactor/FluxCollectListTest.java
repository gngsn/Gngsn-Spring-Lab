package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


public class FluxCollectListTest {

    @Test
    public void bufferWillRerouteAsManyElementAsSpecified() {
        assertThat(Flux.just(1, 2, 3, 4, 5)
            .buffer(2)
            .collectList()
            .block()).containsExactly(Arrays.asList(1, 2),
            Arrays.asList(3, 4),
            Arrays.asList(5));
    }


    @Test
    public void singleSourceNormalWithFuseableDownstream() {
        StepVerifier.create(
                Flux.combineLatest(Collections.singletonList(Flux.just(1, 2, 3).hide()), (arr) -> arr[0].toString())
                    //the map is Fuseable and sees the combine as fuseable too
                    .map(x -> x + "!")
                    .collectList())
            .assertNext(l -> assertThat(l).containsExactly("1!", "2!", "3!"))
            .verifyComplete();
    }

    @Test
    public void singleSourceNormalWithoutFuseableDownstream() {
        StepVerifier.create(
                Flux.combineLatest(
                        Collections.singletonList(Flux.just(1, 2, 3).hide()),
                        (arr) -> arr[0].toString())
                    //the collectList is NOT Fuseable
                    .collectList()
            )
            .assertNext(l -> assertThat(l).containsExactly("1", "2", "3"))
            .verifyComplete();
    }

    @Test
    public void singleSourceFusedWithFuseableDownstream() {
        StepVerifier.create(
                Flux.combineLatest(
                        Collections.singletonList(Flux.just(1, 2, 3)),
                        (arr) -> arr[0].toString())
                    //the map is Fuseable and sees the combine as fuseable too
                    .map(x -> x + "!")
                    .collectList())
            .assertNext(l -> assertThat(l).containsExactly("1!", "2!", "3!"))
            .verifyComplete();
    }

    Sinks.EmitFailureHandler FAIL_FAST = (signalType, emission) -> false;

    @Test
    public void bufferWillAccumulateMultipleListsOfValues() {
        Sinks.Many<Integer> numbers = Sinks.many().multicast().onBackpressureBuffer();
        Sinks.Many<Integer> boundaryFlux = Sinks.many().multicast().onBackpressureBuffer();

        StepVerifier.create(numbers.asFlux()
                .buffer(boundaryFlux.asFlux())
                .collectList())
            .then(() -> {
                numbers.emitNext(1, FAIL_FAST);
                numbers.emitNext(2, FAIL_FAST);
                numbers.emitNext(3, FAIL_FAST);
                boundaryFlux.emitNext(1, FAIL_FAST);
                numbers.emitNext(5, FAIL_FAST);
                numbers.emitNext(6, FAIL_FAST);
                numbers.emitComplete(FAIL_FAST);
                //"the collected lists are available"
            })
            .assertNext(res -> assertThat(res).containsExactly(Arrays.asList(1, 2, 3), Arrays.asList(5, 6)))
            .verifyComplete();
    }
}
