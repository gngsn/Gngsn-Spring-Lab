package com.gngsn.webClient.vo;

import java.io.Serializable;
import java.util.Objects;

public class ApiResult<T> implements Serializable {

	private static final long serialVersionUID = -2792556188993845048L;

	private Integer code;

	private String message;

	private T data;

	/**
	 * Instantiates a new shenyu result.
	 */
	public ApiResult() {

	}

	/**
	 * Instantiates a new shenyu result.
	 *
	 * @param code    the code
	 * @param message the message
	 * @param data    the data
	 */
	public ApiResult(final Integer code, final String message, final T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}


	/**
	 * Gets the value of code.
	 *
	 * @return the value of code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code code
	 */
	public void setCode(final Integer code) {
		this.code = code;
	}

	/**
	 * Gets the value of message.
	 *
	 * @return the value of message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message message
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * Gets the value of data.
	 *
	 * @return the value of data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data data
	 */
	public void setData(final T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ApiResult{"
			+ "code=" + code
			+ ", message='" + message
			+ '\'' + ", data=" + data
			+ '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ApiResult)) {
			return false;
		}
		@SuppressWarnings("all")
		ApiResult<T> that = (ApiResult<T>) o;
		return Objects.equals(code, that.code) && Objects.equals(message, that.message) && Objects.equals(data, that.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, message, data);
	}
}


