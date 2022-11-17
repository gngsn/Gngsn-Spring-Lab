package com.gngsn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;


/**
 * The type Web client plugin.
 */
public class WebClientPlugin extends AbstractHttpClientPlugin<ClientResponse> {

    Logger log = LoggerFactory.getLogger(WebClientPlugin.class);
    private final WebClient webClient;

    /**
     * Instantiates a new Web client plugin.
     *
     * @param webClient the web client
     */
    public WebClientPlugin(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ClientResponse> request(final String httpMethod,
                                        final URI uri, final HttpHeaders httpHeaders, final Flux<DataBuffer> body) {

        return webClient.method(HttpMethod.valueOf(httpMethod)).uri(uri)
            .headers(headers -> headers.addAll(httpHeaders))
            .body(BodyInserters.fromDataBuffers(body))
            .exchangeToMono(response -> response.bodyToMono(byte[].class)
                .flatMap(bytes -> Mono.fromCallable(() -> Optional.ofNullable(bytes))).defaultIfEmpty(Optional.empty())
                .flatMap(option -> {
                    final ClientResponse.Builder builder = ClientResponse.create(response.statusCode())
                        .headers(headers -> headers.addAll(response.headers().asHttpHeaders()))
						.cookies(cookies -> cookies.addAll(response.cookies()));
                    if (option.isPresent()) {
                        final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        return Mono.just(builder.body(Flux.just(dataBufferFactory.wrap(option.get()))).build());
                    }
                    return Mono.just(builder.build());
                }));
    }
}
