package com.gngsn.webClient.reactor;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class MonoZipTest {

    @Test
    public void toStringArray() {
        Mono<String[]> mono = Mono.zip(a -> Arrays.copyOf(a, a.length, String[].class),
            Mono.just("hello"),
            Mono.just("world,"),
            Mono.just("hello"),
            Mono.just("reator")
        );

        mono.subscribe(s -> {
            System.out.println(s.getClass().getSimpleName());
            for (int i = 0; i < s.length; i++) {
                System.out.println(i + " : " + s[i]);
            }
        });
    }

    @Test
    public void StringArrayToString() {
        Function<Object[], String> conbinator = obArr -> Arrays.stream(obArr)
            .map(Object::toString)
            .reduce((a, b) -> a.concat(" " + b))
            .orElse("");

        Mono<String> mono = Mono.zip(conbinator,
            Mono.just("hello"),
            Mono.just("world,"),
            Mono.just("hello"),
            Mono.just("reator")
        );

        mono.subscribe(System.out::println);
    }

    @Test
    public void zip3() {
        Mono<Tuple3> mono = Mono.zip(
            Arrays.asList(Mono.just("foo"), Mono.just("foo2"), Mono.just("foo3")),
            Tuples.fn3());

        mono.subscribe(System.out::println);
    }

    @Test
    public void toTuple() {
        Mono<Tuple4<String, String, String, String>> mono = Mono.zip(
            Mono.just("hello"),
            Mono.just("world"),
            Mono.just("hello"),
            Mono.just("reator")
        );

        mono.subscribe(System.out::println);
    }
    @Test
    public void zipWhenCombines() {

        record User (
                Integer userId,
                String username,
                List<String> attributes
        ) {}

        Mono<User> userMono = Mono.just(new User(1, "Sunny", List.of("Lovely", "Curious")));
        Mono<Boolean> emailSentMono = Mono.just(/* some logic */ true);
        Mono<String> databaseResultMono = Mono.just("'new user 'sunny' is created");

        Mono<String> responseEntityMono = userMono.zipWhen(user -> emailSentMono, (t1, t2) -> Tuples.of(t1, t2))
                .zipWhen(tuple -> databaseResultMono, (tuple, databaseResult) -> {
                    User user = tuple.getT1();
                    Boolean emailSent = tuple.getT2();
                    return "Response: " + user + ", Email Sent: " + emailSent + ", Database Result: " + databaseResult;
                });

        responseEntityMono.subscribe(System.out::println);
    }

    @Test
    public void zipIterableDelayErrorCombinesErrors() {
        Exception boom1 = new NullPointerException("boom1");
        Exception boom2 = new IllegalArgumentException("boom2");

        StepVerifier.create(Mono.zipDelayError(
                Arrays.asList(Mono.just("foo"), Mono.<String>error(boom1), Mono.<String>error(boom2)),
                Tuples.fn3()))

            .verifyErrorMatches(e -> {
                System.out.println(e);
                return e.getMessage().equals("Multiple exceptions") &&
                    e.getSuppressed()[0] == boom1 &&
                    e.getSuppressed()[1] == boom2;
            });
    }
}
