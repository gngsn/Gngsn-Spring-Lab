package com.gngsn.webClient.util;


import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * The type abstract http client plugin.
 */
public class WebClientUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(WebClientUtils.class);

    private final WebClient webClient;

    public WebClientUtils(WebClient webClient) {
        this.webClient = webClient;
    }

    public final Mono<ClientResponse> request(final URI uri, final String method, final Flux<DataBuffer> body) {
        return request(uri, method, body, 3000L, 0, new HttpHeaders());
    }

    public final Mono<ClientResponse> request(final URI uri, final String method, final Flux<DataBuffer> body, Long timeout) {
        return request(uri, method, body, timeout, 0, new HttpHeaders());
    }

    public final Mono<ClientResponse> request(final URI uri, final String method, final Flux<DataBuffer> body, Long timeout, final int retryTimes) {
        return request(uri, method, body, timeout, retryTimes, new HttpHeaders());
    }

    public final Mono<ClientResponse> request(final URI uri, final String method, final Flux<DataBuffer> body, Long timeout, final int retryTimes, final HttpHeaders httpHeaders) {
        if (Objects.isNull(uri)) {
            return Mono.error(() -> new Throwable("Can not find url, please check your configuration!"));
        }

        final Duration duration = Duration.ofMillis(timeout);
        LOG.info("The request urlPath is {}, retryTimes is {}", uri.toASCIIString(), retryTimes);

        final Mono<ClientResponse> response = request(method, uri, httpHeaders, body)
            .timeout(duration, Mono.error(new TimeoutException("Response took longer than timeout: " + duration)))
            .doOnError(e -> LOG.error(e.getMessage(), e));

        // RETRY
        RetryBackoffSpec retryBackoffSpec = Retry.backoff(retryTimes, Duration.ofMillis(20L))
            .maxBackoff(Duration.ofSeconds(20L))
            .transientErrors(true)
            .jitter(0.5d)
            .filter(t -> t instanceof TimeoutException || t instanceof ConnectTimeoutException
                || t instanceof ReadTimeoutException || t instanceof IllegalStateException);

        return response.retryWhen(retryBackoffSpec)
            .onErrorMap(TimeoutException.class, th -> new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, th.getMessage(), th));
    }


    /**
     * Process the Web request.
     *
     * @param httpMethod  http method, eg.POST
     * @param uri         the request uri
     * @param httpHeaders the request header
     * @param body        the request body
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */

    public Mono<ClientResponse> request(final String httpMethod, final URI uri, final HttpHeaders httpHeaders, final Flux<DataBuffer> body) {

        return webClient.method(HttpMethod.valueOf(httpMethod)).uri(uri)
            .headers(headers -> headers.addAll(httpHeaders))
            .body(BodyInserters.fromDataBuffers(body))
            .exchangeToMono(response -> response.bodyToMono(byte[].class)
                .flatMap(bytes -> Mono.fromCallable(() -> Optional.ofNullable(bytes))).defaultIfEmpty(Optional.empty())
                .flatMap(option -> {
                    final ClientResponse.Builder builder = ClientResponse.create(response.statusCode())
                        .headers(headers -> headers.addAll(response.headers().asHttpHeaders()))
                        .cookies(cookies -> cookies.addAll(response.cookies()));
                    return Mono.just(builder.build());
                }));
//            .doOnSuccess(res -> {
////                if (res.statusCode().is2xxSuccessful()) {
////                    exchange.getAttributes().put(Constants.CLIENT_RESPONSE_RESULT_TYPE, ResultEnum.SUCCESS.getName());
////                } else {
////                    exchange.getAttributes().put(Constants.CLIENT_RESPONSE_RESULT_TYPE, ResultEnum.ERROR.getName());
////                }
////                exchange.getResponse().setStatusCode(res.statusCode());
////                exchange.getAttributes().put(Constants.CLIENT_RESPONSE_ATTR, res);
//            });
    }
}
