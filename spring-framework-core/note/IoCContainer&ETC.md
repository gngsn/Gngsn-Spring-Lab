# IoC Container & ETC

## IoC 컨테이너 7부: MessageSource

국제화 (i18n) 기능을 제공하는 인터페이스.

> *ApplicationContext extends **MessageSource***
> 

✔️ getMessage(String code, Object[] args, String, default, Locale, loc)

✔️ ...

스프링 부트를 사용한다면 별다른 설정 필요없이 messages.properties 사용할 수 있음

✔️ messages.properties

✔️ messages_ko_kr.properties

✔️ ...

**릴로딩 기능이 있는 메시지 소스 사용하기**

```java
@Bean public MessageSource messageSource() { 
		var messageSource = new ReloadableResourceBundleMessageSource(); 
		messageSource.setBasename("classpath:/messages"); 
		messageSource.setDefaultEncoding("UTF-8"); 
		messageSource.setCacheSeconds(3); 
		return messageSource; 
}
```

---

## IoC 컨테이너 8부: ApplicationEventPublisher

이벤트 프로그래밍에 필요한 인터페이스 제공. 옵저버 패턴 구현체. 

> *ApplicationContext extends **ApplicationEventPublisher***
> 

- ApplicationEvent를 상속 받는 Event 객체 생성
    
    ```java
    public class MyEvent extends **ApplicationEvent** {
        private int data;
    
        public MyEvent(Object source, int data) {
            super(source);
            this.data = data;
        }
    
        public int getData() {
            return data;
        }
    }
    ```
    
- ✔️ publishEvent(ApplicationEvent event)
    
    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Autowired
        ApplicationEventPublisher publishEvent;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("======= Event AppRunner =======");
            publishEvent.**publishEvent**(new MyEvent(this, 100));
        }
    }
    ```
    

### 이벤트 만들기

✔️ ApplicationEvent 상송

✔️ 스프링 4.2 부터는 이 클래스를 **상속받지 않아도 이벤트로 사용**할 수 있다. 

- **ApplicationEvent 변화 → 상속 제거**
    
    스프링 프레임워크가 추구하는 방향 → **비침투성**을 반영
    
    → 스프링 패키지가 추가되어 있지 않음
    
    ```java
    public class MyEvent {
        private int data;
    
        Object source;
    
        public MyEvent(Object source, int data) {
            this.source = source;
            this.data = data;
        }
    
        public Object getSource() {
            return source;
        }
    
        public int getData() {
            return data;
        }
    }
    ```
    
- **ApplicationListener 변화 → 상속 제거, Annotation**
    
    ApplicationListener 를 상속받지 않고 Annotation으로 추가할 수 있다.
    
    ```java
    @Component
    public class MyEventHandler {
    
        @EventListener
        public void onApplicationEvent(MyEvent event) {
            System.out.println("이벤트 받았다. 데이터는 " + event.getData());
        }
    }
    ```
    

### 이벤트 발생 시키는 방법

✔️ ApplicationEventPublisher.publishEvent(); 

### 이벤트 처리하는 방법

✔️ ApplicationListener<이벤트> 구현한 클래스 만들어서 빈으로 등록하기.

✔️ 스프링 4.2 부터는 @EventListener를 사용해서 빈의 메소드에 사용할 수 있다.

✔️ 기본적으로는 synchronized.

- ✔️ 순서를 정하고 싶다면 **@Order**와 함께 사용.
    
    ```java
    @Component
    public class MyEventHandler {
    
        @EventListener
        **@Order(Ordered.HIGHEST_PRECEDENCE)**
        public void onApplicationEvent(MyEvent event) {
            System.out.println(MyEventHandler.class);
            System.out.println(Thread.currentThread().toString());
            System.out.println("이벤트 받았다. 데이터는 " + event.getData());
        }
    }
    ```
    
- ✔️ 비동기적으로 실행하고 싶다면 @Async와 함께 사용.
    
    Thread가 다르기 때문에 Order가 소용이 없음
    

### 스프링이 제공하는 기본 이벤트

- ✔️ **ContextRefreshedEvent**: ApplicationContext를 초기화 했더나 리프래시 했을 때 발생.
    
    ```java
    @Component
    public class AnotherHandler {
    
        @EventListener
        public void handle(ContextRefreshedEvent event) {
            System.out.println(Thread.currentThread().toString());
            System.out.println("[ContextRefreshedEvent]\n" + event.getSource());
        }
    }
    ```
    

✔️ ContextStartedEvent: ApplicationContext를 start()하여 라이프사이클 빈들이 시작 신호를 받은 시점에 발생.

✔️ ContextStoppedEvent: ApplicationContext를 stop()하여 라이프사이클 빈들이 정지 신호를 받은 시점에 발생.

- ✔️ **ContextClosedEvent**: ApplicationContext를 close()하여 싱글톤 빈 소멸되는 시점에 발생.
    
    ```java
    @Component
    public class AnotherHandler {
    
        @EventListener
        public void handle(ContextClosedEvent event) {
            System.out.println(Thread.currentThread().toString());
            System.out.println("[ContextClosedEvent]\n" + event.getSource());
        }
    }
    
    /*
    Thread[SpringApplicationShutdownHook,5,main]
    Another ContextClosedEvent org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@783f1e67, started on Sat Feb 26 22:44:25 KST 2022
    */
    ```
    

✔️ RequestHandledEvent: HTTP 요청을 처리했을 때 발생.

---

## IoC 컨테이너 9부: ResourceLoader

리소스를 읽어오는 기능을 제공하는 인터페이스

`**ApplicationContext** extends ResourceLoader`

### 리소스 읽어오기

✔️ 파일 시스템에서 읽어오기

✔️ 클래스패스에서 읽어오기

✔️ URL로 읽어오기

✔️ 상대/절대 경로로 읽어오기

`Resource **getResource**(java.lang.String location)`

- CODE
    
    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Autowired
        ResourceLoader resourceLoader;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("======= Resource AppRunner =======");
    				// target/classes/ ..
            Resource resource = resourceLoader.getResource("classpath:test.txt");
            System.out.println(resource.exists());
            System.out.println(resource.getDescription());
            System.out.println(Files.readString(Path.of(resource.getURI())));
        }
    }
    ```
    

자세한건 다음에 이어질 Resource 추상화 시간에 자세히 다루겠습니다.