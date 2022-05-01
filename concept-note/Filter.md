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


<br/>

# Filter

### CharacterEncodingFilter

**스프링 인코딩 처리**

스프링은 웹 요청과 응답에 대한 인코딩 처리를 위해 **CharacterEncodingFilter**를 제공합니다.

인코딩 필터의 경우 모든 프로젝트에서 사용가는 공통적인 기능이므로 스프링프레임워크 측에서 번거로움을 피하기 위해 제공하는것 같습니다.

CharacterEncodingFilter 클래스는 Servlet 표준 스펙인 javax.servlet.Filter 인터페이스를 구현한 클래스이기 때문에 기존의 Servlet, JSP 에서 사용하던 필터와 똑같이 사용 가능합니다.

forceEncoding의 값이 true 이면 encoding의 값을 HttpServletRequest, HttpServletResponse 객체에 강제로 세팅해 준다.

forceEncoding의 값이 false 이면 HttpServletRequest, HttpServletResponse 객체의 값이 null 인 경우에만 encoding의 값으로 세팅해 준다.

<br/>


```java
public class CharacterEncodingFilter extends OncePerRequestFilter {

	@Nullable
	private String encoding;

	private boolean forceRequestEncoding = false;

	private boolean forceResponseEncoding = false;

	// ...
	
	@Override
		protected void doFilterInternal(
				HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
	
			String encoding = getEncoding();
			if (encoding != null) {
				if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
					request.setCharacterEncoding(encoding);
				}
				if (isForceResponseEncoding()) {
					response.setCharacterEncoding(encoding);
				}
			}
			filterChain.doFilter(request, response);
		}
}
```

