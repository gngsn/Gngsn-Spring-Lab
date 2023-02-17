package com.gngsn.webClient.reactor;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class CreateTest {

    private final Logger log = LoggerFactory.getLogger(HotColdTest.class);

    @Test
    public void create_empty() {
        Mono<String> noData = Mono.empty();

        StepVerifier.create(noData)
            .verifyComplete();
    }

    @Test
    public void create_just() {
        Mono<String> data = Mono.just("foo");

        StepVerifier.create(data)
            .expectNext("foo")
            .verifyComplete();
    }

    @Test
    public void create_just_list() {
        /*
            .just() runs immediately when it is defined, which is exception
             to the “nothing happens before you subscribe” rule
        */
        // #1
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
        // or #2
        List<String> iterable = List.of("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

        // verify
        StepVerifier.create(seq1)
            .expectNext("foo")
            .expectNext("bar")
            .expectNext("foobar")
            .verifyComplete();
    }

    @Test
    public void create_range() {
        Flux<Integer> numFromFiveToSeven = Flux.range(5, 3);

        StepVerifier.create(numFromFiveToSeven)
            .expectNext(5)
            .expectNext(6)
            .expectNext(7)
            .verifyComplete();
    }
}
