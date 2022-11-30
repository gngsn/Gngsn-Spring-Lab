package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MonoOnErrorXxxTest {

	static Logger log = LoggerFactory.getLogger(MonoTest.class);

	@Test
	public void onErrorComplete() {
		final AtomicInteger datasource = new AtomicInteger(0);

		Mono<Integer> monoWithError = Mono.just(datasource)
			.map(i -> 100 / i.get());

		StepVerifier.create(monoWithError)
			.verifyErrorMessage("/ by zero");
//			.verifyError(ArithmeticException.class);

		Mono<Integer> mono = monoWithError
			.doOnNext(signal ->  // before onError(java.lang.ArithmeticException: / by zero)
				log.info("before {}", signal.toString()))
//			.onErrorComplete() 												// onError -> onComplete: all Exception will be ignored
//			.onErrorComplete(ArithmeticException.class)						// onError -> onComplete: only if MonoException
			.onErrorComplete(throwable -> 									// onError -> onComplete: only if throwable equals MonoException
				throwable.getClass().equals(ArithmeticException.class))
			.doOnEach(signal -> log.info("after {}", signal.toString()));	// after onComplete()

		StepVerifier.create(mono)
			.verifyComplete();
	}

	@Test
	public void onErrorContinue() {
		/*
			ignore error and continue
		*/
		Mono<String> data = Mono.just("MONO_TEST");
		BiConsumer<Throwable, Object> loggingErrorBiConsumer = (thr, str) -> 			// Error occur: '<<ERROR>>'. ignore current element 'MONO_TEST'.
			log.error("Error occur: '{}'. ignore current element '{}'.", thr.getMessage(), str);

		data.map(str -> { throw new MonoException("<<ERROR>>"); })			// This Exception does not propagate external code
			.doOnEach(signal -> log.info("before {}", signal.toString()))	// before onComplete()
//			.onErrorContinue(loggingErrorBiConsumer) 						// onError -> onComplete: all Exception will be ignored
//			.onErrorContinue(MonoException.class, loggingErrorBiConsumer)	// onError -> onComplete: only if MonoException
			.onErrorContinue(throwable -> 									// onError -> onComplete: only if throwable equals MonoException
				throwable.getClass().equals(MonoException.class), loggingErrorBiConsumer)
			.doOnEach(signal -> log.info("after {}", signal.toString()))	// after onComplete()
			.subscribe();
	}

	@Test
	public void onErrorContinue2() {
		/*
			ignore error and continue
		*/
		AtomicReference<Throwable> errorRef = new AtomicReference<>();
		Mono<Integer> test = Mono.just(1)
			.log()
			.flatMap(i -> Mono.just(1 - i)
				.map(v -> 30 / v)
				.onErrorReturn(100)
//				.onErrorStop()
			)
			.onErrorContinue(MonoOnErrorXxxTest::drop);


		test.subscribe(t -> log.info("Result: " + t.toString()));
	}

	/**
	 * Helper for other tests to emulate resumeDrop with the public consumer-based API.
	 */
	public static <T> void drop(@Nullable Throwable e, @Nullable T v) {
		log.info("Throwable: '{}'. Object '{}'.", e.getMessage(), v);
		if (v != null) {
			Operators.onNextDropped(v, Context.empty());
		}
		if (e != null) {
			Operators.onErrorDropped(e, Context.empty());
		}
	}

	@Test
	public void onErrorMap() {
		Function<Throwable, RuntimeException> mapToMonoExceptionFunc = (thr) -> {			// Error occur: '<<ERROR>>'. ignore current element 'MONO_TEST'.
			log.error("Error occur: '{}'. Exception transform 'MonoException'.", thr.toString());
			return new MonoException(thr.getMessage());
		};

		Mono<Integer> data = Mono.<Integer>error(new Exception())
			.doOnEach(signal -> log.info("before {}", signal.toString()))	// before onError(java.lang.RuntimeException: <<ERROR>>)
			.onErrorMap(mapToMonoExceptionFunc)								// onError -> onComplete: all Exception will be transformed
//			.onErrorMap(RuntimeException.class, mapToMonoExceptionFunc) 	// onError -> onComplete: only if RuntimeException
//			.onErrorMap(throwable -> 										// onError -> onComplete: only if throwable equals RuntimeException
//				throwable.getClass().equals(RuntimeException.class), mapToMonoExceptionFunc)
			.doOnEach(signal -> log.info("after {}", signal.toString()))	// after onError(com.gngsn.webClient.reactor.MonoOnErrorXxxTest$MonoException: <<ERROR>>)
			;

		StepVerifier.create(data)
			.expectError(MonoException.class)
			.verify()
			;
		// Operator called default onErrorDropped
	}

	@Test
	public void onErrorMapSimple() {
		Mono<Integer> data = Mono.<Integer>error(new Exception("ERROR"))
			.log()
//							.onErrorMap(RuntimeException.class, t -> new MonoException(t.getMessage()))
							.onErrorMap(t -> t.getMessage().contains("ERROR"), t -> new MonoException(t.getMessage()));

		StepVerifier.create(data)
			.expectError(MonoException.class)
			.verify()
			;
		// Operator called default onErrorDropped
	}

	@Test
	public void onErrorResume() {
		/*
			Subscribe to a fallback publisher when an error
		*/
		Mono<String> data = Mono.just("MONO_TEST");
		Function<Throwable, Mono<String>> fallback = (thr) -> {					// Error occur: '<<ERROR>>'. ignore current element 'MONO_TEST'.
			log.error("Error occur: '{}'. Exception transform 'MonoException'.", thr.getMessage());
			return Mono.just("FALLBACK_MESSAGE");
		};

		data.map(str -> { throw new MonoException("<<ERROR>>"); })
			.doOnEach(signal -> log.info("before {}, Thread: {}", signal.toString(), Thread.currentThread()))		// before onError(com.gngsn.webClient.reactor.MonoOnErrorXxxTest$MonoException: <<ERROR>>)
//			.onErrorResume(fallback)											// onError -> onComplete: all Exception will be transformed
//			.onErrorResume(RuntimeException.class, fallback) 					// onError -> onComplete: only if RuntimeException
			.onErrorResume(throwable -> 										// onError -> onComplete: only if throwable equals RuntimeException
				throwable instanceof MonoException, fallback)
			.doOnEach(signal -> log.info("after {}, Thread: {}", signal.toString(), Thread.currentThread()))		// it is called twice. (1) after doOnEach_onNext(FALLBACK_MESSAGE). => (2) after onComplete()
			.subscribe();
	}

	@Test
	public void onErrorReturn() {
		/*
			Subscribe to a fallback publisher when an error
		*/
		String fallbackValue = "FALLBACK_MESSAGE";

		Mono.just("MONO_TEST")
			.map(str -> { throw new MonoException("<<ERROR>>"); })
			.log()
//			.doOnEach(signal -> log.info("before {}.", signal.toString()))		// before onError(com.gngsn.webClient.reactor.MonoOnErrorXxxTest$MonoException: <<ERROR>>).
			.onErrorReturn(fallbackValue)										// onError -> onComplete: all Exception will be transformed
//			.onErrorReturn(RuntimeException.class, fallbackValue)				// onError -> onComplete: only if RuntimeException
//			.onErrorReturn(throwable -> 										// onError -> onComplete: only if throwable equals RuntimeException
//				throwable.getClass().equals(MonoException.class), fallbackValue)
//			.doOnEach(signal -> log.info("after {}", signal.toString()))		// it is called twice. (1) after doOnEach_onNext(FALLBACK_MESSAGE). => (2) after onComplete()
			.subscribe(str -> log.info("fallback message is '{}'.", str.toString()));
	}

	@Test
	public void onErrorStop() {
		/*
			Subscribe to a fallback publisher when an error
		*/
		Mono<String> data = Mono.just("MONO_TEST");

		data.map(str -> { throw new MonoException("<<ERROR>>"); })
			.doOnEach(signal -> log.info("before {}.", signal.toString()))		// before onError(com.gngsn.webClient.reactor.MonoOnErrorXxxTest$MonoException: <<ERROR>>).
			.onErrorContinue((thr, str) -> 										// Error occur: '<<ERROR>>'. ignore current element 'MONO_TEST'.
				log.error("Error: '{}'. ignore element '{}'.", thr.getMessage(), str))
			.onErrorStop()										// onError -> onComplete: all Exception will be transformed
			.doOnEach(signal -> log.info("after {}", signal.toString()))		// it is called twice. (1) after doOnEach_onNext(FALLBACK_MESSAGE). => (2) after onComplete()
			.subscribe(str -> log.info("fallback message is '{}'.", str.toString()));
	}


	static class MonoException extends RuntimeException {
		public MonoException() {
			super();
		}

		public MonoException(String message) {
			super(message);
		}
	}
}
