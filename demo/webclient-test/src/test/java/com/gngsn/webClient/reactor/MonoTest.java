package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class MonoTest {

	Logger log = LoggerFactory.getLogger(MonoTest.class);

	@Test
	public void create() {
		Mono<String> noData = Mono.empty();
		Mono<String> data = Mono.just("mono create by cold publishing");

		StepVerifier.create(noData).verifyComplete();
		StepVerifier.create(data).expectNext("mono create by cold publishing").verifyComplete();
	}

	@Test
	public void onErrorComplete() { // replace onError signal with onComplete signal
		Mono<String> data = Mono.just("mono create by cold publishing");

		data.map(str -> {
				System.out.println(str);
				throw new MonoException();		// This Exception does not propagate external code
//				throw new RuntimeException();	// RuntimeException, which is not corresponding with onErrorComplete condition, will be propagated external.
			})
			.doOnEach(signal -> log.info("before {}", signal.toString()))	// before onError(com.gngsn.webClient.reactor.MonoException)
			.onErrorComplete() 												// onError -> onComplete: all Exception will be ignored
			.onErrorComplete(MonoException.class)							// onError -> onComplete: only if MonoException
			.onErrorComplete(throwable -> 									// onError -> onComplete: only if throwable equals MonoException
				throwable.getClass().equals(MonoException.class))
			.doOnEach(signal -> log.info("after {}", signal.toString()))	// after onComplete()
			.subscribe();
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

class MonoException extends RuntimeException {

}