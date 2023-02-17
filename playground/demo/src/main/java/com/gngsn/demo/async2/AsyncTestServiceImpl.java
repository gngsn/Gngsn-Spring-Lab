package com.gngsn.demo.async2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncTestServiceImpl implements AsyncTestService {

	private final WebClient webClient;
	private final RestTemplate restTemplate = new RestTemplate(); //레거시 방식

	private String testUrl = "http://localhost:81/test/delay/callable/%s";

	private static HttpHeaders textUtf8Headers = new HttpHeaders(); //한글 메시지를 응답하기 위해서 text utf8

	static {
		textUtf8Headers.set("Content-Type", "text/plain;charset=UTF-8");
	}

	@PostConstruct
	private void init() {

		//레거시 restTemplate의 커넥션풀 등 설정
		HttpComponentsClientHttpRequestFactory crf = new HttpComponentsClientHttpRequestFactory();

		crf.setReadTimeout(1000 * 10);
		crf.setConnectTimeout(1000 * 10);

		HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(50) //커넥션풀적용(최대 오픈되는 커넥션 수)
			.setMaxConnPerRoute(30) //커넥션풀적용(IP:포트 1쌍에 대해 수행 할 연결 수제한)
			.evictIdleConnections(30L, TimeUnit.SECONDS) //서버에서 keepalive 시간동안 미 사용한 커넥션을 죽이는 등의 케이스 방어로 idle 커넥션을 주기적으로 지움
			.build();
		crf.setHttpClient(httpClient);

		restTemplate.setRequestFactory(crf);

	}

	@Override
	public ResponseEntity<Object> excuteLongWorkProcess(int delaySecond) {

		log.info("메소드 호출됨: excuteLongWorkProcess");
		try {
			Thread.sleep(delaySecond * 1000);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}

		return new ResponseEntity<>("delay after(Async callback test) : " + delaySecond + " | " + LocalDateTime.now(), HttpStatus.OK);
	}

	/**
	 * 외부 API요청: 논블럭 방식(NIO)
	 */
	@Override
	public void externalReqByNio() {
		log.info("== Start: 메소드 호출됨: excuteNIOHtppRequestTest. :{}", Thread.currentThread().getName());

		String uri = String.format(testUrl, 2) + "?reqId=" + Thread.currentThread().getName();
		webClient.get().uri(uri).retrieve().bodyToMono(String.class).subscribe(result -> {
			log.info("externalReqByNio result : {} | {}", result, Thread.currentThread().getName());
		});

		log.info("== End: 메소드 호출됨: excuteNIOHtppRequestTest. :{}\n", Thread.currentThread().getName());
	}

	/**
	 * 외부 API요청: 논블럭 방식(NIO) & 리턴처리
	 *
	 * @param dr
	 * @param startMillis
	 */
	@Override
	public void externalReqByNioVer2(DeferredResult<Object> dr, long startMillis) {

		final String reqId = String.valueOf(System.currentTimeMillis());

		log.info("== Start: 비동기용 메소드 호출됨: externalReqByNioVer2. reqId: {}", reqId);

		//참고: webClient가 최초 대상 API 호출시에는 http 핸드쉐이크 등의 작업때문에 소요시간이 더 걸릴 수 있음

		final String uri = String.format(testUrl, 2) + "?reqId=" + reqId;
		webClient.get().uri(uri).retrieve().bodyToMono(String.class).subscribe(result -> {

			log.info("테스트대상 API {} 요청 후 리턴됨 ==> {}", uri, result);

			long endMillis = System.currentTimeMillis();
			long duration = endMillis - startMillis;

			StringBuffer sb = new StringBuffer();
			sb.append(String.format("'%s' ", uri));
			sb.append("API를 비동기/논블럭킹으로 실행결과\n\n");
			sb.append("=========   요청 정보   =========\n");
			sb.append(String.format("요청ID: %s\n", reqId));

			sb.append("\n\n=========   결과   =========\n");
			sb.append("소요시간: " + duration + "(Millis)\n");
			sb.append("  - Srping 컨트롤러부터 API호출 후 처리까지 소요시간\n");
			sb.append("API응답결과\n");
			sb.append("  ");
			sb.append(result);

			//            final String resultMsg = String.format("'%s' 를 비동기/논블럭킹으로 실행.\n\nreqId:%s | method duration(Millis): %s | threadName: %s\n\n\nAPI 응답결과\n=============\n%s",
			//                uri, reqId, duration, Thread.currentThread().getName(), result);

			ResponseEntity<String> rtnData = new ResponseEntity<>(sb.toString(), textUtf8Headers, HttpStatus.OK);
			log.info("== End111:  비동기 방식으로 webClient 콜백 후 컨트롤러에 다음 결과를 리턴함.\n\trtnData: {}\n", rtnData);

			dr.setResult(rtnData); //결과값을 DeferredResult에 담아서 반환
			//TODO-FIXME: 실무에서는 타임아웃 등 옵션값 설정해서 사용

		});

		//주의: 하단에는 로직/로깅 등 어떤 작업도 안하는게 좋음
		//log.info("== End222: 비동기용 메소드 호출됨: externalReqByNioVer2. reqId:{}\n", reqId);

	}

	/**
	 * 외부 API요청: IO블럭킹 방식
	 */
	@Override
	public ResponseEntity<String> externalReqByBlock() {
		log.info("==Start: BLOCK방식 메소드 호출됨: externalReqByBlock. :{}", Thread.currentThread().getName());

		String uri = String.format(testUrl, 2) + "?reqId=" + Thread.currentThread().getName();

		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
		// String result = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
		log.info("externalReqByBlock result : {} | {}", result, Thread.currentThread().getName());

		log.info("==End: BLOCK방식 메소드 호출됨: externalReqByBlock. :{}\n", Thread.currentThread().getName());

		return result;
	}
}