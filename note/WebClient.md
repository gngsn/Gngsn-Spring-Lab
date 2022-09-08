## WebClient

WebCleint는 스프링 5.0에서 추가된 인터페이스

Spring WebFlux는 HTTP request를 수행하는 client인 WebClient 를 포함하고 있으며 반응형으로 동작하도록 설계되었다.

실제로는 spring-webflux 라이브러리에 속하지만 이 솔루션은 동기 및 비동기 작업을 모두 지원하므로 서블릿 스택에서 실행되는 애플리케이션에도 적용 가능하다.

WebClient는 다음과 같은 특징이 있다.

- 싱글 스레드 방식을 사용
- Non-Blocking 방식을 사용
- JSON, XML을 쉽게 응답받는다.

<br>

WebClient는 외부 API로 요청을 할 때 리액티브 타입의 전송과 수신을 한다.

WebClient는 요청을 나타내고 전송하게 해주는 빌더 방식의 인터페이스를 사용한다.

<br>

```
WebClient 인스턴스를 생성 (or WebClient 빈을 주입)
        ⬇
요청 HTTP 메서드 지정
        ⬇
요청 URI 및 헤더 지정
        ⬇
    요청을 제출
        ⬇
  응답 소비(사용)
```

<br><br>

### 의존성

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

<br><br>

WebClient는 두 가지 방식으로 생성할 수 있다.

<details>
<summary> create() </summary>

create() 메서드는 overload된 메서드로, 요청 URL을 받을 수 있다.

``` java 
WebClient webClient1 = WebClient.create();

WebClient webClient2 = WebClient.create("https://client-domain.com");
```

</details>
<details>
<summary> build() </summary>
build() 메서드는 DefaultWebClientBuilder 클래스에 의해 빌드되어 모든 설정을 customization할 수 있다.


#### Option과 함께 Build

- `uriBuilderFactory` : base url을 커스텀한 UriBuilderFactory
- `defaultHeader` : 모든 요청에 사용할 헤더
- `defaultCookie` : 모든 요청에 사용할 쿠키
- `defaultRequest` : 모든 요청을 커스텀할 Consumer
- `filter` : 모든 요청에 사용할 클라이언트 필터
- `exchangeStrategies` : HTTP 메시지 reader & writer 커스텀
- `clientConnector` : HTTP 클라이언트 라이브러리 세팅


``` java
WebClient client = WebClient.builder()
  .baseUrl("http://localhost:8080")
  .defaultCookie("cookieKey", "cookieValue")
  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
  .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
  .build();
```
</details>

<br><br>

### Configurations

<br>

WebClient는 한 번 빌드한 뒤부터 immutable 하다. 
만약 WebClient를 Singleton으로 사용하는데 default setting과 다르게 사용하고 싶다면 mutate()를 사용할 수 있다.

<br>

``` java
// mutate() 예시

WebClient client1 = WebClient.builder()
        .filter(filterA).filter(filterB).build();

WebClient client2 = client1.mutate()
        .filter(filterC).filter(filterD).build();

// client1 has filterA, filterB

// client2 has filterA, filterB, filterC, filterD
```

<br><br>

#### Configure the Timeouts

WebClient에서 아래와 같이 Timeout 설정을 지정할 수 있다.

<br>

``` java 
HttpClient httpClient = HttpClient.create()
  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
  .responseTimeout(Duration.ofMillis(5000))
  .doOnConnected(conn -> 
    conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
      .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

WebClient client = WebClient.builder()
  .clientConnector(new ReactorClientHttpConnector(httpClient))
  .build();
```

<br>

Connect Timeout 과 ReadTimeout, WriteTimeout을 모두 5000ms로 지정한 HttpClient 객체를 만들어 주입한다.

<br><br>

### Define the Headers




### Error Handling

4xx, 5xx의 응답 코드를 받으면 WebClientResponseException 또는 HTTP 상태에 해당하는 WebClientResponseException.BadRequest 등 과 같은 하위 exception을 던진다. onStatus 메서드로 상태별 exception을 커스텀도 가능하다.

``` java 
Mono<Person> result = client.get()
      .uri("/persons/{id}", id).accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError, response -> ...)
      .onStatus(HttpStatus::is5xxServerError, response -> ...)
      .bodyToMono(Person.class);
```


### Request
#### GET
#### POST
#### PUT
#### DELETE

### Retrieve
### Exchange

### Response

response를 받아오는 방법에는 두 가지가 있다.

- retrieve() → body를 받아 디코딩하는 간단한 메서드
- exchange() → ClientResponse를 상태값 그리고 헤더와 함께 가져온다

exchange()를 통해 세세한 컨트롤이 가능하지만, Response 컨텐츠에 대한 모든 처리를 직접 하면서 메모리 누수 가능성 때문에 retrieve()를 권고하고 있다.

bodyToMono 는 가져온 body를 Reactor의 Mono 객체로 바꿔준다. Mono 객체는 0-1개의 결과를 처리하는 객체이다. Flux는 0-N개의 결과를 처리하는 객체이다.

> block() 을 사용하면 RestTemplate 처럼 동기식으로 사용할 수 있다.
>

