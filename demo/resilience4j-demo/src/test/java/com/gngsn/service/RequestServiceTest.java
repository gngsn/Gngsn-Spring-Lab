package com.gngsn.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

class RequestServiceTest {
    private final Logger log = LoggerFactory.getLogger(RequestServiceTest.class);

    @Test
    public void circuitBreakerTest() {
        RequestService requestService = new RequestService();

        Flux.just("foo")
                .handle((d, s) -> s.next(requestService.failRequest()))
            .repeat(100)
            .onErrorContinue((t, o) -> log.info(t.getMessage()))
            .subscribe(res -> log.info((String) res));
//        Mono.defer(() -> Mono.just(requestService.failRequest()))
//            .repeat(20)
//            .onErrorResume(trw-> {
//                System.out.println(trw);
//                return () -> trw.getMessage();
//            })
//            .subscribe(System.out::println);


    }
}