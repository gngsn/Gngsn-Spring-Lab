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
    public void reqApiTest200() {
        String URI = HOST + "/test/200";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        StepVerifier.create(this.exchangePostForMono(URI, requestBody))
            .expectNext(ResResult.success("Request Success"))
            .verifyComplete();
    }

    @Test
    public void reqApiTest400() {
        String URI = "http://127.0.0.1:8822/test/400";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        StepVerifier.create(this.exchangePostForMono(URI, requestBody))
            .expectErrorMatches(res ->
                res instanceof BadWebClientRequestException &&
                    res.getMessage().equals("4xx 외부 요청 오류"))
            .verify();
    }

    @Test
    public void reqApiTest500() {
        String URI = "http://127.0.0.1:8822/test/500";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        StepVerifier.create(this.exchangePostForMono(URI, requestBody))
            .expectErrorMatches(res ->
                res instanceof TargetServerErrorException &&
                    res.getMessage().equals("5xx 외부 시스템 오류"))
            .verify();
    }

    @Test
    public void reqApiTestTimeout() {
        String URI = "http://127.0.0.1:8822/test/timeout";
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
                    log.info("API 요청에 성공했습니다.");
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

    public WebClient commonWebClient(
        ExchangeStrategies exchangeStrategies,
        HttpClient httpClient
    ) {
        return WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .exchangeStrategies(exchangeStrategies)
            .build();
    }

    public HttpClient defaultHttpClient(ConnectionProvider provider) {

        return HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5)) //읽기시간초과 타임아웃
                    .addHandlerLast(new WriteTimeoutHandler(5)));
    }

    public ConnectionProvider connectionProvider() {

        return ConnectionProvider.builder("http-pool")
            .maxConnections(100)     // connection pool의 갯수
            .pendingAcquireTimeout(Duration.ofMillis(0)) //커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
            .pendingAcquireMaxCount(-1) //커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
            .maxIdleTime(Duration.ofMillis(2000L)) //커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
            .build();
    }

    public ExchangeStrategies defaultExchangeStrategies() {
        return ExchangeStrategies.builder().codecs(config -> {
            config.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(OM, MediaType.APPLICATION_JSON));
            config.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(OM, MediaType.APPLICATION_JSON));
            config.defaultCodecs().maxInMemorySize(1024 * 1024); // max buffer 1MB 고정. default: 256 * 1024
        }).build();
    }
}
