## 초기화

1. SecurityConfig 설정 클래스 제작

그 안에서 여러개의 API들을 정의. 각각 요청을 받아 처리하게 구성

그 API와 구성대로 필터들을 생성한다. 실제로 필터들이 요청을 처리ㅣ하는 역할을 하기 때문에 필터를 생성

필터를 생성하는 클래스가 바로 HttpSecurity.

두 개의 설정 클래스를 만들면 초기화 과정 때 설정 구성대로 필터가 생성됨

필터들은 다시 WebSecurity 클래스에게 전달이 됨

각각 설정 클래스로 부터 필터 목록들을 전달받고, WebSecurity는 다시 FilterChainProxy전달하는데 생성자로 전달함

FilterChainProxy는 각각의 설정 클래스 별(SecurityConfig1, SecurityConfig2)로 필터 목록들을 갖고 있는 것.

그리고 DelegatingFilterProxy는 **서블릿 필터**인데, 초기화될 때 이미 FilterChainProxy가 빈으로 등록이 되어있다.

springSecurityFilterChain이라는 이름을 가진 Bean을 찾게 되는데, 그 Bean이 바로 FilterChainProxy이다.

그래서 DelegatingFilterProxy가 요청을 했을 때, 요청을 받아서 특정 빈에게 위임하게 되는데 

그 빈이 바로 springSecurityFilterChain, 즉 FilterChainProxy이다.

</br></br>

## Filter

- 인증 과정과 인증 후 자원에 접근하는 경우를 구분해서 알아두자

</br>

가장 먼저 사용자가 인증을 요청하게 되면 DelegatingFilterProxy가 가장 먼저 그 요청을 받고, FilterChainProxy에게 요청을 위임한다.

(이 때, FilterChainProxy는 이미 초기화 때 config에서 설정한 값을 기반으로 Filter들을 가지고 있다)

요청을 갖고 있는 각각의 Filter들에게 각 순서대로 요청을 맡긴다.

각 필터들이 체인으로 연결되어 수행→ 넘김→ 수행→넘김으로 진행된다.

</br>

### SecurityContextPersistenceFilter

*SecurityContextRepository에서 SecurityContext를 가져와 유저 Authentication에 접근 할 수 있게 한다.*

내부적으로 HttpSessionSecurityContextRepository 클래스를 가진다.

이 클래스가 SecurityContext 객체를 생성하고, 세션에 저장한다. 세션에 저장된 SecurityContext를 조회하고 참조하는 클래스이다.

인증 시도한 사용자가 loadContext해서 이전에 세션에 저장한 이력이 있는지 확인

처음 인증을 한다고 했을 때 혹은 익명 사용자일 경우, 세션에 저장된 것이 없을 테니 Create SecurityContext를 실행함

SecurityContextHolder안에 저장을 하는 역할을 해당 필터가 수행

</br>

### LogoutFilter

로그아웃 요청을 처리한다.

로그아웃 요청 시에만 실행 (건너 뜀)

</br>

### UsernamePasswordAuthenticationFilter

ID와 Password를 사용하는 **실제 Form 기반 유저 인증을 처리**한다.

인증 객체를 만들어서 Authentication 객체를 만들어 아이디 패스워드를 저장하고, AuthenticationManager에게 인증처리를 맡기게 된다.


</br>

- Authentication
    
    사용자의 **인증 정보를 저장**하는 **토큰 개념**입니다.
    
    인증 시 id 와 password 를 담고 인증 검증을 위해 전달되어 사용됩니다.
    
    인증 후 최종 인증 결과 (user 객체, 권한정보) 를 담고 **SecurityContext** 에 저장되어 **전역적으로 참조가 가능**합니다
    

</br>


AuthenticationManager가 실질적인 인증을 검증단계를 총괄하는 클래스인 AuthenticationProvider인증처리를 위임함

그럼 AuthenticationProvider가 UserDetailsService와 같은 서비스를 사용해서 인증을 검증함

최종적으로 인증을 성공한 경우에, 인증에 성공한 결과를 담은 인증객체(Authentication)를 생성한 다음에 SecurityContext에 저장한다.

결국에 SecurityContextHolder 안에 있는 SecurityContext는 **SecurityContextPersistenceFilter**에서 생성하고 저장한 객체를 참조하는 것 (꺼내와서 저장)

인증 후 후속처리를 할텐데, 이때 **SessionManagementFilter**가 가진 Register SessionInfo, SessionFixation, Concurrentsession을 인증을 시도하는 당시 동시에 진행하고 있다.

그래서 인증에 성공했다면 그 사용자가 Concurrentsession, 동시점에 세션이 존재하는지 확인하는데 

만약에, 이 시스템이 동일한 계정으로 생성할 수 있는 세션의 개수가 하나라고 지정되어있는 경우, 현재 사용자 인증 시도를 차단(SessionAuthenticationException)

SessionFixation은 세션 고정 보호이죠. 인증에 성공한 시점에 새롭게 쿠키가 발급된다. 인증을 시도하기 전에 이전에 쿠키가 삭제되고 새로운 쿠키가 발급되도록 작동한다.

Register SessionInfo 사용자의 세션 정보가 등록되는 것.

인증에 성공한 이후에 일반적으로 successHandler등 으로 다음 페이지 (가령 루트페이지)로 이동할텐데.

다시금 시큐리티 필터가 세션에 최종적으로 인증에 성공한 인증객체(SecurityContext)를 세션에 저장하는 처리를 응답 직전에 처리한다.

후 응답하고 루트페이지로 이동됨.


</br>


---

= 자원에 접근하는 Flow


</br>


loadContext 메소드가 지금 접속한 사용자가 SecurityContext를 세션에 저장한지의 여부를 확인한다고 했는데, 

이력이 있을 때 SecurityContext를 꺼내와서 SecurityContextHolder에 저장한다. 

따라서 Create SecurityContext를 실행하지 않음.

최종적으로 클라이언트에게 인증하기 직전에는 항상 Clear SecurityContext를 실행함

LogoutFilter, UsernamePasswordAuthenticationFilter 건너 뜀


</br>


### **ConcurrentSessionFilter**

동시적인 세션에 관련된 필터인데, 현재 사용자 계정으로 인증을 받은 사용자가 두 명 이상일 때 작동한다.


</br>

### **RememberMeAuthenticationFilter**

*세션이 사라지거나 만료 되더라도 쿠키 또는 DB를 사용하여 저장된 토큰 기반으로 인증을 처리하는 필터*

세션이 만료되거나 무효화되어서 세션안에 있는 SecurityContext내의 인증 객체가 null일 경우 해당 필터가 작동한다.

인증 객체가 null일 경우에 현재 사용자가 요청하는 request header에 remember-me cookie 값을 헤더에 저장한 상태로 왔을 때

이 필터가 접속한 사용자 대신에 인증처리를 시도하고 있는 것.

만약, remember-me 기능을 활성하고 인증받고 세션이 만료되면 실행되겠죠


</br>

### **AnonymousAuthenticationFilter**

이 필터가 호출되는 시점까지 사용자 정보가 인증되지 않았다면 익명 사용자 토큰을 반환한다.

익명 사용자 필터

인증 시도도 하지 않고 권한도 없이 어떤 자원에 바로 접속을 시도하는 경우, 익명 사용자용 필터가 실행됨

이 필터는 인증되지 않은 사용자가 접근했을 때 annonymouseAuthenticationToken을 만들어서 SecurityContext 객체에 저장하는 역할을 함


</br>

### **SessionManagementFilter**

로그인 이후 인증된 사용자인지 확인하거나 설정된 Session 메커니즘에 따라 작업을 수행한다. (동시 로그인 확인 등...)

이 필터는 조건이 현재 세션에 SecurityContext이 없거나 세션이 null인 경우에 동작된다.

**Register SessionInfo**

**SessionFixation**

**Concurrentsession**


</br>


---
 

인증 이후 자원에 접근할 때 가장 큰 역할을 하는 필터: ExceptionTranslationFilter, FilterSecurityInterceptor

</br>

### ExceptionTranslationFilter

필터 체인 내에서 발생되는 모든 예외(AccessDeniedException, AuthenticationException...)를 처리한다.

인증, 인가 예외가 발생할 경우 실행됨

근데 try, catch로 감싸서 chain.doFilter 로 바로 다음 필터로 넘겨 이동해버림

그냥 예외만 처리하는 필터

</br>

### FilterSecurityInterceptor

권한부여와 관련한 결정을 AccessDecisionManager에게 위임해 권한부여 결정 및 접근 제어를 처리한다.

Check authenticated: 인증 객체가 존재하는 지. 없으면 즉시 인증 예외를 날림(인가불가능하기 때문) 있으면 아래

AccessDecisionManager: 인가 처리. 얘가 내부적으로 아래(accessDecisionVoter) 실행. 권한 없으면 AccessDeniedException을 날림

accessDecisionVoter: 접근하고자하는 자원의 승인과 거부 

</br>

---

= 두 번째는 동일한 계정으로 인증을 시도하는 경우

모든 과정은 위와 동일한데, UsernamePasswordAuthenticationFilter과 SessionManagementFilter가 동시에 실행된다고 했는데, 이때 ConcurrentSession에서 조건에 걸림.

두 가지 전략에 따름

1. 인증 자체를 실행하지 못하도록 인증관련 예외를 날리는 방법 → 세션이 하나만 남기 때문에 하나만 남음
2. 현재 사용자는 인증을 계속 사용하고, 이전 사용자의 세션을 만료시킴 


### ConcurrentSessionFilter

매 요청마다 현재 사용자가 세션이 만료되었는지 확인한다.

두 번째 사용자가 인증하다가 첫 번째 사용자의 세션을 만료하는 설정을 함(session.expireNow)

그래서 해당 사용자를 로그아웃하고 요청 실패를 날림