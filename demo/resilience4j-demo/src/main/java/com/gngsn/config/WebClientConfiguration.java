package com.gngsn.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;


/**
 * webclient bean 설정
 * - 기본적으로 비동기로 작동, 동기로 작동하게 하고 싶다면 block 사용
 *
 * @Author 박경선
 */
@Configuration
public class WebClientConfiguration {

    public static final ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    public static final String COMMON_WEB_CLIENT = "commonWebClient";
    public static final String WEB_CLIENT_HTTP_CLIENT = "defaultHttpClient";
    public static final String WEB_CLIENT_CONNECTION_PROVIDER = "connectionProvider";
    public static final String WEB_CLIENT_EXCHANGE_STRATEGIES = "defaultExchangeStrategies";

    @Bean(name = COMMON_WEB_CLIENT)
    static public WebClient commonWebClient(
        @Qualifier(value = WEB_CLIENT_EXCHANGE_STRATEGIES) ExchangeStrategies exchangeStrategies,
        @Qualifier(value = WEB_CLIENT_HTTP_CLIENT) HttpClient httpClient
        ) {
        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .exchangeFunction(ExchangeFunctions.create(new ReactorClientHttpConnector(httpClient), exchangeStrategies))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    @Bean(name = WEB_CLIENT_HTTP_CLIENT)
    static public HttpClient defaultHttpClient(@Qualifier(value = WEB_CLIENT_CONNECTION_PROVIDER) ConnectionProvider provider) {

        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5)) //읽기시간초과 타임아웃
                        .addHandlerLast(new WriteTimeoutHandler(5)));
    }

    @Bean(name = WEB_CLIENT_CONNECTION_PROVIDER)
    static public ConnectionProvider connectionProvider() {

        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)     // connection pool의 갯수
                .pendingAcquireTimeout(Duration.ofMillis(0)) //커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
                .pendingAcquireMaxCount(-1) //커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
                .maxIdleTime(Duration.ofMillis(2000L)) //커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
                .build();
    }

    @Bean(name = WEB_CLIENT_EXCHANGE_STRATEGIES)
    static public ExchangeStrategies defaultExchangeStrategies() {

        return ExchangeStrategies.builder().codecs(config -> {
            config.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(OM, MediaType.APPLICATION_JSON));
            config.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(OM, MediaType.APPLICATION_JSON));
            config.defaultCodecs().maxInMemorySize(1024 * 1024); // max buffer 1MB 고정. default: 256 * 1024
        }).build();
    }
}
