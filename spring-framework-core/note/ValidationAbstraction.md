# Validation Abstraction

### Validation 추상화

> *[org.springframework.validation.Validator](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Validator.html)*
> 

애플리케이션에서 사용하는 객체 검증용 인터페이스. 

### 특징

✔️ 어떤한 계층과도 관계가 없다. => 모든 계층(웹, 서비스, 데이터)에서 사용해도 좋다.

✔️ 구현체 중 하나로, JSR-303(Bean Validation 1.0)과 JSR-349(Bean Validation 1.1)을 지원한다. ([LocalValidatorFactoryBean](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/beanvalidation/LocalValidatorFactoryBean.html))

✔️ DataBinder에 들어가 바인딩 할 때 같이 사용되기도 한다. 

### 인터페이스

✔️ boolean supports(Class clazz): 어떤 타입의 객체를 검증할 때 사용할 것인지 결정함

✔️ void validate(Object obj, Errors e): 실제 검증 로직을 이 안에서 구현

- 구현할 때 ValidationUtils 사용하며 편리 함.

- 원시적인 코드
    
    ```java
    public class EventValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return Event.class.equals(clazz);
        }
    
        @Override
        public void validate(Object target, Errors errors) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "notempty", "Empty title is allowed");
    //        or
    
    //        Event event = (Event)target;
    //        if (event.getTitle() == null) {
    //            errors.reject("reject!");
    //        }
        }
    }
    ```
    
    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Autowired
        ResourceLoader resourceLoader;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("======= Validator AppRunner =======");
    
            Event event = new Event();
            EventValidator eventValidator = new EventValidator();
            // 실질적으로 이 클래스를 사용할 일은 거의 없음
            Errors errors = new BeanPropertyBindingResult(event, "event");
    
            // 아주 원시적인 방법
            eventValidator.validate(event, errors);
    
            System.out.println(errors.hasErrors());
    
            errors.getAllErrors().forEach(e -> {
                System.out.println("====== error code ======");
                Arrays.stream(e.getCodes()).forEach(System.out::println);
                System.out.println(e.getDefaultMessage());
            });
        }
    }
    
    ```
    
- **Validator** 로 변경
    
    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Autowired
        Validator validator;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("======= Validator AppRunner =======");
            System.out.println(validator.getClass());
    
            Event event = new Event();
    
            event.setLimit(-4);
            event.setEmail("dfnlad");
    
            Errors errors = new BeanPropertyBindingResult(event, "event");
    
            validator.validate(event, errors);
    
            System.out.println(errors.hasErrors());
            errors.getAllErrors().forEach(e -> {
                System.out.println("====== error code ======");
                Arrays.stream(e.getCodes()).forEach(System.out::println);
                System.out.println(e.getDefaultMessage());
            });
        }
    }
    ```
    
    ```bash
    
    ======= Validator AppRunner =======
    class org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
    true
    ====== error code ======
    NotEmpty.event.title
    NotEmpty.title
    NotEmpty.java.lang.String
    NotEmpty
    비어 있을 수 없습니다
    ====== error code ======
    Email.event.email
    Email.email
    Email.java.lang.String
    Email
    올바른 형식의 이메일 주소여야 합니다
    ====== error code ======
    Min.event.limit
    Min.limit
    Min.java.lang.Integer
    Min
    0 이상이어야 합니다
    ```
    

- 검증에 필요한 package
    
    ```xml
    <dependency>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    ```
    

### 스프링 부트 2.0.5 이상 버전을 사용할 때

✔️ LocalValidatorFactoryBean 빈으로 자동 등록

✔️ JSR-380(Bean Validation 2.0.1) 구현체로 hibernate-validator 사용.

✔️ [https://beanvalidation.org/](https://beanvalidation.org/)