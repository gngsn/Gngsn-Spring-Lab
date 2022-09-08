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

<img width="30%" src="./img/spring-request-lifecycle.jpeg" alt="Spring MVC LifeCycle" />

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

- `preHandle(request, response, handler)` : 지정된 컨트롤러의 동작 이전에 가로채는 역할로 사용
- `postHandle(request, response, handler, modelAndView)` : 지정된 컨트롤러의 동작 이후에 처리, Spring MVC의 Front Controller인 DispatcherServlet이 화면을 처리하기 전에 동작
- `afterCompletion(request, response, handler, exception)` : DispatcherSerlvet의 화면 처리(뷰)가 완료된 상태에서 처리

<br>

각 메서드의 반환값이 true이면 핸들러의 다음 체인이 실행되지만 false이면 중단하고 남은 인터셉터와 컨트롤러가 실행되지 않는다

<br><br>

### HandlerInterceptorAdapter

HandlerInterceptor는 인터페이스로 정의되어 있지만, HandlerInterceptorAdaptor는 인터페이스를 구현한 추상 클래스로 설계되어 있다. 일반적으로 디자인 패턴엣 Adapter라는 용어가 붙으면 특정 인터페이스를 미리 구현해서 사용하기 쉽게 하는 용도인 경우가 많다. HandlerInterceptorAdaptor 역시 HandlerInterceptor를 쉽게 사용하기 위해 인터페이스의 메소드를 미리 구현한 클래스다.

<br><br>

### preHandle() 의 Object Parameter

preHandle()의 경우 세 개의 파라미터를 사용하는데, `HttpServletRequest`, `HttpServletResponse`, `Object handler`로 구성된다.
마지막의 Handler는 현재 실행하려는 메소드 자체를 의미하는데, 이를 활용하면 **현재 실행되는 컨트롤러를 파악**하거니, **추가적인 메소드를 싱행하는 등의 작업이 가능**하다.

<br>

**현재 실행되는 컨트롤러와 메소드의 정보를 파악하는 예제**

``` java
@Override
public boolean preHandle(HttpServletRequest request, 
	HttpServletResponse response, Object handler) throws Exception {

	System.out.println("================ preHandle ================");
	
	HandlerMethod handlerMethod = (HandlerMethod) handler;
	Method method = handlerMethod.getMethod();

	System.out.println("Bean: " + handlerMethod.getBean());
	System.out.println("Method: " + method);
		
	return true;
```
handler를 HandlerMethod 타입으로 캐스팅한 후 원래의 메소드와 객체(빈)를 확인할 수 있다.


<br><br>

### postHandle() 으로 추가적인 작업

컨트롤러 메소드의 실행이 끝나고, 아직 화면처리는 안 된 상태임으로, 필요하다면 메솓의 실행 이후에 추가적인 작업이 가능하다.

예를 들어, 특정한 메소드의 실행 결과를 HttpSession 객체에 같이 담아야 하는 경우를 생각해 볼 수 있다. 컨트롤러에서는 Model 객체에 결과 뎅터를 저장하고, 인터셉터으  PostHandle()에서 이를 이용해 HttpSession에 결과를 담는다면, 컨트롤러에서 HttpSession을 처리할 필요가 없게 됩니다.

<br>

**result라는 변수가 저장되었다면 HttpSession객체에 이를 보관하는 예제**

``` java
@Override
public void postHandle(HttpServletRequest request,
	HttpServletResponse response, Object handler,
	ModelAndView modelAndView) throws Exception {

	System.out.println("================ postHandle ================");
		
	Object result = modelAndView.getModel().get("result");
		
	if (result != null) {
		request.getSession().setAttribute("result", result);
		response.sendRedirect("/home");
	}
}
```

컨트롤러에서 'result'라는 이름으로 저장된 데이터를 저장하는 요청 `/doSomething`이 있다고 해보자.
이 요청을 보내면 `postHandle()`에서 'result'라는 변수가 `ModelAndView`에 존재하면 이를 추출해서 `HttpSession`에 추가한다.
`/doSomething` 을 호출하면 `HttpSession`에 'result'라는 이름으로 보관한 후 `/home` 로 redirect를 수행한다.

<br>

``` java
// HomeController
@Controller
@RequestMapping("/home")
public class HomeController {
	@RequestMapping("/")
	public String home(Model model) {
		return "home";
	}
}
```
``` jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h2>${result}</h2>
</body>
</html>
```

<br>

/home이 HomeController를 통해 'home'이라는 문자열을 반환하여 home.jsp를 보여준다고 했을 때,
home.jsp에서는 `${result}` 라는 데이터를 사용할 수 있다.

이렇게 되면 Controller상에서 home.jsp에는 전달되는 객체가 없지만, HttpSession 객체에 필요한 정보가 보관되어 있기 때문에 데이터가 보여지는 것이다.

<br><br>

위의 예제는 로그인 처리에서 유용하게 사용할 수 있다.
컨트롤러에서는 로그인 처리 후의 결과를 반환하고, 인터셉터를 이용해서 HttpSession에 로그인이 필요한 객체를 보관하는 형태로 작성하면 컨트롤러에서 직접 HttpSession 등의 API를 사용하지 않는 코드를 만들 수 있다.

<br><br>

**유저의 Login 상태를 관리하는 AuthInterceptor 예제**

``` java
public class AuthInterceptor extends HandlerInterceptorAdapter {
	private final String LOGIN = "login";
	private final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();

		if (session.getAttribute(LOGIN) == null) {
			logger.info("current user is not logined");
			saveDest(request);
			response.sendRedirect("/user/login");
			return false;
		}
		return true;
	}

	private void saveDest(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String query = req.getQueryString();

		if (query == null || query.equals("null")) {
			query = "";
		} else {
			query = "?" + query;
		}

		if (req.getMethod().equals("GET")) {
			logger.info("dest: "+ (uri + query));
			req.getSession().setAttribute("dest", uri + query);
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		HttpSession session = request.getSession();
		ModelMap modelMap = modelAndView.getModelMap();
		Object userVO = modelMap.get("userVO");

		if (userVO != null) {
			logger.info("new login success");
			session.setAttribute(LOGIN, userVO);
		}
		Object dest = session.getAttribute("dest");
		response.sendRedirect(dest != null ? (String) dest : "/");
	}
}
```
