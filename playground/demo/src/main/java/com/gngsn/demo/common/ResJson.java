package com.gngsn.demo.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResJson {
    private int statusCode;
    private String msg;
    private Object data;

    public ResJson() {
    }

    public ResJson(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public ResJson(int statusCode, String msg, Object data) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }
}
