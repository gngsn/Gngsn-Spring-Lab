package com.gngsn.service;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;

class RequestServiceTest {
    private final Logger log = LoggerFactory.getLogger(RequestServiceTest.class);
    private WebClient webClient;

    @BeforeEach
    void setup() {
        ClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.builder("http-pool")
                .maxConnections(100)     // connection pool의 갯수
                .pendingAcquireTimeout(Duration.ofMillis(0)) //커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
                .pendingAcquireMaxCount(-1) //커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
                .maxIdleTime(Duration.ofMillis(2000L)) //커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
                .build())
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5)) //읽기시간초과 타임아웃
                    .addHandlerLast(new WriteTimeoutHandler(5))));

        webClient = WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .exchangeFunction(ExchangeFunctions.create(connector))
            .build();
    }

    @Test
    public void circuitBreakerTest() throws InterruptedException {
        IntStream.rangeClosed(1, 5)
            .forEach(i -> {
                webClient.get().uri("/api/circuit-breaker").retrieve().toEntity(String.class).subscribe(response -> {
                    Assertions.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
                });
            });

        IntStream.rangeClosed(1, 5)
            .forEach(i -> {
                webClient.get().uri("/api/circuit-breaker").retrieve().toEntity(String.class).subscribe(response -> {
                    Assertions.assertEquals(response.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
                });
            });
        Thread.sleep(1000);
    }
}