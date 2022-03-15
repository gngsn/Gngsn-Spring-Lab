## @ControllerAdvice

[🔗 Spring.io - ControllerAdvice](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/ControllerAdvice.html)

<br/>

``` java
@Target(value=TYPE)
 @Retention(value=RUNTIME)
 @Documented
 @Component
public @interface ControllerAdvice
```

<br/>

@ExceptionHandler가 하나의 클래스에 대한 것이라면, 
@ControllerAdvice는 모든 @Controller 전역에서 발생할 수 있는 예외를 잡아 처리해주는 annotation이다.

<br/><br/>

## @RestControllerAdvice

[🔗 Spring.io - RestControllerAdvice](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestControllerAdvice.html)

<br/>

``` java
@Target(value=TYPE)
 @Retention(value=RUNTIME)
 @Documented
 @ControllerAdvice
 @ResponseBody
public @interface RestControllerAdvice
```