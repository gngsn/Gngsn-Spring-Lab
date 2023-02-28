package com.gngsn.webClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.webClient.exception.BadWebClientRequestException;
import com.gngsn.webClient.exception.TargetServerErrorException;
import com.gngsn.webClient.vo.ResResult;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.gngsn.webClient.config.WebClientConfiguration.*;


@Slf4j
public class MockRequestExchangeTest {

    private final String HOST = "http://127.0.0.1:8822";
    private WebClient webClient;

    public static final ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    @BeforeEach
    public void setUp() {
        webClient = commonWebClient(defaultExchangeStrategies(), defaultHttpClient(connectionProvider()));
    }

    @Test
    public void exchange_NonBlock_200() {
        String URI = HOST + "/test/200";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        StepVerifier.create(this.exchangePostForMono(URI, requestBody))
            .expectNext(ResResult.success("Success Request"))
            .verifyComplete();
    }

    @Test
    public void exchange_NonBlock_400() {
        String URI = HOST + "/test/400";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        StepVerifier.create(this.exchangePostForMono(URI, requestBody))
            .expectErrorMatches(res ->
                res instanceof BadWebClientRequestException &&
                    res.getMessage().equals("4xx 외부 요청 오류"))
            .verify();
    }

    @Test
    public void exchange_NonBlock_500() {
        String URI = HOST + "/test/500";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        StepVerifier.create(this.exchangePostForMono(URI, requestBody))
            .expectErrorMatches(res ->
                res instanceof TargetServerErrorException &&
                    res.getMessage().equals("5xx 외부 시스템 오류"))
            .verify();
    }

    @Test
    public void exchange_NonBlock_Timeout() {
        String URI = HOST + "/test/timeout";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        StepVerifier.create(this.exchangePostForMono(URI, requestBody))
            .expectError(WebClientRequestException.class)
            .verify();
    }

    private Mono<ResResult> exchangePostForMono(String uri, MultiValueMap<String, String> body) throws WebClientResponseException {
        // @formatter:off
        return webClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.toEntity(ResResult.class).mapNotNull(HttpEntity::getBody);
                }

                if (response.statusCode().is4xxClientError()) {
                    log.error("API 요청 중 4xx 에러가 발생했습니다. 요청 데이터를 확인해주세요.");
                    throw new BadWebClientRequestException(response.rawStatusCode(), "4xx 외부 요청 오류");
                }

                log.error("API 요청 중 요청한 서버에서 5xx 에러가 발생했습니다.");
                throw new TargetServerErrorException(response.rawStatusCode(), "5xx 외부 시스템 오류");
            })
            .doOnError(error -> log.error("doOnError logging: " + error));
    }
}
