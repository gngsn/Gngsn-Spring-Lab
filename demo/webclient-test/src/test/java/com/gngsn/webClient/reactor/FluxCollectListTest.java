package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
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

}
