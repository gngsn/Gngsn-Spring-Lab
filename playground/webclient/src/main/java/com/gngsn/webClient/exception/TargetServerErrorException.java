package com.gngsn.webClient.exception;

public class TargetServerErrorException extends RuntimeException {
	private static final long serialVersionUID = -2113106875266819123L;

	private final int statusCode;

	private String statusText;

	public TargetServerErrorException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public TargetServerErrorException(int statusCode, String msg) {
		super(msg);
		this.statusCode = statusCode;
	}

	public TargetServerErrorException(int statusCode, String msg, String statusText) {
		super(msg);
		this.statusCode = statusCode;
		this.statusText = statusText;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusText() {
		return statusText;
	}
}
