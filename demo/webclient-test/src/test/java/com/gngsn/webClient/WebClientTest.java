package com.gngsn.webClient;

import com.gngsn.webClient.common.Constants;
import com.gngsn.webClient.util.SpringBeanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WebclientTestApplicationTests {

	@Mock
	private ExchangeFunction exchangeFunction;

	@Captor
	private ArgumentCaptor<ClientRequest> captor;

	private WebClientPlugin webClientPlugin;

	@BeforeEach
	public void setup() {
		ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
		SpringBeanUtils.getInstance().setApplicationContext(context);
		when(context.getBean(ShenyuResult.class)).thenReturn(mock(ShenyuResult.class));

		WebClient webClient = mockWebClientOK();
		webClientPlugin = new WebClientPlugin(webClient);
	}

	/**
	 * test case for WebClientPlugin {@link WebClientPlugin#execute(ServerWebExchange, ShenyuPluginChain)}.
	 */
	@Test
	public void testExecuted() {
		final ShenyuPluginChain chainNoPathTest = mock(ShenyuPluginChain.class);
		final WebClient webClientNoPathTest = mockWebClientOK();
		ServerWebExchange exchangeNoPathTest = MockServerWebExchange
			.from(MockServerHttpRequest.get("/test").build());
		exchangeNoPathTest.getAttributes().put(Constants.CONTEXT, mock(ShenyuContext.class));
		WebClientPlugin webClientPluginNoPathTest = new WebClientPlugin(webClientNoPathTest);
		Mono<Void> monoNoPathTest = webClientPluginNoPathTest.execute(exchangeNoPathTest, chainNoPathTest);
		StepVerifier.create(monoNoPathTest).expectSubscription().verifyComplete();

		final ShenyuPluginChain chainPostTest = mock(ShenyuPluginChain.class);
		final WebClient webClientPostTest = mockWebClientOK();
		ServerWebExchange exchangePostTest = MockServerWebExchange
			.from(MockServerHttpRequest.post("/test123?param=1").build());
		exchangePostTest.getAttributes().put(Constants.CONTEXT, mock(ShenyuContext.class));
		exchangePostTest.getAttributes().put(Constants.HTTP_URI, URI.create("/test123?param=1"));
		WebClientPlugin webClientPluginPostTest = new WebClientPlugin(webClientPostTest);
		Mono<Void> monoPostTest = webClientPluginPostTest.execute(exchangePostTest, chainPostTest);
		StepVerifier.create(monoPostTest).expectSubscription().verifyError();

		final ShenyuPluginChain chainOkTest = mock(ShenyuPluginChain.class);
		final WebClient webClientOkTest = mockWebClientOK();
		WebClientPlugin webClientPluginOkTest = new WebClientPlugin(webClientOkTest);
		Mono<Void> monoOkTest = webClientPluginOkTest.execute(generateServerWebExchange(), chainOkTest);
		StepVerifier.create(monoOkTest).expectSubscription().verifyError();

		final ShenyuPluginChain chainErrorTest = mock(ShenyuPluginChain.class);
		final WebClient webClientErrorTest = mockWebClientError();
		WebClientPlugin webClientPluginErrorTest = new WebClientPlugin(webClientErrorTest);
		Mono<Void> monoErrorTest = webClientPluginErrorTest.execute(generateServerWebExchange(), chainErrorTest);
		StepVerifier.create(monoErrorTest).expectSubscription().verifyError();
	}

	/**
	 * test case for WebClientPlugin {@link WebClientPlugin#skip(ServerWebExchange)}.
	 */
	@Test
	public void testSkip() {
		ServerWebExchange exchangeNormal = generateServerWebExchange();
		assertTrue(webClientPlugin.skip(exchangeNormal));

		ServerWebExchange exchangeHttp = generateServerWebExchange();
		when(((ShenyuContext) exchangeHttp.getAttributes().get(Constants.CONTEXT)).getRpcType())
			.thenReturn(RpcTypeEnum.HTTP.getName());
		assertFalse(webClientPlugin.skip(exchangeHttp));

		ServerWebExchange exchangeSpringCloud = generateServerWebExchange();
		when(((ShenyuContext) exchangeSpringCloud.getAttributes().get(Constants.CONTEXT)).getRpcType())
			.thenReturn(RpcTypeEnum.SPRING_CLOUD.getName());
		assertFalse(webClientPlugin.skip(exchangeSpringCloud));
	}

	/**
	 * test case for WebClientPlugin {@link WebClientPlugin#getOrder()}.
	 */
	@Test
	public void testGetOrder() {
		assertEquals(PluginEnum.WEB_CLIENT.getCode(), webClientPlugin.getOrder());
	}

	/**
	 * test case for WebClientPlugin {@link WebClientPlugin#named()}.
	 */
	@Test
	public void testNamed() {
		assertEquals(PluginEnum.WEB_CLIENT.getName(), webClientPlugin.named());
	}

	private ServerWebExchange generateServerWebExchange() {
		ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
		exchange.getAttributes().put(Constants.CONTEXT, mock(ShenyuContext.class));
		exchange.getAttributes().put(Constants.HTTP_URI, URI.create("/test"));
		return exchange;
	}

	private WebClient mockWebClientOK() {
		final ClientResponse.Headers headers = mock(ClientResponse.Headers.class);
		when(headers.asHttpHeaders()).thenReturn(new HttpHeaders());

		final ClientResponse mockResponse = mock(ClientResponse.class);
		when(mockResponse.statusCode()).thenReturn(HttpStatus.OK);
		when(mockResponse.headers()).thenReturn(headers);
		when(mockResponse.bodyToMono(byte[].class)).thenReturn(Mono.just("{\"test\":\"ok\"}".getBytes()));
		when(mockResponse.releaseBody()).thenReturn(Mono.empty());
		given(this.exchangeFunction.exchange(this.captor.capture())).willReturn(Mono.just(mockResponse));
		return WebClient.builder().baseUrl("/test")
			.exchangeFunction(this.exchangeFunction)
			.apply(consumer -> consumer.defaultHeader("Accept", "application/json")
				.defaultCookie("id", "test"))
			.build();
	}

	private WebClient mockWebClientError() {
		final ClientResponse.Headers headers = mock(ClientResponse.Headers.class);
		when(headers.asHttpHeaders()).thenReturn(new HttpHeaders());

		final ClientResponse mockResponse = mock(ClientResponse.class);
		when(mockResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
		when(mockResponse.headers()).thenReturn(headers);
		when(mockResponse.bodyToMono(byte[].class)).thenReturn(Mono.just(new byte[0]));
		when(mockResponse.releaseBody()).thenReturn(Mono.empty());
		given(this.exchangeFunction.exchange(this.captor.capture())).willReturn(Mono.just(mockResponse));
		return WebClient.builder().baseUrl("/test")
			.exchangeFunction(this.exchangeFunction)
			.apply(consumer -> consumer.defaultHeader("Accept", "application/json")
				.defaultCookie("id", "test"))
			.build();
	}
}