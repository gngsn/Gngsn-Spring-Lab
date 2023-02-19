package com.gngsn.webClient.vo;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class ResResult implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;

    private String message;

    private Object data;

    public ResResult() {
    }

    public ResResult(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    /**
     * return success.
     *
     * @param msg msg
     */
    public static ResResult success(final String msg) {
        return success(msg, null);
    }

    /**
     * return success.
     *
     * @param msg  this ext msg.
     * @param data this is result data.
     */
    public static ResResult success(final String msg, final Object data) {
        return get(msg, data);
    }

    /**
     * return error.
     *
     * @param msg error msg
     */
    public static ResResult error(final String msg) {
        return get(msg, null);
    }

    private static ResResult get(final String msg, final Object data) {
        return new ResResult(msg, data);
    }


    @Override
    public String toString() {
        return "ResResult{"
            + "message='" + getMessage()
            + '\'' + ", data=" + getData()
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResResult resResult = (ResResult) o;
        return Objects.equals(message, resResult.message) && Objects.equals(data, resResult.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, data);
    }
}


