package com.gngsn.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ReactorTests {

  @Test
  public void createFluxByJust() {
    Flux<String> fruitFlux = Flux
        .just("Apple", "Orange", "Grape", "Banana", "Strawberry");

    fruitFlux.subscribe(
        fruit -> System.out.println("fruit: " + fruit)
    );
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
