package com.gngsn.webClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gngsn.webClient.common.Constants;
import com.gngsn.webClient.util.ObjectTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;

import java.io.IOException;
import java.util.Objects;

public interface HttpResult<T> {
	/**
	 * logger.
	 */
	static final Logger LOG = LoggerFactory.getLogger(HttpResult.class);

	static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * The response result.
	 *
	 * @param exchange the exchange
	 * @param formatted the formatted data that is origin data(basic、byte[]) or json string
	 * @return the result object
	 */
	default Object result(ServerWebExchange exchange, Object formatted) {
		return formatted;
	}

	/**
	 * format the origin, default is json format except the basic and bytes.
	 *
	 * @param exchange the exchange
	 * @param origin the origin
	 * @return format origin
	 */
	default Object format(ServerWebExchange exchange, Object origin) {
		// basic data or upstream data
		if (ObjectTypeUtils.isBasicType(origin) || (origin instanceof byte[])) {
			return origin;
		}
		// error result or rpc origin result.
		return this.toJson(origin);
	}

	default String toJson(final Object object) {
		try {
			return MAPPER.writeValueAsString(object);
		} catch (IOException e) {
			LOG.warn("write to json string error: " + object, e);
			return Constants.EMPTY_JSON;
		}
	}

	/**
	 * the response context type, default is application/json.
	 *
	 * @param exchange the exchange
	 * @param formatted the formatted data that is origin data(basic、byte[]) or json string
	 * @return the context type
	 */
	default MediaType contentType(ServerWebExchange exchange, Object formatted) {
		final ClientResponse clientResponse = exchange.getAttribute(Constants.CLIENT_RESPONSE_ATTR);
		if (Objects.nonNull(clientResponse) && clientResponse.headers().contentType().isPresent()) {
			return clientResponse.headers().contentType().get();
		}
		return MediaType.APPLICATION_JSON;
	}

	/**
	 * Error t.
	 *
	 * @param exchange the exchange
	 * @param code    the code
	 * @param message the message
	 * @param object  the object
	 * @return the t
	 */
	default T error(ServerWebExchange exchange, int code, String message, Object object) {
		return error(code, message, object);
	}

	/**
	 * Error t.
	 *
	 * @param code    the code
	 * @param message the message
	 * @param object  the object
	 * @return the t
	 */
	default T error(int code, String message, Object object) {
		return null;
	}
}
