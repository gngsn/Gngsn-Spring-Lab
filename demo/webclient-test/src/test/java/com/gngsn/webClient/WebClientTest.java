package com.gngsn.webClient;

import com.gngsn.webClient.common.Constants;
import com.gngsn.webClient.plugin.WebClientPlugin;
import com.gngsn.webClient.util.SpringBeanUtils;
import com.gngsn.webClient.vo.ResResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Objects;

import static com.gngsn.webClient.config.WebClientConfiguration.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WebclientTestApplicationTests {

	private Jackson2JsonDecoder decoder;

    private WebClientPlugin webClientPlugin;

    private WebClient webClient;

    Logger log = LoggerFactory.getLogger(WebclientTestApplicationTests.class);

    @BeforeEach
    public void setUp() {
        webClient = commonWebClient(defaultExchangeStrategies(), defaultHttpClient(connectionProvider()));
    }

    @BeforeEach
    public void setup() {
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
        SpringBeanUtils.getInstance().setApplicationContext(context);
        when(context.getBean(ApiResult.class)).thenReturn(mock(ApiResult.class));

		decoder = new Jackson2JsonDecoder();
        webClientPlugin = new WebClientPlugin(webClient);
    }

    @Test
    public void success_Test() {

        ServerWebExchange webExchange = MockServerWebExchange
            .from(
                MockServerHttpRequest.get("http://127.0.0.1:8080/test/200")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build()
            );

        HttpContext context = mock(HttpContext.class);

        webExchange.getAttributes().put(Constants.CONTEXT, context);
        webExchange.getAttributes().put(Constants.HTTP_URI, URI.create("http://127.0.0.1:8080/test/200"));
        webExchange.getAttributes().put(Constants.HTTP_TIME_OUT, 300000L);

        Mono<ClientResponse> clientResponseMono = webClientPlugin.request(webExchange);

		webExchange.getAttributes().forEach((key, value) -> log.info("key: {}, value: {}", key, value));
		StepVerifier.create(clientResponseMono)
			.expectNextMatches(clientResponse -> clientResponse.statusCode().equals(HttpStatus.OK))
			.verifyComplete();
    }

	@Test
    public void successClientCode() throws InterruptedException {

        ServerWebExchange webExchange = MockServerWebExchange
            .from(
                MockServerHttpRequest.get("http://127.0.0.1:8080/test/200")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build()
            );

        HttpContext context = mock(HttpContext.class);

        webExchange.getAttributes().put(Constants.CONTEXT, context);
        webExchange.getAttributes().put(Constants.HTTP_URI, URI.create("http://127.0.0.1:8080/test/200"));
        webExchange.getAttributes().put(Constants.HTTP_TIME_OUT, 300000L);

        Mono<ClientResponse> clientResponseMono = webClientPlugin.request(webExchange);


        clientResponseMono
            .subscribe(clientResponse ->
                clientResponse.body(BodyExtractors.toDataBuffers())
                    .flatMap(dataBuffer ->
                        Mono.just(Objects.requireNonNull(decoder.decode(dataBuffer, ResolvableType.forType(ResResult.class), null, null))))
                    .subscribe()
            );
		Thread.sleep(1_000);
    }
}