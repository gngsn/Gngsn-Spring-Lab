package com.gngsn.webClient.vo;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResResult extends ApiResult<Object> implements Serializable {

    /**
     * Instantiates a new shenyu result.
     */
    public ResResult() {

    }

    /**
     * Instantiates a new shenyu result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public ResResult(final Integer code, final String message, final Object data) {
        super(code, message, data);
    }

    /**
     * return success.
     *
     * @return {@linkplain ResResult}
     */
    public static ResResult success() {
        return success("");
    }

    /**
     * return success.
     *
     * @param msg msg
     * @return {@linkplain ResResult}
     */
    public static ResResult success(final String msg) {
        return success(msg, null);
    }

    /**
     * return success.
     *
     * @param data this is result data.
     * @return {@linkplain ResResult}
     */
    public static ResResult success(final Object data) {
        return success(null, data);
    }

    /**
     * return success.
     *
     * @param msg  this ext msg.
     * @param data this is result data.
     * @return {@linkplain ResResult}
     */
    public static ResResult success(final String msg, final Object data) {
        return get(HttpStatus.OK.value(), msg, data);
    }

    /**
     * return error .
     *
     * @param msg error msg
     * @return {@linkplain ResResult}
     */
    public static ResResult error(final String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    /**
     * return error .
     *
     * @param code error code
     * @param msg  error msg
     * @return {@linkplain ResResult}
     */
    public static ResResult error(final int code, final String msg) {
        return get(code, msg, null);
    }

    /**
     * return timeout .
     *
     * @param msg error msg
     * @return {@linkplain ResResult}
     */
    public static ResResult timeout(final String msg) {
        return error(HttpStatus.REQUEST_TIMEOUT.value(), msg);
    }

    private static ResResult get(final int code, final String msg, final Object data) {
        return new ResResult(code, msg, data);
    }


	@Override
	public String toString() {
		return "ResResult{"
			+ "code=" + getCode()
			+ ", message='" + getMessage()
			+ '\'' + ", data=" + getData()
			+ '}';
	}
}


