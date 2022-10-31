package com.gngsn.webClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.webClient.exception.BadWebClientRequestException;
import com.gngsn.webClient.vo.ResDTO;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Slf4j
public class RequestExchangeTest {

    private WebClient webClient;

    public static final ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    @BeforeEach
    public void setUp() {
        webClient = commonWebClient(defaultExchangeStrategies(), defaultHttpClient(connectionProvider()));
    }

    @Test
    public void reqApiTest200() {
        String URI = "http://127.0.0.1:8080/test/200";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        ResDTO resDTO = this.exchangePostForMono(URI, requestBody).block();
        log.info(resDTO.toString());

        Assertions.assertTrue(resDTO.getStatus().is2xxSuccessful());
    }


    @Test
    public void reqApiTest400() {
        String URI = "http://127.0.0.1:8080/test/400";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        try {
            ResDTO resDTO = this.exchangePostForMono(URI, requestBody).block();
            log.info("이건 출력 안됨 resDTO: {}", resDTO);
        } catch (BadWebClientRequestException wre) {
            log.error("BadWebClientRequestException | msg: {}", wre.getMessage());
            Assertions.assertEquals(400, wre.getStatusCode());

            return;
        } catch (WebClientResponseException wre) {
            log.error("WebClientResponseException | msg: {}", wre.getMessage());
        } catch (WebClientException we) {
            log.error("WebClientException | msg: {}", we.getMessage());
        } catch (Exception e) {
            log.error("Exception | msg: {}", e.getMessage());
        }

        Assertions.fail();
    }

    @Test
    public void reqApiTest500() {
        String URI = "http://127.0.0.1:8080/test/500";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        try {
            ResDTO resDTO = this.exchangePostForMono(URI, requestBody).block();
            log.info("이건 출력 안됨 resDTO: {}", resDTO);
        } catch (BadWebClientRequestException bwre) {
            log.error("BadWebClientRequestException | msg: {}", bwre.getMessage());
        } catch (WebClientResponseException wre) {
            log.error("WebClientResponseException | msg: {}", wre.getMessage());
            Assertions.assertEquals(500, wre.getRawStatusCode());
            return;
        } catch (WebClientException we) {
            log.error("WebClientException | msg: {}", we.getMessage());
        } catch (Exception e) {
            log.error("Exception | msg: {}", e.getMessage());
        }

        Assertions.fail();
    }

    @Test
    public void reqApiTestTimeout() {
        String URI = "http://127.0.0.1:8080/test/timeout";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        try {
            ResDTO resDTO = this.exchangePostForMono(URI, requestBody).block();
            log.info("이건 출력 안됨 resDTO: {}", resDTO);
        } catch (BadWebClientRequestException bwre) {
            log.error("WebClientResponseException | msg: {}", bwre.getMessage());
        } catch (WebClientResponseException wre) {
            log.error("WebClientResponseException | msg: {}", wre.getMessage());
        } catch (WebClientException we) {
            log.error("WebClientException | msg: {}", we.getMessage());
            Assertions.assertTrue(we.getMessage().contains("ReadTimeoutException"));
            return;
        } catch (Exception e) {
            log.error("Exception | msg: {}", e.getMessage());
        }
    }

    private Mono<ResDTO> exchangePostForMono(String uri, MultiValueMap<String, String> body) throws WebClientResponseException {
        // @formatter:off
        return webClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body))
            .exchangeToMono(response ->
                response.bodyToMono(ResDTO.class)
                    .map(validReqVO -> {

                        if (response.statusCode().is2xxSuccessful()) {
                            log.info("API 요청에 성공했습니다.");
                            return validReqVO;
                        }

                        if (response.statusCode().is4xxClientError()) {
                            log.error("API 요청 중 4xx 에러가 발생했습니다. 요청 데이터를 확인해주세요.");
                            throw new BadWebClientRequestException(response.rawStatusCode(), String.format("4xx 외부 요청 오류. statusCode: %s, response: %s, header: %s", response.rawStatusCode(), response.bodyToMono(String.class), response.headers().asHttpHeaders()));
                        }

                        log.error("API 요청 중 Tree 서버에서 5xx 에러가 발생했습니다.");
                        throw new WebClientResponseException(response.rawStatusCode(), String.format("5xx 외부 시스템 오류. %s", response.bodyToMono(String.class)), response.headers().asHttpHeaders(), null, null);
                    })
            ).doOnError(error -> log.error("doOnError logging: "+ error));
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
