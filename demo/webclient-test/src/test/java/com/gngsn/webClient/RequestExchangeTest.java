package com.gngsn.webClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.webClient.exception.BadWebClientRequestException;
import com.gngsn.webClient.vo.ResResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import static com.gngsn.webClient.config.WebClientConfiguration.*;

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

        ResResult resResult = this.exchangePostForMono(URI, requestBody).block();
        log.info(resResult.toString());

        Assertions.assertEquals(200, resResult.getCode());
    }

    @Mock
    private ExchangeFunction exchangeFunction;

    @Captor
    private ArgumentCaptor<ClientRequest> captor;

    @Test
    public void reqApi200Subscribe() throws InterruptedException {
        String URI = "http://127.0.0.1:8080/test/200";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        Mono<ResResult> res =
            WebClient
            .builder()
//            .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//            .exchangeFunction(this.exchangeFunction)
            .build()
            .post()
            .uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono(response ->
                response.bodyToMono(ResResult.class)
                    .map(validReqVO -> {
                        log.info("API 요청에 성공했습니다.");
                        return validReqVO;
                    })
            );

            res.subscribe(
                new Subscriber<ResResult>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ResResult resResult) {
                        log.info("result: " + resResult.toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            Thread.sleep(1000);
//        StepVerifier.create(res)
//            .expectNext(ResResult.success("Request res 200"))
//            .verifyComplete();
//        resResultMono.doOnError(throwable -> log.error(throwable.getMessage()))
//            .doOnNext(resResult -> {
//                log.info("result: " + resResult);
//                Assertions.assertEquals(200, resResult.getCode());
//            })
//            .subscribe(resResult -> {
//            log.info("result: " + resResult.toString());
//
//            Assertions.assertEquals(200, resResult.getCode());
//        });
    }


    @Test
    public void reqApiTest400() {
        String URI = "http://127.0.0.1:8080/test/400";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        try {
            ResResult resDTO = this.exchangePostForMono(URI, requestBody).block();
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
            ResResult resDTO = this.exchangePostForMono(URI, requestBody).block();
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
            ResResult resDTO = this.exchangePostForMono(URI, requestBody).block();
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

    private Mono<ResResult> exchangePostForMono(String uri, MultiValueMap<String, String> body) throws WebClientResponseException {
        // @formatter:off
        return webClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body))
            .exchangeToMono(response ->
                response.bodyToMono(ResResult.class)
                    .map(validReqVO -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            log.info("API 요청에 성공했습니다.");
                            return validReqVO;
                        }

                        if (response.statusCode().is4xxClientError()) {
                            log.error("API 요청 중 4xx 에러가 발생했습니다. 요청 데이터를 확인해주세요.");
                            throw new BadWebClientRequestException(response.rawStatusCode(), String.format("4xx 외부 요청 오류. statusCode: %s, response: %s, header: %s", response.rawStatusCode(), response.bodyToMono(String.class), response.headers().asHttpHeaders()));
                        }

                        log.error("API 요청 중 서버에서 5xx 에러가 발생했습니다.");
                        throw new WebClientResponseException(response.rawStatusCode(), String.format("5xx 외부 시스템 오류. %s", response.bodyToMono(String.class)), response.headers().asHttpHeaders(), null, null);
                    })
            )
            .doOnError(error -> log.error("doOnError logging: " + error));
    }
}
