package com.gngsn.webClient;


import com.gngsn.webClient.common.ApiResultEnum;
import com.gngsn.webClient.common.Constants;
import com.gngsn.webClient.common.RetryEnum;
import com.gngsn.webClient.util.RequestUrlUtils;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type abstract http client plugin.
 */
public abstract class AbstractHttpClientPlugin<R> {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractHttpClientPlugin.class);

    public final Mono<R> request(final ServerWebExchange exchange) {
        final URI uri = exchange.getAttribute(Constants.HTTP_URI);

        if (Objects.isNull(uri)) {
            return Mono.error(() -> new Throwable(ApiResultEnum.CANNOT_FIND_URL.getMsg()));
        }

        final long timeout = (long) Optional.ofNullable(exchange.getAttribute(Constants.HTTP_TIME_OUT)).orElse(3000L);
        final Duration duration = Duration.ofMillis(timeout);
        final int retryTimes = (int) Optional.ofNullable(exchange.getAttribute(Constants.HTTP_RETRY)).orElse(0);
        final String retryStrategy = (String) Optional.ofNullable(exchange.getAttribute(Constants.RETRY_STRATEGY)).orElseGet(RetryEnum.CURRENT::getName);
        LOG.info("The request urlPath is {}, retryTimes is {}, retryStrategy is {}", uri.toASCIIString(), retryTimes, retryStrategy);
        final HttpHeaders httpHeaders = buildHttpHeaders(exchange);

        return request(exchange, uri, duration, retryTimes, retryStrategy, httpHeaders);
    }

    private Mono<R> request(ServerWebExchange exchange, URI uri, Duration duration, int retryTimes, String retryStrategy, HttpHeaders httpHeaders) {

        final Mono<R> response = request(
            /* ServerWebExchange */ exchange,
            /* MethodValue */ exchange.getRequest().getMethodValue(),
            /* Uri */ uri,
            /* HttpHeaders */ httpHeaders,
            /* RequestBody */ exchange.getRequest().getBody()
        )
            .timeout(duration, Mono.error(new TimeoutException("Response took longer than timeout: " + duration)))
            .doOnError(e -> LOG.error(e.getMessage(), e));


        if (RetryEnum.CURRENT.getName().equals(retryStrategy)) {
			/*
				RETRY
			*/
            RetryBackoffSpec retryBackoffSpec = Retry.backoff(retryTimes, Duration.ofMillis(20L))
                .maxBackoff(Duration.ofSeconds(20L))
                .transientErrors(true)
                .jitter(0.5d)
                .filter(t -> t instanceof TimeoutException || t instanceof ConnectTimeoutException
                    || t instanceof ReadTimeoutException || t instanceof IllegalStateException);

            return response.retryWhen(retryBackoffSpec)
                .onErrorMap(TimeoutException.class, th -> new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, th.getMessage(), th));
        }

        HashSet<URI> exclude = new HashSet(1);
        Collections.addAll(exclude, uri);

        return resend(response, exchange, duration, httpHeaders, retryTimes)
            .onErrorMap(TimeoutException.class, th -> new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, th.getMessage(), th));
    }

    private Mono<R> resend(final Mono<R> clientResponse,
                           final ServerWebExchange exchange,
                           final Duration duration,
                           final HttpHeaders httpHeaders,
                           final int retryTimes) {
        Mono<R> result = clientResponse;

        for (int i = 0; i < retryTimes; i++) {
            result = resend(result, exchange, duration, httpHeaders);
        }

        return result;
    }

    /**
     * without retry
     *
     * @param response
     * @param exchange
     * @param duration
     * @param httpHeaders
     * @return
     */
    private Mono<R> resend(final Mono<R> response,
                           final ServerWebExchange exchange,
                           final Duration duration,
                           final HttpHeaders httpHeaders) {

        return response.onErrorResume(th -> {
            final String uri = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();

            return request(exchange, exchange.getRequest().getMethodValue(), RequestUrlUtils.buildRequestUri(exchange, uri), httpHeaders, exchange.getRequest().getBody())
                .timeout(duration, Mono.error(new TimeoutException("Response took longer than timeout: " + duration)))
                .doOnError(e -> LOG.error(e.getMessage(), e));
        });
    }

    /**
     * build request domain.
     *
     * @return domain
     */
//	public String buildDomain() {
//		String protocol = this.getProtocol();
//		if (StringUtils.isBlank(protocol)) {
//			protocol = "http://";
//		}
//		return protocol + this.getUrl().trim();
//	}

    /**
     * Build the http request headers.
     *
     * @param exchange the current server exchange
     * @return HttpHeaders
     */
    private HttpHeaders buildHttpHeaders(final ServerWebExchange exchange) {
        final HttpHeaders headers = new HttpHeaders();
        headers.addAll(exchange.getRequest().getHeaders());

        List<String> acceptEncoding = headers.get(HttpHeaders.ACCEPT_ENCODING);

        if (!CollectionUtils.isEmpty(acceptEncoding)) {
            acceptEncoding = Stream.of(String.join(",", acceptEncoding).split(",")).collect(Collectors.toList());
            acceptEncoding.remove(Constants.HTTP_ACCEPT_ENCODING_GZIP);
            headers.set(HttpHeaders.ACCEPT_ENCODING, String.join(",", acceptEncoding));
        }
        return headers;
    }

    /**
     * Process the Web request.
     *
     * @param exchange    the current server exchange
     * @param httpMethod  http method, eg.POST
     * @param uri         the request uri
     * @param httpHeaders the request header
     * @param body        the request body
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */

    public abstract Mono<R> request(final ServerWebExchange exchange, final String httpMethod, final URI uri,
                                    final HttpHeaders httpHeaders, final Flux<DataBuffer> body);

}
