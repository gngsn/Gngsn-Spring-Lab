# Resource Abstraction

## Resource 추상화

> org.springframework.core.io.Resource
> 

### 특징

✔️ java.net.URL을 추상화 한 것. → (감싼 것) 실제 low level을 접근함

✔️ 스프링 내부에서 많이 사용하는 인터페이스.

### 추상화 한 이유

✔️ 클래스패스 기준으로 리소스 읽어오는 기능 부재

✔️ ServletContext를 기준으로 상대 경로로 읽어오는 기능 부재

✔️ 새로운 핸들러를 등록하여 특별한 URL 접미사를 만들어 사용할 수는 있지만 구현이 복잡하고 편의성 메소드가 부족하다.

```java
var ctx = new ClassPathXmlApplicationContext("testest.xml");

// ApplicationContext를 만들 때 위와 같이 xml 파일을 만들었다
// 이 문자열 자체가 아래와 같이 resource로 변환이 됨
// 내부적으로 사용자 모르게 Resource를 사용하고 있던 것

Resource resource = resourceLoader.getResource(/* 내부적으로 이 안에 문자열이 들어감 */);
```

### 인터페이스 둘러보기

[🔗 Link](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/Resource.html)

✔️ 상속 받은 인터페이스

✔️ 주요 메소드

- getInputStream()

- exitst()    → 항상 존재한다고 가정하지 않음

- isOpen()

- getDescription(): 전체 경로 포함한 파일 이름 또는 실제 URL

### 구현체

✔️ **UrlResource**: [java.net.URL](https://docs.oracle.com/javase/7/docs/api/java/net/URL.html) 참고, 기본으로 지원하는 프로토콜 **http, https, ftp, file, jar**.

✔️ ClassPathResource: 지원하는 접두어 **classpath**:

✔️ FileSystemResource

✔️ **ServletContextResource**: 웹 애플리케이션 루트에서 상대 경로로 리소스 찾는다. 

→ 사실상 가장 많이 사용됨. 읽어들이는 타입이 applicationContext의 타입에 따라 달라짐

### 리소스 읽어오기

✔️ Resource의 타입은 locaion 문자열과 ApplicationContext의의 타입에 따라 결정 된다.

- ClassPathXmlApplicationContext -> ClassPathResource

- FileSystemXmlApplicationContext -> FileSystemResource

- WebApplicationContext -> ServletContextResource

**✔️ ApplicationContext의의 타입에 상관없이 리소스 타입을 강제하려면 java.net.URL 접두어(+접두어 classpath:)중중 하나를 사용할 수 있다.**

- **classpath:me**/whiteship/config.xml -> ClassPathResource

- **file**:///some/resource/path/config.xml -> FileSystemResource

접두어를 사용하는 것을 추천함 → 리소스가 어디서 가지고 와지는지 알고 사용하는 개발자가 별로 없기 때문에 명시해서 파악하는 것이 좋음