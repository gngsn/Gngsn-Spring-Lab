package com.gngsn.webClient;


import com.gngsn.webClient.common.ApiResultEnum;
import com.gngsn.webClient.common.Constants;
import com.gngsn.webClient.exception.WebClientException;
import com.gngsn.webClient.util.WebFluxResultUtils;
import com.google.common.collect.Sets;
import com.gngsn.webClient.common.RetryEnum;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type abstract http client plugin.
 */
public abstract class AbstractHttpClientPlugin<R> {

	protected static final Logger LOG = LoggerFactory.getLogger(AbstractHttpClientPlugin.class);

//	public final Mono<Void> execute(final ServerWebExchange exchange) {
//		final URI uri = exchange.getAttribute(Constants.HTTP_URI);
//		if (Objects.isNull(uri)) {
//			Object error = ApiResultWrap.error(exchange, ApiResultEnum.CANNOT_FIND_URL);
//			return WebFluxResultUtils.result(exchange, error);
//		}
//
//		final long timeout = (long) Optional.ofNullable(exchange.getAttribute(Constants.HTTP_TIME_OUT)).orElse(3000L);
//		final Duration duration = Duration.ofMillis(timeout);
//		final int retryTimes = (int) Optional.ofNullable(exchange.getAttribute(Constants.HTTP_RETRY)).orElse(0);
//		final String retryStrategy = (String) Optional.ofNullable(exchange.getAttribute(Constants.RETRY_STRATEGY)).orElseGet(RetryEnum.CURRENT::getName);
//		LOG.info("The request urlPath is {}, retryTimes is {}, retryStrategy is {}", uri.toASCIIString(), retryTimes, retryStrategy);
//		final HttpHeaders httpHeaders = buildHttpHeaders(exchange);
//		final Mono<R> response = doRequest(exchange, exchange.getRequest().getMethodValue(), uri, httpHeaders, exchange.getRequest().getBody())
//			.timeout(duration, Mono.error(new TimeoutException("Response took longer than timeout: " + duration)))
//			.doOnError(e -> LOG.error(e.getMessage(), e));
//
//		if (RetryEnum.CURRENT.getName().equals(retryStrategy)) {
//			//old version of DividePlugin and SpringCloudPlugin will run on this
//			RetryBackoffSpec retryBackoffSpec = Retry.backoff(retryTimes, Duration.ofMillis(20L))
//				.maxBackoff(Duration.ofSeconds(20L))
//				.transientErrors(true)
//				.jitter(0.5d)
//				.filter(t -> t instanceof TimeoutException || t instanceof ConnectTimeoutException
//					|| t instanceof ReadTimeoutException || t instanceof IllegalStateException);
//			return response.retryWhen(retryBackoffSpec)
//				.onErrorMap(TimeoutException.class, th -> new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, th.getMessage(), th));
//		}
//
//		HashSet<URI> exclude = new HashSet(1);
//		Collections.addAll(exclude, uri);
//
//		return resend(response, exchange, duration, httpHeaders, exclude, retryTimes)
//			.onErrorMap(TimeoutException.class, th -> new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, th.getMessage(), th))
//			.flatMap((Function<Object, Mono<? extends Void>>) o -> chain.execute(exchange));
//	}

//	private Mono<R> resend(final Mono<R> clientResponse,
//		final ServerWebExchange exchange,
//		final Duration duration,
//		final HttpHeaders httpHeaders,
//		final Set<URI> exclude,
//		final int retryTimes) {
//		Mono<R> result = clientResponse;
//		for (int i = 0; i < retryTimes; i++) {
//			result = resend(result, exchange, duration, httpHeaders, exclude);
//		}
//		return result;
//	}

//	private Mono<R> resend(final Mono<R> response,
//		final ServerWebExchange exchange,
//		final Duration duration,
//		final HttpHeaders httpHeaders,
//		final Set<URI> exclude) {
//		// does it necessary to add backoff interval time ?
//		return response.onErrorResume(th -> {
//			final String selectorId = exchange.getAttribute(Constants.DIVIDE_SELECTOR_ID);
//			final String loadBalance = exchange.getAttribute(Constants.LOAD_BALANCE);
//			//always query the latest available list
//			final List<Upstream> upstreamList = UpstreamCacheManager.getInstance().findUpstreamListBySelectorId(selectorId)
//				.stream().filter(data -> {
//					final String trimUri = data.getUrl().trim();
//					for (URI needToExclude : exclude) {
//						// exclude already called
//						if ((needToExclude.getHost() + ":" + needToExclude.getPort()).equals(trimUri)) {
//							return false;
//						}
//					}
//					return true;
//				}).collect(Collectors.toList());
//			if (CollectionUtils.isEmpty(upstreamList)) {
//				// no need to retry anymore
//				return Mono.error(new WebClientException(ApiResultEnum.CANNOT_FIND_HEALTHY_UPSTREAM_URL_AFTER_FAILOVER.getMsg()));
//			}
//			final String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
//			final Upstream upstream = LoadBalancerFactory.selector(upstreamList, loadBalance, ip);
//			if (Objects.isNull(upstream)) {
//				// no need to retry anymore
//				return Mono.error(new WebClientException(ApiResultEnum.CANNOT_FIND_HEALTHY_UPSTREAM_URL_AFTER_FAILOVER.getMsg()));
//			}
//			final URI newUri = RequestUrlUtils.buildRequestUri(exchange, upstream.buildDomain());
//			// in order not to affect the next retry call, newUri needs to be excluded
//			exclude.add(newUri);
//			return doRequest(exchange, exchange.getRequest().getMethodValue(), newUri, httpHeaders, exchange.getRequest().getBody())
//				.timeout(duration, Mono.error(new TimeoutException("Response took longer than timeout: " + duration)))
//				.doOnError(e -> LOG.error(e.getMessage(), e));
//		});
//	}
//
//	/**
//	 * Build the http request headers.
//	 *
//	 * @param exchange the current server exchange
//	 * @return HttpHeaders
//	 */
//	private HttpHeaders buildHttpHeaders(final ServerWebExchange exchange) {
//		final HttpHeaders headers = new HttpHeaders();
//		headers.addAll(exchange.getRequest().getHeaders());
//		// remove gzip
//		List<String> acceptEncoding = headers.get(HttpHeaders.ACCEPT_ENCODING);
//		if (CollectionUtils.isNotEmpty(acceptEncoding)) {
//			acceptEncoding = Stream.of(String.join(",", acceptEncoding).split(",")).collect(Collectors.toList());
//			acceptEncoding.remove(Constants.HTTP_ACCEPT_ENCODING_GZIP);
//			headers.set(HttpHeaders.ACCEPT_ENCODING, String.join(",", acceptEncoding));
//		}
//		return headers;
//	}

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
	protected abstract Mono<R> doRequest(ServerWebExchange exchange, String httpMethod,
		URI uri, HttpHeaders httpHeaders, Flux<DataBuffer> body);

}
