package com.gngsn.webClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultApiResult implements ApiResult<DefaultApiEntity> {

	/**
	 * logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultApiResult.class);

	@Override
	public DefaultApiEntity error(final int code, final String message, final Object object) {
		return DefaultApiEntity.error(code, message, object);
	}
}
