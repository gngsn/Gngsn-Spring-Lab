# Spring Security - Filter Chain


Spring Security의 Servlet 지원은 Servlet 필터를 기반으로 하므로 일반적으로 필터의 역할을 살펴보는 것이 좋다. 

다음 그림은 단일 HTTP 요청에 대한 핸들러의 일반적인 계층화를 보여 준다.

<img src="./filterchain.png" alt="filterchain" width="40%" />

<br/><br/>

클라이언트가 응용 프로그램에 요청을 보내고 컨테이너는 요청 URI 경로를 기반으로 HttpServletRequest를 처리해야 하는 필터 및 Servlet을 포함하는 FilterChain을 생성합니다.

Spring MVC 애플리케이션에서 Servlet은 DispatcherServlet의 인스턴스입니다.

최대 하나의 Servlet에서 단일 HttpServletRequest 및 HttpServletResponse를 처리할 수 있습니다.

단, 아래의 경우에는 두 개 이상의 필터를 사용하여 다음을 수행할 수 있습니다.