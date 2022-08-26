package com.gngsn.webClientTest.exception;

import lombok.Getter;

@Getter
public class BadWebClientRequestException extends RuntimeException {
	private static final long serialVersionUID = -2113106875266819123L;

	private final int statusCode;

	private String statusText;

	public BadWebClientRequestException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public BadWebClientRequestException(int statusCode, String msg) {
		super(msg);
		this.statusCode = statusCode;
	}

	public BadWebClientRequestException(int statusCode, String msg, String statusText) {
		super(msg);
		this.statusCode = statusCode;
		this.statusText = statusText;
	}
}
