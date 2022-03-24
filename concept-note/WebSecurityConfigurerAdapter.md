# WebSecurityConfigurerAdapter
스프링 시큐리티의 웹 보안 기능 초기화 및 설정


> @Order(100)
> public abstract class WebSecurityConfigurerAdapter 
> implements WebSecurityConfigurer<WebSecurity> {


Spring Security를 사용하면서 보안 구성을 원하는 대로 조작해서 적용하길 원할 때,

WebSecurityConfigurerAdapter을 상속받아 구현할 수 있다.



즉, Spring Security에서 각종 인증/인가와 관련된 설정 관리를 할 수 있는 기능을 제공한다.



해당 추상 클래스를 상속받아 사용하게 되면,

기본 자동 구성이 비활성화되고 사용자 지정 보안 구성을 활성화 시킬 수 있다.



@EnableWebSecurity 어노테이션과 WebSecurityConfigurerAdapter는 웹 기반 보안을 제공하기 위해 같이 동작한다.

같이 동작을 하기 때문에 선언도 같이 해주어야 하는데요.

예시를 통해 확인해보면, 보통 아래와 같이 정의하여 관리한다.



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// ...		
}



WebSecurityConfigurerAdapter가 갖고 있는 WebSecurity와 HttpSecurity 객체를 다루는 config method를 통해 사용자가 원하는대로 맞춤 변경할 수 있도록 한다.



WebSecurityConfigurerAdapter는 Configure(WebSecurity), Configure(HttpSecurity) 메소드를 정의해두었다.

이 메소드들을 Override해서 각각의 객체를 인자로 받아 설정 값들을 설정한다.



그럼 WebSecurity와 HttpSecurity를 어떻게 다루는지 자세히 확인해보며

인증/인가 관련 설정 방법에 대해 알아보겠다.





HttpSecurity
세부적인 보안 기능을 설정할 수 있는 API 제공



protected void configure(HttpSecurity http) throws Exception {
	this.logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
	http.authorizeRequests((requests) -> {
		((AuthorizedUrl)requests.anyRequest()).authenticated();
	});
	http.formLogin();
	http.httpBasic();
}


추상 클래스인 WebSecurityConfigurerAdapter는 HttpSecurity객체를 인자로 받는

configure method를 위와 같이 정의해 두었다.



만약, 따로 오버라이딩을 하지 않는다면 기본적으로 정의되어 있는 위의 메소드가 실행된다.



먼저, HttpSecurity를 사용해서 구성할 수 있는 것은 아래와 같다.



✔️ 리소스(URL) 접근 권한 설정

✔️ 커스텀 로그인 페이지 지원

✔️ 인증 후 성공/실패 핸들링

✔️ 사용자 로그아웃

✔️ CSRF 공격으로 부터 보호



위와 같이 다양한 내용들을 지원한다.

그럼, 인증과 인가 설정 방법을 순서대로 알아보겠다.





Authentication
어플리케이션을 사용하기 위해서는 사용하는 그 사람이 누구인지, 그 사람이 맞는지를 확인해야한다.



Spring Security는 기본적으로 제공하는 로그인 페이지가 있지만

대부분의 서비스들은 자체 로그인 페이지를 갖는다.



뿐만 아니라, 서비스에 맞는 인증 설정들을 따로 적용시켜주어야 한다.

가령, 로그인 성공/실패 시의 액션들을 핸들링하는 것과 같다.



먼저, 인증 설정으로 적용되는 예시를 보며 하나씩 살펴보겠다.



protected void configure(HttpSecurity http) throws Exception {
  http.formLogin()
    .loginPage("/login.html")				// 사용자 정의 로그인 페이지
    .defaultSuccessUrl("/home")				// 로그인 성공 후 이동 페이지
    .failureUrl("/login.html?error=true")		// 로그인 실패 후 이동 페이지
    .usernameParameter("username")			// 아이디 파라미터명 설정
    .passwordParameter("password")			// 패스워드 파라미터명 설정
    .loginProcessingUrl("/login")			// 로그인 Form Action Url
    .successHandler(loginSuccessHandler())		// 로그인 성공 후 핸들러
    .failureHandler(loginFailureHandler())		// 로그인 실패 후 핸들러
}


위의 주석을 따라 살펴보면 어렵지 않게 이해하실 수 있을텐데요.

원하는 내용만 찾아서 적용하면 된다.



위 말고도 다양한 기능을 지원하는데요.

인증과 관련된 대표적인 설정들을 간단히 표시하면 아래와 같다.



http.formLogin()
http.logout()
http.csrf()
http.httpBasic()
http.SessionManagement()
http.RememberMe()
http.ExceptionHandling()
http.addFilter()


위의 키워드를 확인하며, 필요한 것들을 살펴보고 적용하시면 되겠다.





## Authorization
이번엔 인가와 관련된 설정에 대해 알아보겠다.

유저의 권한으로 접근할 수 있는 URI와 접근할 수 없는 URI를 구별할 수 있어야겠죠.

이를 위해 각각 ROLE 마다 접근할 수 있는 경로 패턴을 설정할 수 있다.



위와 마찬가지로, 적용되는 예시를 보며 하나씩 살펴보겠다.



@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
    	.antMatchers("/design/", "/orders")
        .hasRole("ROLE_USER")                        // ①
        .antMatchers("/", "/**").permitAll()         // ②
}


위의 설정을 설명하자면, 아래와 같다.

① : /design, /orders 의 요청은 ROLE_USER 의 권한을 갖는 사용자에게만 허용

② : 이외의 모든 요청은 모든 사용자에게 허용



#### 주의사항!

⚠️ 설정 시 구체적인 경로가 먼저 오고 그것 보다 큰 범위의 경로가 뒤에 오도록 해야 한다.



antMatcher()에서 지정된 경로의 패턴 일치를 검사하며, 먼저 지정된 보안 규칙이 우선적으로 처리된다.

따라서 만일 앞 코드에서 두 개의 antMatcher() 순서를 바꾸면,

모든 요청의 사용자에게 permitAll() 이 적용되므로 /design 과 /oders 의 요청은 효력이 없어지게 된다.



인가 관련 메소드는 아래와 같다.


✔️ authenticated() : 인증된 사용자의 접근을 허용

✔️ fullyAuthenticated() : 인증된 사용자의 접근을 허용, rememberMe 인증 제외

✔️ permitAll() :  무조건 접근 허용

✔️ denyAll() :  무조건 접근을 허용하지 않음

✔️ anonymous() : 익명사용자의 접근을 허용

✔️ rememberMe() :  기억하기를 통해 인증된 사용자의 접근을 허용

✔️ access(String) : 주어진 SpEL 표현식의 평가 결과가 true이면 접근을 허용

✔️ hasRole(String) : 사용자가 주어진 역할이 있다면 접근을 허용 

✔️ hasAuthority(String) :  사용자가 주어진 권한이 있다면

✔️ hasAnyRole(String...) : 사용자가 주어진 권한이 있다면 접근을 허용

✔️ hasAnyAuthority(String...) : 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용

✔️ hasIpAddress(String) : 주어진 IP로부터 요청이 왔다면 접근을 허용



## WebSecurity
특정 요청들을 무시하고 싶을때 사용



public void configure(WebSecurity web) throws Exception {}


WebSecurity를 가지는 Configure 메소드는 특정 요청들을 무시하고 싶을때 사용한다.

js, css, image 파일 등 보안 필터를 적용할 필요가 없는 리소스를 설정할 수 있다.



HttpSecurity에서 주된 설정을 적용하는데,

종종 이 WebSecurity와의 구분을 헷갈려하시는 분들이 있기 때문에 잘 구별해서 사용할 필요가 있다.

어떻게 사용하는지 예시를 통해 확인해보겠다.



@Override 
public void configure(WebSecurity web) throws Exception {
  web.ignoring()
     .mvcMatchers("/node_modules/**")
     .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); 
}


위와 같이 보안에 적용될 필요가 없는 경로를 설정해서 불필요한 필터링을 없앨 수 있다.


