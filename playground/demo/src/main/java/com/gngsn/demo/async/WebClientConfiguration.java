package com.gngsn.demo.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientConfig {

    private final ObjectMapper OM;

    @Bean
    public WebClient webClient() {
        // @formatter:off
        //참고: https://lasel.kr/archives/740
        ConnectionProvider connectionProvider = ConnectionProvider.builder("webclient-pool")
            .maxConnections(500) //유지할 Connection Pool의 수, max 값 많큼 미리 생성해 놓지 않고 필요할때마다 생성(최대 생성 가능한 수)
            .pendingAcquireTimeout(Duration.ofMillis(5000)) //Connection Pool에서 사용할 수 있는 Connection 이 없을때 (모두 사용중일때) Connection을 얻기 위해 대기하는 시간
            .pendingAcquireMaxCount(-1) //커넥션 풀에서 커넥션을 가져오는 시도 횟수
            .evictInBackground(Duration.ofSeconds(30)) //백그라운드에서 만료된 connection을 제거하는 주기
            .maxIdleTime(Duration.ofSeconds(55L)) //커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간(주의: AWS ALB 타임아웃이 60초라서 60초보다 작은게 좋음)
            .maxLifeTime(Duration.ofSeconds(55L)) //커넥션 풀에서 살아있을 수 있는 커넥션의 최대 수명시간
            .build(); //keepAliveTimeout 보다 maxIdleTime 이 작은게 좋음(일반적으로 AWS나 nginx keeplive timeout이 60초인 경우 많음)

        //참고: https://yangbongsoo.tistory.com/30
        HttpClient nettyHttpClient = HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected(connection ->
                connection.addHandlerLast(new ReadTimeoutHandler(5)
                ).addHandlerLast(new WriteTimeoutHandler(5))
            )
            .responseTimeout(Duration.ofSeconds(6)); //idle 커넥션을 닫거나 맺는 시간을 고려하지 않은 순수 http 요청/응답 시간을 제한(connection timeout + 커넥션풀에서 얻는 시간 보다 무조건 커야함)

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(nettyHttpClient))
            .filter(applyAuthTokenHeader())
            .exchangeStrategies(customExchangeStrategies())
            .build();
        // @formatter:on
    }

    /**
     * WebClient 필터 추가: 모든 요청에 공통적인 인증 헤더 추가
     *
     * @return
     */
    private ExchangeFilterFunction applyAuthTokenHeader() {
        // @formatter:off
        return (req, next) -> {
            ClientRequest request = ClientRequest.from(req)
                .header("X-Req-Svc", "Test-Svc-Value") //TODO-FIXME: 임시값임으로 맞춰서 수정필요
                .header("X-Auth-Token", "Test-Auth-TOken-Value") //TODO-FIXME: 임시값임으로 맞춰서 수정필요
                .build();
            return next.exchange(request);
        };
        // @formatter:on
    }

    /**
     * HTTP 메시지 reader/writer 커스텀
     *
     * @return
     */
    private ExchangeStrategies customExchangeStrategies() {

        ExchangeStrategies jacksonStrategy = ExchangeStrategies.builder().codecs(config -> {
            config.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(OM, MediaType.APPLICATION_JSON));
            config.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(OM, MediaType.APPLICATION_JSON));
            //config.defaultCodecs().maxInMemorySize(1024 * 1024 * 2); //Memory 조정: 2M (default 256KB)
        }).build();
        return jacksonStrategy;
    }

}