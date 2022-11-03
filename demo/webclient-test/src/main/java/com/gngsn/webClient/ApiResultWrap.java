package com.gngsn.webClient;

import com.gngsn.webClient.common.ApiResultEnum;
import com.gngsn.webClient.util.SpringBeanUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * The type api result warp.
 */
public final class ApiResultWrap {

	private ApiResultWrap() {
	}

	/**
	 * Success object.
	 *
	 * @param exchange the exchange
	 * @param object  the object
	 * @return the success object
	 */
	public static Object success(final ServerWebExchange exchange, final Object object) {
		return apiResult().result(exchange, object);
	}

	/**
	 * Error object.
	 *
	 * @param exchange the exchange
	 * @param apiResult  the apiResult
	 * @param object  the object
	 * @return the object
	 */
	public static Object error(final ServerWebExchange exchange, final ApiResultEnum apiResult, final Object object) {
		return apiResult().error(exchange, apiResult.getCode(), apiResult.getMsg(), object);
	}

	/**
	 * Error object.
	 *
	 * @param apiResult the apiResult
	 * @param object  the object
	 * @return the object
	 */
	public static Object error(final ApiResultEnum apiResult, final Object object) {
		return apiResult().error(apiResult.getCode(), apiResult.getMsg(), object);
	}

	/**
	 * Error object.
	 *
	 * @param exchange the exchange
	 * @param apiResult the apiResult
	 * @return the object
	 */
	public static Object error(final ServerWebExchange exchange, final ApiResultEnum apiResult) {
		return apiResult().error(exchange, apiResult.getCode(), apiResult.getMsg(), null);
	}

	/**
	 * Error object.
	 *
	 * @param exchange the exchange
	 * @param code    the code
	 * @param message the message
	 * @param object  the object
	 * @return the object
	 */
	public static Object error(final ServerWebExchange exchange, final int code, final String message, final Object object) {
		return apiResult().error(exchange, code, message, object);
	}

	/**
	 * api result bean.
	 *
	 * @return the api result bean.
	 */
	public static HttpResult<?> apiResult() {
		return SpringBeanUtils.getInstance().getBean(HttpResult.class);
	}
}
