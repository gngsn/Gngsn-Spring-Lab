package com.gngsn.webClient.util;

import com.gngsn.webClient.ApiResult;
import com.gngsn.webClient.ApiResultWrap;
import com.gngsn.webClient.common.ApiResultEnum;
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
			mediaType = apiResult.contentType(exchange, resultData);
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

	/**
	 * get no selector result.
	 *
	 * @param pluginName the plugin name
	 * @param exchange   the exchange
	 * @return the mono
	 */
	public static Mono<Void> noSelectorResult(final String pluginName, final ServerWebExchange exchange) {
		LOG.error("can not match selector data: {}", pluginName);
		Object error = ApiResultWrap.error(exchange, ApiResultEnum.SELECTOR_NOT_FOUND.getCode(), pluginName + ":" + ApiResultEnum.SELECTOR_NOT_FOUND.getMsg(), null);
		return WebFluxResultUtils.result(exchange, error);
	}

	/**
	 * get no rule result.
	 *
	 * @param pluginName the plugin name
	 * @param exchange   the exchange
	 * @return the mono
	 */
	public static Mono<Void> noRuleResult(final String pluginName, final ServerWebExchange exchange) {
		LOG.error("can not match rule data: {}", pluginName);
		Object error = ApiResultWrap.error(exchange, ApiResultEnum.RULE_NOT_FOUND.getCode(), pluginName + ":" + ApiResultEnum.RULE_NOT_FOUND.getMsg(), null);
		return WebFluxResultUtils.result(exchange, error);
	}
}
