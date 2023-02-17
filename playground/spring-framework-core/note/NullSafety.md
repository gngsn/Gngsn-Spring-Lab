# Null-Safety

컴파일 타임의 Null Point Exception 을 미연에 방지하기 위한 목적

스프링 프레임워크 5에 추가된 Null 관련 애노테이션

✔️ @NonNull

✔️ @Nullable

✔️ @NonNullApi (패키지 레벨 설정)

✔️ @NonNullFields (패키지 레벨 설정)

```java
@Service
public class EventService {

    @NonNull
    public String createEvent(@NonNull String name) {
        return "hello " + name;
    }
}
```

### 목적

✔️ (툴의 지원을 받아) 컴파일 시점에 최대한 NullPointerException을 방지하는 것