package com.gngsn.webClient.reactor;

import jdk.jfr.DataAmount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

@Slf4j
public class MonoTest {

	@Test
	public void create() {
		Mono<String> noData = Mono.empty();
		Mono<String> data = Mono.just("mono create by cold publishing");

		StepVerifier.create(noData).verifyComplete();
		StepVerifier.create(data).expectNext("mono create by cold publishing").verifyComplete();
	}

	@Test
	public void subscribe () {
		Mono<String> data = Mono.just("mono create by cold publishing");
		data.subscribe();
	}

	@Test
	public void publisher() throws InterruptedException {
		Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

		final Flux<String> flux = Flux
			.range(1, 2)
			.map(i -> {
				log.info("first map: " + i);
				return 10 + i;
			})
			.publishOn(s)
			.map(i -> "value " + i);

		new Thread(() -> flux.subscribe(System.out::println));
		Thread.sleep(2000);
	}

	@Test
	public void cold_vs_hot() {}

	@Test
	public void zip() {}

}
