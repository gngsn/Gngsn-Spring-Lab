package com.gngsn.webClient.util;


import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

/**
 * ResponseUtils.
 */
public final class ResponseUtils {

    private static final String CHUNKED = "chunked";

    private ResponseUtils() {
    }


    /**
     * build client response with current response data.
     *
     * @param response current response
     * @param body     current response body
     * @return the client response
     */
    public static ClientResponse buildClientResponse(final ServerHttpResponse response,
                                                     final Publisher<? extends DataBuffer> body) {
        ClientResponse.Builder builder = ClientResponse.create(Objects.requireNonNull(response.getStatusCode()), getReaders());
        return builder
            .headers(headers -> headers.putAll(response.getHeaders()))
            .cookies(cookies -> response.getCookies())
            .body(Flux.from(body)).build();
    }

    /**
     * Chunked Headers.
     *
     * @param headers headers.
     * @return chunked headers
     */
    public static HttpHeaders chunkedHeader(final HttpHeaders headers) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(headers);
        fixHeaders(httpHeaders);
        return httpHeaders;
    }

    /**
     * Gets reads from ServerCodecConfigurer with custom the codec.
     * @return ServerCodecConfigurer readers
     */
    private static List<HttpMessageReader<?>> getReaders() {
        return SpringBeanUtils.getInstance().getBean(ServerCodecConfigurer.class).getReaders();
    }

    /**
     * fix headers.
     *
     * @param httpHeaders the headers
     */
    private static void fixHeaders(final HttpHeaders httpHeaders) {
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, CHUNKED);
    }
}

