# SpEL

## SpEL: Spring Expression Language

### 스프링 EL이란?

[🔗 Link](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions)

✔️ 객체 그래프를 조회하고 조작하는 기능을 제공한다.

✔️ [Unified EL](https://docs.oracle.com/javaee/5/tutorial/doc/bnahq.html)과 비슷하지만, **메소드 호출을 지원**하며, **문자열 템플릿 기능**도 제공한다. 

→ 스프링 전반에 걸쳐 필요하게 되어서 코어에 추가되어 아래 사용되는 곳에서 사용됨

✔️ OGNL, MVEL, JBOss EL 등 자바에서 사용할 수 있는 여러 EL이 있지만, SpEL은 모든 스프링 프로젝트 전반에 걸쳐 사용할 EL로 만들었다.

✔️ 스프링 3.0 부터 지원.

### SpEL 구성

✔️ **ExpressionParser** parser = new SpelExpressionParser()

✔️ StandardEvaluationContext context = new StandardEvaluationContext(bean)

✔️ Expression expression = parser.parseExpression(“SpEL 표현식”)

✔️ String value = expression.getvalue(context, String.class)

### 문법

✔️ #{“표현식"}

✔️ ${“프로퍼티"}

✔️ 표현식은 프로퍼티를 가질 수 있지만, 반대는 안 됨.

- #{${my.data} + 1}

✔️ 레퍼런스 참고

- **Code**
    
    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Value("#{ 1+1 }")
        int value;
    
        @Value("#{'hello ' + 'world'}")
        String greeting;
    
        @Value("#{1 eq 1}")
        String tf;
    
        @Value("hello")
        String hello;
    
        // properties
        @Value("${my.value}")
        int myValue;
    
        @Value("#{${my.value} eq 100}")
        boolean myValueTf100;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println(value);
            System.out.println(greeting);
            System.out.println(tf);
            System.out.println(myValue);
            System.out.println(myValueTf100);
        }
    }
    
    /*
    2
    hello world
    true
    100
    true
    */
    
    ```
    
- Bean 접근 가능
    
    ```java
    @Component
    public class Sample {
        int data = 200;
    
        public int getData() { return data; }
    
        public void setData(int data) { this.data = data; }
    }
    ```
    
    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Value("#{sample.data}") // Bean
        int sampleData;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println(sampleData); // 200
        }
    }
    ```
    

메서드를 호출하는 기능도 있는데, 잘 사용하지는 않음 

### 실제로 어디서 쓰나?

✔️ @Value 애노테이션

✔️ @ConditionalOnExpression 애노테이션

✔️ [스프링 시큐리티](https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)

- 메소드 시큐리티, @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
    
    ```java
    @PreAuthorize("hasPermission(#contact, 'admin')")
      public void deletePermission(Contact contact, Sid recipient, Permission permission);
    ```
    

- XML 인터셉터 URL 설정

- ...

✔️ [스프링 데이터](https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions)

- @Query 애노테이션

- ✔️ [Thymeleaf](https://blog.outsider.ne.kr/997)
    
    ```java
    #{T(kr.ne.outsider.Codes).values()}
    #{T(kr.ne.outsider.Codes).ERROR}
    #{T(kr.ne.outsider.Codes).MESSAGE}
    ```
    

✔️ ...

**Parser**

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExpressionParser parser = new SpelExpressionParser();

				// 안의 스트링 자체가 익스프레션
        Expression expression = parser.parseExpression("2 + 100"); 
        Integer value = expression.getValue(Integer.class);
        System.out.println(value);
    }
}

```