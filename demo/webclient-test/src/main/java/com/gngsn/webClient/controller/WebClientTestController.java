package com.gngsn.webClient.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/flux/test")
public class WebClientTestController {


	@RequestMapping(value = "/long")
	public Flux<String> atomicLong() {
		Flux<String> flux = Flux.generate(
			AtomicLong::new,
			(state, sink) -> {
				long i = state.getAndIncrement();
				sink.next("3 x " + i + " = " + 3*i);
				if (i == 5) sink.complete();
				return state;
			});

		return flux;
	}

}
