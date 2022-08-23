package com.gngsn.webClientTest;

import com.gngsn.webClientTest.exception.BadWebClientRequestException;
import com.gngsn.webClientTest.vo.ResVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static com.gngsn.webClientTest.config.WebClientConfiguration.COMMON_WEB_CLIENT;

@Slf4j
@RequiredArgsConstructor
public class Test {
    @Qualifier(value = COMMON_WEB_CLIENT)
    private final WebClient webClient;


    private void saveReqApi(String svcCd, String authKey) {
		String URI = "https://test.com";

        ResVO resVO = webClient
            .post()
            .uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(req))
            .exchangeToMono(response ->
                response.bodyToMono(ResVO.class).map(resVO -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return resVO;
                    }

                    if (response.statusCode().is4xxClientError()) {
                        log.error("API 요청 중 4xx 에러가 발생했습니다. 요청 데이터를 확인해주세요.");
                        throw new RuntimeException(String.format("Error Msg: %s, Error Cd: %s | request: %s", resVO.getMsg(), resVO.getErrorCd(), req));
                    }

                    log.error("API 요청 중 Tree 서버에서 5xx 에러가 발생했습니다.");
                    throw new RuntimeException(String.format("Error Msg: %s, Error Cd: %s | request: %s", resVO.getMsg(), resVO.getErrorCd(), req));
                }))
            .block();

        if (resVO == null || !resVO.isSuccess()) {
            throw new BadWebClientRequestException("JWT 발급에 실패했습니다.");
        }

//        jwt = OM.convertValue(apiResVO.getData(), TreeApiJwtDTO.Res.class);
    }
    
    public Mono<Object> postForMono(String uri, MultiValueMap<String, String> body) {
	return this.requestPostForMono(uri, body).bodyToMono(Object.class);
}

    
    private WebClient.ResponseSpec requestPostForMono(String uri, MultiValueMap<String, String> body) {
	// @formatter:off
       return webClient
               .post()
               .uri(uri)
               .bodyValue(body)
               .retrieve()
               .onStatus(HttpStatus::is4xxClientError, response ->
                       Mono.error(
                               new WebClientResponseException(
                                       response.rawStatusCode(),
                                       String.format("외부 요청 오류. %s", response.bodyToMono(String.class)),
                                       response.headers().asHttpHeaders(), null, null
                               )
                       )
               )
               .onStatus(HttpStatus::is5xxServerError, response ->
                       Mono.error(
                               new WebClientResponseException(
                                       response.rawStatusCode(),
                                       String.format("외부 시스템 오류. %s", response.bodyToMono(String.class)),
                                       response.headers().asHttpHeaders(), null, null
                               )
                       )
               );
   }

}
