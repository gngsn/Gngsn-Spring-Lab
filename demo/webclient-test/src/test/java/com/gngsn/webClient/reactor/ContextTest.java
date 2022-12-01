package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.test.StepVerifier;
import reactor.test.subscriber.TestSubscriber;

public class ContextTest {
	@Test
	void normalBuffered() {
		TestSubscriber<Integer> ts = TestSubscriber.create();
		Flux<Integer> source = Flux.<Signal<Integer>>create(e -> {
			e.next(Signal.next(1));
			e.next(Signal.next(2));
			e.next(Signal.next(3));
			e.next(Signal.complete());
		}).dematerialize();

		source.subscribe(ts);
		StepVerifier.create(source)
			.expectNext(1, 2, 3)
			.verifyComplete();
	}
	@Test
	public void contextPassing() {

	}
}
