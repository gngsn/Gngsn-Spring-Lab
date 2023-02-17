package com.gngsn.webClient;

import com.gngsn.webClient.util.WebClientUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static com.gngsn.webClient.config.WebClientConfiguration.*;
import static org.mockito.Mockito.mock;

class WebClientTest {

    private WebClientUtils webClientUtils;


    @BeforeEach
    public void setUp() {
        WebClient webClient = commonWebClient(defaultExchangeStrategies(), defaultHttpClient(connectionProvider()));
        webClientUtils = new WebClientUtils(webClient);
    }

    @Test
    public void success_Test() {
        URI uri = URI.create("http://127.0.0.1:8080/test/200");
        String method = "GET";

        Mono<ClientResponse> clientResponseMono = webClientUtils.request(uri, method, null);

		StepVerifier.create(clientResponseMono)
			.expectNextMatches(clientResponse -> clientResponse.statusCode().equals(HttpStatus.OK))
			.verifyComplete();
    }
}