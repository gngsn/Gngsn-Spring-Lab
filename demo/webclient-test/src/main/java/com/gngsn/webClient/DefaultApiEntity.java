package com.gngsn.webClient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;

public class DefaultApiEntity  implements Serializable {

	private static final long serialVersionUID = -2792556188993845048L;

	private static final int ERROR = 500;

	private Integer code;

	private String message;

	@JsonBackReference
	private Object data;

	/**
	 * Instantiates a new shenyu result.
	 *
	 * @param code    the code
	 * @param message the message
	 * @param data    the data
	 */
	public DefaultApiEntity(final Integer code, final String message, final Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * Gets code.
	 *
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * Sets code.
	 *
	 * @param code the code
	 */
	public void setCode(final Integer code) {
		this.code = code;
	}

	/**
	 * Gets message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets message.
	 *
	 * @param message the message
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * Gets data.
	 *
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Sets data.
	 *
	 * @param data the data
	 */
	public void setData(final Object data) {
		this.data = data;
	}

	/**
	 * return error .
	 *
	 * @param msg error msg
	 * @return {@linkplain DefaultApiEntity}
	 */
	public static DefaultApiEntity error(final String msg) {
		return error(ERROR, msg);
	}

	/**
	 * return error .
	 *
	 * @param code error code
	 * @param msg  error msg
	 * @return {@linkplain DefaultApiEntity}
	 */
	public static DefaultApiEntity error(final int code, final String msg) {
		return get(code, msg, null);
	}

	/**
	 * return error .
	 *
	 * @param code error code
	 * @param msg  error msg
	 * @param data the data
	 * @return {@linkplain DefaultApiEntity}
	 */
	public static DefaultApiEntity error(final int code, final String msg, final Object data) {
		return get(code, msg, data);
	}

	/**
	 * return timeout .
	 *
	 * @param msg error msg
	 * @return {@linkplain DefaultApiEntity}
	 */
	public static DefaultApiEntity timeout(final String msg) {
		return error(ERROR, msg);
	}

	private static DefaultApiEntity get(final int code, final String msg, final Object data) {
		return new DefaultApiEntity(code, msg, data);
	}
}

