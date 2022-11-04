package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

/**
 * Hot vs Cold
 *
 * [kr]
 * Flux & Mono 는 데이터의 비동기 시퀀스이며, subscribe 하기 전까지 아무런 동작도 하지 않는다.
 * publisher 는 hot과 cold 두 가지 종류가 있다.
 * - Cold: subscribe 할 때마다 새로운 데이터 생성
 * - Hot: subscribe 의 호출 수와 상관 없이 subscribe 전에 동작 실행 (subscribe 전 아무 동작도 하지 않는 reactor 특징 예외 경우)
 *
 *
 * [en]
 * Flux & Mono are the same that represent an asynchronous sequence of data, and nothing happens before you subscribe.
 * There are two board of publishers: hot and cold.
 *
 * - Cold: generate data anew for each subscription. (ex. Mono.defer())
 * - Hot: does happen before you subscribe regardless of any number of subscribers (ex. Mono.just())
 *      : an exception to the “nothing happens before you subscribe” rule
 */
public class HotColdTest {

    private final Logger log = LoggerFactory.getLogger(HotColdTest.class);

    /**
     * Mono.defer(): a cold (lazy) publisher
     */
    @Test
    public void Cold() {

        Mono<String> deferMsg = Mono.defer( // defer로 subscribe 하기 전까지 getMessageByMonoJust 실행 안함
            () -> getMessageByMonoJust("Lazy Publisher"));

        log.info("Intermediate Test Message");

        StepVerifier.create(deferMsg)
            .expectNext("Lazy Publisher")
            .verifyComplete();

        sleep(5000 /*ms*/);

        StepVerifier.create(deferMsg)
            .expectNext("Lazy Publisher")
            .verifyComplete();

        /*
            Intermediate Test Message
            Call to Retrieve Message Lazy Publisher at 2022-11-04T22:50:57.732335.
            Call to Retrieve Message Lazy Publisher at 2022-11-04T22:51:02.749502.
        */
    }


    /**
     * Mono.just(): runs regardless of when subscriptions have been attached
     */
    @Test
    public void Hot() {

        Mono<String> msg = getMessageByMonoJust("Eager Publisher");

        log.info("Intermediate Test Message");

        StepVerifier.create(msg)    // subscribe 전 만들어진 메세지로 검증 처리 -> True
            .expectNext("Eager Publisher")
            .verifyComplete();

        sleep(5000 /*ms*/);

        StepVerifier.create(msg)    // 처음 만들어졌던 메세지로 검증 처리 -> True
            .expectNext("Eager Publisher")
            .verifyComplete();

        /*
            Call to Retrieve Message Eager Publisher at 2022-11-04T22:55:01.378738
            Intermediate Test Message
        */
    }

    private Mono<String> getMessageByMonoJust(String msg) {
        log.info("Call to Retrieve Message {} at {}", msg, LocalDateTime.now());

        return Mono.just(msg);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            log.error(ie.getMessage());
        }
    }

    // ----------------------------  Example 2  -------------------------------

    @Test
    public void Cold_2() {
        Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
            .map(String::toUpperCase);

        source.subscribe(d -> System.out.println("Subscriber 1: "+d));
        source.subscribe(d -> System.out.println("Subscriber 2: "+d));
    }

    @Test
    public void Hot_2() {
        Sinks.Many<String> hotSource = Sinks.unsafe().many().multicast().directBestEffort();

        Flux<String> hotFlux = hotSource.asFlux().map(String::toUpperCase);

        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));

        hotSource.emitNext("blue", FAIL_FAST);  // Subscriber 1: runs regardless of when subscriptions have been attached
        hotSource.tryEmitNext("green").orThrow();   // Subscriber 1

        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));

        hotSource.emitNext("orange", FAIL_FAST); // Subscriber 1 -> 2
        hotSource.emitNext("purple", FAIL_FAST); // Subscriber 1 -> 2
        hotSource.emitComplete(FAIL_FAST);

    }
}
