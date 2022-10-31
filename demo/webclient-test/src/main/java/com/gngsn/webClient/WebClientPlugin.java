package com.gngsn.webClient;

import com.gngsn.webClient.common.Constants;
import com.gngsn.webClient.common.ResultEnum;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;


/**
 * The type Web client plugin.
 */
public class WebClientPlugin extends AbstractHttpClientPlugin<ClientResponse> {

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
	protected Mono<ClientResponse> doRequest(final ServerWebExchange exchange, final String httpMethod, final URI uri,
		final HttpHeaders httpHeaders, final Flux<DataBuffer> body) {
		// springWebflux5.3 mark #exchange() deprecated. because #echange maybe make memory leak.
		// https://github.com/spring-projects/spring-framework/issues/25751
		// exchange is deprecated, so change to {@link WebClient.RequestHeadersSpec#exchangeToMono(Function)}
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
						final DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
						return Mono.just(builder.body(Flux.just(dataBufferFactory.wrap(option.get()))).build());
					}
					return Mono.just(builder.build());
				}))
			.doOnSuccess(res -> {
				if (res.statusCode().is2xxSuccessful()) {
					exchange.getAttributes().put(Constants.CLIENT_RESPONSE_RESULT_TYPE, ResultEnum.SUCCESS.getName());
				} else {
					exchange.getAttributes().put(Constants.CLIENT_RESPONSE_RESULT_TYPE, ResultEnum.ERROR.getName());
				}
				exchange.getResponse().setStatusCode(res.statusCode());
				exchange.getAttributes().put(Constants.CLIENT_RESPONSE_ATTR, res);
			});
	}
}
