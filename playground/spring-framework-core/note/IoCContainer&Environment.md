# IoC Container & Environment

ApplicationContext가 BeanFactory 기능만 하는 것은 아님

많은 인터페이스들을 상속받는데, EnvironmentCapable 인터페이스가 포함.

EnvironmentCapable 크게 두 가지 기능을 갖는데, Capable 기능을 알아봄

## IoC 컨테이너 6부: Environment 1부. 프로파일

프로파일과 프로퍼티를 다루는 인터페이스.

프로파일 : 어떤 환경. 테스트환경에서는 ~ 빈들을 쓰고, 알파에는 ~ 빈들을 사용하겠다. 

각각의 환경 혹은 특정 환경에서 다른 빈들을 사용하거나 등록해야하는 경우에 대한 요구사항을 충족시키기 위해 프로파일 기능이 추가되었고, 그걸 Environment 로 사용할 수 있음.

ApplicationContext extends [EnvironmentCapable](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/env/EnvironmentCapable.html)

- ✔️ getEnvironment()
    
    ```java
    public interface EnvironmentCapable {
        Environment getEnvironment();
    }
    ```
    

### 프로파일

✔️ 빈들의 그룹

✔️ [Environment](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/env/Environment.html)의 역할은 활성화할 프로파일 확인 및 설정

### 프로파일 유즈케이스

✔️ 테스트 환경에서는 A라는 빈을 사용하고, 배포 환경에서는 B라는 빈을 쓰고 싶다.

✔️ 이 빈은 모니터링 용도니까 테스트할 때는 필요가 없고 배포할 때만 등록이 되면 좋겠다.

### 프로파일 정의하기

[🔗 Spring Docs](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-definition-profiles-java)

✔️ 클래스에 정의

- @Configuration @Profile(“test”)
    
    ```java
    @Configuration
    @Profile("test")
    public class TestConfiguration {
    
        @Bean
        public BookRepository getBookRepository() {
            return new TestBookRepository();
        }
    }
    ```
    
    test 환경일 때에만 실행됨
    
    ```java
    @Component
    public class AppRunner2 implements ApplicationRunner {
    		...
        @Autowired
        ApplicationContext ctx;
    		...
    }
    
    /* 위 코드는 test 환경이 아닐 때에 Bean을 생성하지 못하니까 ERROR
    *  Error:(14, 16) java: incompatible types: com.gngsn.springcore.environment.TestBookRepository cannot be converted to com.gngsn.springcore.book.BookRepository
    *
    */
    ```
    
- @Component @Profile(“test”)
    
    ```java
    @Configuration
    public class TestConfiguration {
    
        @Bean
        public BookRepository getBookRepository() {
            return new TestBookRepository();
        }
    }
    ```
    

✔️ 메소드에 정의

- @Bean @Profile(“test”)

### 프로파일 설정하기

✔️ -Dspring.profiles.avtive=”test,A,B,...”

✔️ [@ActiveProfiles](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/context/ActiveProfiles.html) (테스트용)

![스크린샷 2022-02-26 오후 8.28.29.png](img/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-02-26_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_8.28.29.png)

![스크린샷 2022-02-26 오후 8.35.30.png](img/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-02-26_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_8.35.30.png)

### 프로파일 표현식

- `!`  : A logical “not” of the profile
    
    `@Profile("!prod")`
    
    prod 환경이 아닌 모든 환경
    

✔️ `&` : A logical “and” of the profiles

✔️ `|` : A logical “or” of the profiles

---

## IoC 컨테이너 6부: Environment 2부. 프로퍼티

### 프로퍼티

✔️ 다양한 방법으로 정의할 수 있는 설정값

✔️ [Environment](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/env/Environment.html)의 역할은 프로퍼티 소스 설정 및 프로퍼티 값 가져오기

**프로퍼티에는 우선 순위가 있다.**

✔️ StandardServletEnvironment의 우선순위

- ServletConfig 매개변수

- ServletContext 매개변수

- JNDI (java:comp/env/)

- JVM 시스템 프로퍼티 (-Dkey=”value”)
    
    ![스크린샷 2022-02-26 오후 8.56.54.png](img/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-02-26_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_8.56.54.png)
    
    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Autowired
        ApplicationContext ctx;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            Environment env = ctx.getEnvironment();
            System.out.println("app.name : " + env.getProperty("app.name"));
        }
    }
    
    // app.name : spring5
    ```
    
- JVM 시스템 환경 변수 (운영 체제 환경 변수)
    
    ```java
    @SpringBootApplication
    @PropertySource("classpath:/app.properties") <- 추가
    public class SpringCoreApplication { ... }
    ```
    
    ```java
    @Component
    public class AppRunner2 implements ApplicationRunner {
        @Autowired
        ApplicationContext ctx;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            Environment env = ctx.getEnvironment();
            System.out.println("app.name : " + env.getProperty("app.name"));
            System.out.println("app.about : " + env.getProperty("app.about"));
        }
    }
    
    /*
    app.name : spring5
    app.about : spring
    */
    ```
    

### @PropertySource

✔️ Environment를 통해 프로퍼티 추가하는 방법

**스프링 부트의 외부 설정 참고**

✔️ 기본 프로퍼티 소스 지원 (application.properties)

✔️ 프로파일까지 고려한 계층형 프로퍼티 우선 순위 제공