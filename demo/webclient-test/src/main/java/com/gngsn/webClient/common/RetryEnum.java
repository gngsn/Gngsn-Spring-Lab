package com.gngsn.webClient.common;

/**
 * retry enum.
 */
public enum RetryEnum {

	/**
	 * Retry the previously failed call.
	 */
	CURRENT(1, "current", true),

	/**
	 * Retry other servers when failed.
	 */
	FAILOVER(2, "failover", true);

	private final int code;

	private final String name;

	private final boolean support;

	/**
	 * all args constructor.
	 *
	 * @param code    code
	 * @param name    name
	 * @param support support
	 */
	RetryEnum(final int code, final String name, final boolean support) {
		this.code = code;
		this.name = name;
		this.support = support;
	}

	/**
	 * get code.
	 *
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * get name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * get support.
	 *
	 * @return support
	 */
	public boolean isSupport() {
		return support;
	}
}
