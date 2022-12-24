package com.gngsn.dto;

public class ResDTO {
    Number statusCode;
    String message;

    public static ResDTO ok() {
        return new ResDTO(200, null);
    }

    public ResDTO(Number statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}