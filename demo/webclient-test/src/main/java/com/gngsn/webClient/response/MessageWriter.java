package com.gngsn.webClient.response;


import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * The interface Message writer.
 */
public interface MessageWriter {

    /**
     * Write with exchange and shenyu plugin chain.
     *
     * @param exchange exchange the current server exchange
     * @param chain provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    Mono<Void> writeWith(ServerWebExchange exchange);
}
