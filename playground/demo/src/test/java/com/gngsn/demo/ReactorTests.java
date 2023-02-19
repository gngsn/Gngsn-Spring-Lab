package com.gngsn.demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class ReactorTests {

    @Test
    public void createFluxByJust() {
        Flux<String> fruitFlux = Flux
            .just("Apple", "Orange", "Grape", "Banana", "Strawberry");

        fruitFlux.subscribe(
            fruit -> System.out.println("fruit: " + fruit)
        );


        WebClient client = WebClient.create("https://example.org");

        Mono<String> result = client.get()
            .uri("/persons/{id}", 1).accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                throw new RuntimeException("Fail to API Request");
            })
            .onStatus(HttpStatus::is5xxServerError, error -> {
                throw new RuntimeException("System Error");
            })
            .bodyToMono(String.class);
    }


    @Test
    public void createFluxByFromArray() {
        String[] fruits = new String[]{
            "Apple", "Orange", "Grape", "Banana", "Strawberry"};
        Flux<String> fruitFlux = Flux.fromArray(fruits);

        fruitFlux.subscribe(
            fruit -> System.out.println("fruit: " + fruit)
        );
    }

    @Test
    public void zipFluxesToObject() {
        Flux<String> characterFlux = Flux
            .just("Garfield", "Kojak", "Barbossa");

        Flux<String> foodFlux = Flux
            .just("Lasagna", "Lollipops", "Apples");

        Flux.zip(characterFlux, foodFlux, (c, f) -> c + " eats " + f)
            .subscribe(System.out::println);
    }

    @Test
    public void firstFlux() {
        Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
            .delaySubscription(Duration.ofMillis(100));

        Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

        Flux.firstWithSignal(slowFlux, fastFlux)
            .subscribe(System.out::println);
    }
}
