package com.gngsn.webClient.util;

import com.gngsn.webClient.ApiResult;
import com.gngsn.webClient.ApiResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * The type api result utils.
 */
public final class WebFluxResultUtils {

	/**
	 * result utils log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(WebFluxResultUtils.class);

	private WebFluxResultUtils() {
	}

	/**
	 * Response result.
	 *
	 * @param exchange the exchange
	 * @param result    the result
	 * @return the result
	 */
	public static Mono<Void> result(final ServerWebExchange exchange, final Object result) {
		if (Objects.isNull(result)) {
			return Mono.empty();
		}
		final ApiResult<?> apiResult = ApiResultWrap.apiResult();

		Object resultData = apiResult.format(exchange, result);
		// basic data use text/plain

		MediaType mediaType = MediaType.TEXT_PLAIN;

		if (!ObjectTypeUtils.isBasicType(result)) {
			mediaType = apiResult.contentType(exchange);
		}

		exchange.getResponse().getHeaders().setContentType(mediaType);
		final Object responseData = apiResult.result(exchange, resultData);
		assert null != responseData;

		final byte[] bytes = (responseData instanceof byte[])
			? (byte[]) responseData : responseData.toString().getBytes(StandardCharsets.UTF_8);
		return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
				.bufferFactory().wrap(bytes))
			.doOnNext(data -> exchange.getResponse().getHeaders().setContentLength(data.readableByteCount())));
	}
}
