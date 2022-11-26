package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Operators;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FluxOnErrorXxxTest {

	Logger log = LoggerFactory.getLogger(FluxTest.class);

	/* ===================================
			    onErrorContinue
	 ===================================== */

	@Test
	public void onErrorContinue() {
		List<String> valueDropped = new ArrayList<>();
		List<Throwable> errorDropped = new ArrayList<>();

		Flux<String> test = Flux.just("foo", "", "bar", "baz")
			.log()
			.filter(s -> 3 / s.length() == 1)
			.onErrorContinue(ArithmeticException.class,
				(t, v) -> {
					errorDropped.add(t);
					valueDropped.add((String) v);
				});

		StepVerifier.create(test)
			.expectNext("foo")
			.expectNext("bar")
			.expectNext("baz")
			.verifyComplete();

		assertThat(valueDropped).isEqualTo(List.of(""));
		assertThat(errorDropped.get(0).getMessage()).isEqualTo("/ by zero");
	}

	@Test
	public void onErrorContinueConditionalByClassNotMatch() {
		List<String> valueDropped = new ArrayList<>();
		List<Throwable> errorDropped = new ArrayList<>();

		Flux<String> test = Flux.just("foo", "", "bar", "baz")
			.filter(s -> 3 / s.length() == 1)
			.onErrorContinue(IllegalStateException.class,
				(t, v) -> {
					errorDropped.add(t);
					valueDropped.add((String) v);
				});

		StepVerifier.create(test)
			.expectNext("foo")
			.expectErrorMessage("/ by zero")
			.verifyThenAssertThat()
			.hasNotDroppedElements()
			.hasNotDroppedErrors();

		assertThat(valueDropped).isEmpty();
		assertThat(errorDropped).isEmpty();
	}

	@Test
	public void onErrorStop() {
		List<Integer> valueDropped = new ArrayList<>();
		List<Throwable> errorDropped = new ArrayList<>();

		Flux<Integer> test = Flux
			.just(0, 5)
			.flatMap(f ->
				Flux.range(f, 3).map(i -> 100 / i))
			.onErrorContinue((t, v) -> {
				errorDropped.add(t);
				valueDropped.add((Integer) v);
			});

		StepVerifier.create(test)
			.expectNoFusionSupport()
			.expectNext(100 / 1)
			.expectNext(100 / 2)
			.expectNext(100 / 5)
			.expectNext(100 / 6)
			.expectNext(100 / 7)
			.expectComplete()
			.verifyThenAssertThat()
			.hasNotDroppedElements()
//			.hasDroppedErrors(1)
		;

		Assertions.assertEquals(valueDropped, List.of(0));
		Assertions.assertEquals("/ by zero", errorDropped.get(0).getMessage());

		log.info(valueDropped.toString());
		log.info(errorDropped.toString());

//		Flux<Integer> test2 = Flux.just(0, 1).map(o -> o*o);
//		StepVerifier.create(test2).expectFusion().expectNext(0).expectNext(1).verifyComplete();
	}

	public static <T> void drop(@Nullable Throwable e, @Nullable T v) {
		if (v != null) {
			Operators.onNextDropped(v, Context.empty());
		}
		if (e != null) {
			Operators.onErrorDropped(e, Context.empty());
		}
	}

	static class FluxException extends RuntimeException {
		public FluxException() {
			super();
		}

		public FluxException(String message) {
			super(message);
		}
	}
}
