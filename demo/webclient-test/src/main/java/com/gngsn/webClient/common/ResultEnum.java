package com.gngsn.webClient.common;

public enum ResultEnum {

	/**
	 * Success result enum.
	 */
	SUCCESS("success"),

	/**
	 * Time out result enum.
	 */
	TIME_OUT("timeOut"),

	/**
	 * Error result enum.
	 */
	ERROR("error")
	;

	private final String name;

	/**
	 * all args constructor.
	 *
	 * @param name name
	 */
	ResultEnum(final String name) {
		this.name = name;
	}

	/**
	 * get name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}
}
