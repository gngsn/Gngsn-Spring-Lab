# Filter

<br><br>

필터는 통합 파이프라인의 중간에 위치할 수 있으며, 플로우의 전 단계로부터 다음 단계로의 메시지 전달을 허용 또는 불허한다.

<br>

``` java
public interface Filter {

    public default void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException;

    public default void destroy() {}
}

```
- `init` : init 메소드는 필터 객체를 초기화하고 서비스에 추가하기 위한 메소드이다. 웹 컨테이너가 1회 init 메소드를 호출하여 필터 객체를 초기화하면 이후의 요청들은 doFilter를 통해 처리된다.
- `doFilter` : doFilter 메소드는 url-pattern에 맞는 모든 HTTP 요청이 디스패처 서블릿으로 전달되기 전에 웹 컨테이너에 의해 실행되는 메소드이다. doFilter의 파라미터로는 FilterChain이 있는데, FilterChain의 doFilter 통해 다음 대상으로 요청을 전달하게 된다. chain.doFilter() 전/후에 우리가 필요한 처리 과정을 넣어줌으로써 원하는 처리를 진행할 수 있다.
- `destroy` : destroy 메소드는 필터 객체를 서비스에서 제거하고 사용하는 자원을 반환하기 위한 메소드이다. 이는 웹 컨테이너에 의해 1번 호출되며 이후에는 이제 doFilter에 의해 처리되지 않는다.

<br>

예를 들어, 정수 값을 갖는 메시지가 numberChannel이라는 이름의 채널로 입력되고, 짝수인 경우만 evenNumberChannel이라는 이름의 채널로 전달된다고 해보자. 이 경우 다음과 같이 `@Filter` 어노테이션이 지정된 필터를 선언할 수 있다.

<br><br>

``` java
@Filter(inputChannel="numberChannel",
        outputChannel="evenNumberChannel")
public boolean evenNumberFilter(Integer number) {
    return number % 2 == 0;
}
```

혹은 자바 DSL 구성을 사용해서 통합 플로우를 정의한다면 다음과 같이 `filter()`메서드를 호출할 수 있다.

<br>

``` java
@Bean
public IntegrationFlow evenNumberFilter(AtomicInteger integerSource) {
    return IntegrationFlows
        ...
        .<Integer>filter((p) -> p % 2 == 0)
        ...
        .get();
}
```

<br>

여기서는 람다를 사용해서 필터를 구현했지만, 실제로는 filter() 메서드가 GenericSelector를 인자로 받는다.
이것은 우리의 필요에 따라 GenericSelector를 구현하여 다양한 조건으로 필터링 할 수 있다는 것을 의미한다.
