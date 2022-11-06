package com.gngsn.webClient.reactor;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class SubscribeTest {

    private final Logger log = LoggerFactory.getLogger(HotColdTest.class);

    @Test
    public void create_empty() {
        Flux<Integer> ints = Flux.range(1, 3);
        ints.subscribe();
    }

}
