package com.gngsn.webClientTest.exception;

public class BadWebClientRequestException extends RuntimeException {
	private static final long serialVersionUID = -2113106875266819123L;

	public BadWebClientRequestException() {
		super();
	}

	public BadWebClientRequestException(String msg) {
		super(msg);
	}
}
