/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gngsn.webClient.util;

import com.gngsn.webClient.HttpContext;
import com.gngsn.webClient.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RequestUrlUtils.
 */
public final class RequestUrlUtils {

    private RequestUrlUtils() {
    }

    /**
     * Build the final request uri.
     *
     * @param exchange the exchange
     * @param domain   the domain
     * @return request uri
     */
    public static URI buildRequestUri(final ServerWebExchange exchange, final String domain) {
        String path = domain;
        final String rewriteUri = (String) exchange.getAttributes().get(Constants.REWRITE_URI);
        if (StringUtils.isNoneBlank(rewriteUri)) {
            path = path + rewriteUri;
        } else {
            HttpContext httpContext = exchange.getAttribute(Constants.CONTEXT);
            assert httpContext != null;
            String realUrl = httpContext.getRealUrl();
            if (StringUtils.isNoneBlank(realUrl)) {
                path = path + realUrl;
            }
        }
        if (StringUtils.isNoneBlank(exchange.getRequest().getURI().getQuery())) {
            path = String.join("?", path, getCodecQuery(exchange));
        }
        return URI.create(path);
    }

    /**
     * Gets codec query string.
     *
     * @param exchange the exchange
     * @return codec query string
     */
    public static String getCodecQuery(final ServerWebExchange exchange) {
        if (!exchange.getRequest().getURI().getRawQuery().contains("%")) {
            return exchange.getRequest().getURI().getQuery();
        }
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        return queryParams.keySet().stream()
            .map(key -> queryParams.get(key).stream()
                .map(item -> Optional.ofNullable(item)
                    .map(value -> String.join("=",
                        UriUtils.encode(key, StandardCharsets.UTF_8),
                        UriUtils.encode(value, StandardCharsets.UTF_8)))
                    .orElse(UriUtils.encode(key, StandardCharsets.UTF_8)))
                .filter(StringUtils::isNoneBlank)
                .collect(Collectors.joining("&")))
            .collect(Collectors.joining("&")).trim();
    }
}
