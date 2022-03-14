# Interceptor

*[코드로 배우는 스프링 웹 프로젝트]*

Interceptor: 컨트롤러에 들어오는 요청 HttpRequest와 컨트롤러가 응답하는 HttpResponse를 가로채는 역할

<br>

## PART 6. 인터셉터(Interceptor)를 활용하는 로그인 처리

### Spring MVC의 인터셉터(Intercaptor)

스프링 MVC 에서 인터셉터는 웹 애플리케이션 내에서 특정한 URI 호출을 말 그대로 '가로채는' 역할을 한다. 
인터셉터를 활용하면 로그인한 사용자만이 사용할 수 있는 기능을 제어할 수 있기 때문에 기존 컨트롤러의 롲기을 수정하지 않고도, 사전이나 사후에 제어가 가능하다.

<br>

### Filter와 Interceptor의 공통점과 차이점
Servlet 기술의 Filter와 Spring MVC의 HandlerInterceptor는 특정 URI에 접근할 때 제어하는 용도로 사용된다는 공통점을 가지고 있다. 조금 뒤에서 보겠지만, 설정 역시 web.xml에 사용하는 필터의 설정과 상당히 유사한 부분이 많다.
<br>
두 기능의 가장 큰 차이는 실행 시점에 속하는 영역(Context)에 있습니다. Filter는 동일한 웹 애플리케이션의 영역 내에서 필요한 자원들을 활용한다.

<br>

Interceptor의 경우 스프링에서 관리되기 때문에 스프링 내의 모든 객체(빈)에 접근이 가능하다는 차이가 있다.

<br>

<img width="30%" src="https://supawer0728.github.io/images/spring-filter-interceptor/spring-request-lifecycle.jpg" alt="Spring MVC LifeCycle" />

<br>

Filter의 경우 웹 애플리케이션 내에서 동작하므로, 스프링의 Context를 접근하기 어렵다.
Interceptor의 경우 Spring의 Context내에서 존재하므로 Context 내의 모든 객체를 활용할 수 있다.

HandlerInterceptor의 경우 스프링의 빈으로 등록된 컨트롤러나 서비스 객체들을 주입받아서 사용할 수 있기 때문에 기존의 구조를 그대로 활용하면서 추가적인 작업이 가능하다.

<br><br>

### Spring AOP 기능과 HandlerInterceptor의 차이

특정 객체 동작의 사전 혹은 사전 처리는 이미 AOP 기능을 통해서 확인해 보았지만, 일반적인 경우라면 컨트롤러를 이요할 때는 AOP의 BeforeAdvice 등을 활용하기 보다는 HandlerInterceptor인터페이스 혹은 HandlerInterceptorAdaptor 클래스를 활용하는 경우가 더 많다.

<br>

AOP의 Advice와 HandlerInterceptor의 가장 큰 차이는 파라미터의 차이라고 할 수 있다. Advice의 경우 JoinPoint나 ProceedingJoinPoint 등을 활용해서 호출 대상이 되는 메소드의 파라미터 등을 처리하는 방식이다.
반면, HandlerInterceptor는 Filter와 유사하게 HttpServletRequest, HttpServletResponse를 파라미터로 받는 구조이다.

<br>

일반적인 경우라면 Controller에서는 DTO나 VO 타입을 주로 파라미터로 활용하고, Servlet API에 해당하는 HttpServletRequest, HttpServletResponse를 활용하는 경우는 그다지 많지 않다.

HandlerInterceptor는 기존의 컨트롤러에서는 순수하게 필요한 파라미터와 결과 데이터를 만들어 내고, 인터셉터를 이용해서 웹과 관련된 처리를 도와주는 역할을 한다.

HandlerInterceptor에는 아래의 메소드가 존재한다.

<br>

``` java
public interface HandlerInterceptor {

	default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}
    
	default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}
}

```

<br>

- preHandle(request, response, handler) : 지정된 컨트롤러의 동작 이전에 가로채는 역할로 사용
- postHandle(request, response, handler, modelAndView) : 지정된 컨트롤러의 동작 이후에 처리, Spring MVC의 Front Controller인 DispatcherServlet이 화면을 처리하기 전에 동작
- afterCompletion(request, response, handler, exception) : DispatcherSerlvet의 화면 처리가 완료된 상태에서 처리

<br><br>

### HandlerInterceptorAdapter

HandlerInterceptor는 인터페이스로 정의되어 있지만, HandlerInterceptorAdaptor는 인터페이스를 구현한 추상 클래스로 설계되어 있다. 일반적으로 디자인 패턴엣 Adapter라는 용어가 붙으면 특정 인터페이스를 미리 구현해서 사용하기 쉽게 하는 용도인 경우가 많다. HandlerInterceptorAdaptor 역시 HandlerInterceptor를 쉽게 사용하기 위해 인터페이스의 메소드를 미리 구현한 클래스다.

<br><br>

### preHandle() 의 Object Parameter

preHandle()의 경우 세 개의 파라미터를 사용하는데, `HttpServletRequest`, `HttpServletResponse`, `Object handler`로 구성된다.
마지막의 Handler는 현재 실행하려는 메소드 자체를 의미하는데, 이를 활용하면 **현재 실행되는 컨트롤러를 파악**하거니, **추가적인 메소드를 싱행하는 등의 작업이 가능**하다.

