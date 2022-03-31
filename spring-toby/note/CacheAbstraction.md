# 캐시 추상화

<details>
<summary> 공식문서 </summary>

[🔗 링크](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/cache.html)

기본적으로 추상화는 Java 메서드에 캐싱을 적용하므로 캐시에서 사용할 수 있는 정보를 기반으로 실행되는 횟수가 줄어듭니다. 
즉, 대상 메서드가 호출될 때마다 추상화는 지정된 인수에 대해 메서드가 이미 실행되었는지 확인하는 캐시 동작을 적용합니다.
만약 그렇다면, 캐시된 결과는 실제 메서드를 실행할 필요 없이 반환됩니다. 그렇지 않으면 메서드가 실행되고 결과가 캐시되어 사용자에게 반환되므로 다음 번에 메서드가 호출될 때 캐시된 결과가 반환됩니다.


이렇게 하면 고가의 메서드(CPU 또는 IO 바인딩 여부에 관계없이)를 주어진 파라미터 집합에 대해 한 번만 실행할 수 있으며 실제로 메서드를 다시 실행할 필요 없이 결과를 재사용할 수 있습니다.
캐싱 논리는 호출자에 대한 간섭 없이 투명하게 적용됩니다.

📌 이 접근 방식은 실행 횟수에 관계없이 특정 입력(또는 인수)에 대해 동일한 출력(결과)을 반환하도록 보장된 메서드에만 적용됩니다.


캐시 추상화를 사용하려면 개발자가 다음 두 가지 측면을 고려해야 합니다.
캐싱 선언 - 캐싱해야 하는 메서드와 정책을 식별합니다.
캐시 컨피규레이션 - 데이터를 저장 및 읽어들이는 백업캐시
</details>

<br/><br/>

스프링 3.1은 빈의 메소드에 캐시 서비스를 적용할 수 있는 기능을 제공
트랜잭션과 마찬가지로, AOP를 이용해 메소드 실행 과정에 투명하게 적용된다.

따라서 캐시 API를 애플리케이션 빈 코드에 추가하지 않아도 손쉽게 캐시 기능을 부여할 수 있다.
또한 캐시 서비스 구현 기술에 종속되지 않도록 추상화 서비스를 제공하기 때문에 환경이 바뀌거나 적용할 기술을 변경해서 캐시 서비스의 종류가 달라지더라도 애플리케이션 코드에 영향을 주지 않는다.

<br/>

### 빈 메소드에 캐시를 적용하는 목적

캐시cache = 임시 저장소

캐시는 기본적으로 성능 향상을 위해 사용된다.
계산이 굉장히 복잡하거나, DB 같은 백엔드 시스템에 많은 부하를 주거나, 원격 API 호출처럼 시간이 오래 걸리다면 캐시의 사용을 고려해볼만하다.
처리 결과를 캐시에 저장해뒀다가 동일한 요청이 들어오면 복잡한 작업을 수행해서 결과를 만드는 대신 캐시에 보관해뒀던 기존 결과를 바로 돌려주는 방식이다.


<br/><br/>

Cache를 사용하면 자주 사용되는 리소스가 존재 할시 리소스를 얻은 후 캐시 저장소에 만료시간과 함께 저장하고 사용자가 조회를 요청할 때 마다 만료시간 이전까지는 캐시 저장소에 있는 리소스를 사용함으로써 조회 성능을 대폭 향상 시킬 수 있다.

일반적으로 동일한 리소스 대해 빈번한 SELECT로 발생되는 DBMS 과부하를 줄이고자 사용한다.

스프링은 캐시 추상화 (Cache Abstraction)을 통해 편리한 캐싱 기능을 지원하고 있다.

<aside>
💡 Spring Cache Abstraction ? 

사용자는 캐시 구현에 대해 신경 쓸 필요없이 퍼블릭 인터페이스로 쉽게 캐싱 기능을 사용할 수 있는 것을 말한다.

더 자세히 말하면 캐싱이 필요한 비즈니스 로직에서 EhCache, redis 등 캐싱 인프라스트럭쳐에 의존하지않고 추상화된 퍼블릭 인터페이스로  캐싱을 할 수 있다. 이는 만약 EhCache로 사용중이다가 Redis로 변경되더라도 비즈니스로직에는 영향을 주지 않는다는 장점을 말한다.

</aside>

| 코드 | 설명 |
| --- | --- |
| EhCacheManagerFactoryBean | CacacheManager의 적절한 관리 및 인스턴스를 제공하는데 필요하며 EhCache 설정 리소스를 구성한다.  |
| setConfigLocation | 지정된 경로를 통해 EhCache 설정 리소스를 로드한다. (미지정시 루트의 ehcache.xml파일을 찾음). |
| setShared | CacheManager 싱글톤 여부 (default= false). |
| @EnableCaching | Annotation을 사용하여 캐싱 기능을 이용하겠다고 선언. |

### Cache 설정

| cache 속성 | 설명 | default | required |
| --- | --- | --- | --- |
| name | 캐시명. | 필수 | true |
| eternal | true일 경우 timeout 관련 설정이 무시, 
element가 캐시에서 삭제되지 않음. | false | true |
| timeToldleSeconds | Element가 지정한 시간 동안 사용(조회)되지 않으면 캐시에서 제거된다. 이 값이 0인 경우 조회 관련 만료 시간을 지정하지 않는다. | 0 | false |
| timeToLiveSeconds | Element가 존재하는 시간. 이 시간이 지나면 캐시에서 제거된다. 이 시간이 0이면 만료 시간을 지정하지 않는다. | 0 | false |
| diskPersistent | VM이 재 가동할 때 디스크 저장소에 캐싱된 객체를 저장할지의 여부를 지정한다. | false | false |
| diskExpiryThreadIntervalSeconds | Disk Expiry 쓰레드의 수행 시간 간격을 초 단위로 지정한다. | 120 | false |
| memoryStoreEvictionPolicy | 객체의 개수가 maxElementsInMemory에 도달했을 때, 메모리에서 객체를 어떻게 제거할 지에 대한 정책을 지정한다. LRU, FIFO, LFU 지정 가능. | LRU | false |

[](https://www.ehcache.org/ehcache.xml)

<aside>
💡 디폴트 캐시 설정

이러한 설정은 CacheManager.add(String cacheName)를 사용하여 프로그래밍 방식으로 작성된 캐시에 적용됩니다. 이 요소는 옵션이며 CacheManager.add(String cacheName)가 존재하지 않을 때 사용하면 CacheException이 느려집니다.

defaultCache에는 예약된 캐시 이름인 암묵적인 이름 "default"가 있습니다.

</aside>

DefaultCache는 ehCache CacheManager의 `addCacheIfAbsent` 에서 호출한다.

- Code
    
    ```java
    public synchronized Ehcache addCacheIfAbsent(final String cacheName) {
        checkStatus();
    
        // NPE guard
        if (cacheName == null || cacheName.length() == 0) {
            return null;
        }
    
        Ehcache ehcache = ehcaches.get(cacheName);
        if (ehcache == null) {
            Ehcache clonedDefaultCache = cloneDefaultCache(cacheName);
            if (clonedDefaultCache == null) {
                throw new CacheException(NO_DEFAULT_CACHE_ERROR_MSG);
            }
            addCacheIfAbsent(clonedDefaultCache);
            for (Ehcache createdCache : createDefaultCacheDecorators(clonedDefaultCache)) {
                addOrReplaceDecoratedCache(clonedDefaultCache, createdCache);
            }
        }
        return ehcaches.get(cacheName);
    }
    ```
    

## @Cacheable

@Cacheable 이외에도 @CacheEvict, @CachePut, @Caching, @CacheConfig가 존재한다.

| @Cacheable 속성 | 설명 | Default |
| --- | --- | --- |
| value, cacheName | ehcache.xml 또는 ehcach config에 구성된 cache name. | {} |
| key | 동일한 cache name을 사용하지만 구분될 필요가 있을 경우 사용되는 값, (상수 권장). | "" |
| cacheManager | 사용 할 CacheManager 지정 (EHCacheCacheManager, RedisCacheManager 등) . | "" |
| condition | SpEL 표현식을 작성하여 참일 경우에만 캐싱이 적용됨 
(or, and 등 조건식, 논리연산 가능).

EX) 
@Cacheable(key = "testKey", condition="#caching") 
public Object getSome(boolean caching) { /* ... */ } | "" |
| unless | condition과 반대로 참일 경우에만 캐싱이 적용되지 않는다. | "" |
| sync | 캐시 구현체가 Thread safe 하지 않는 경우, 캐시에 동기화를 걸 수 있는 속성이다. | false |

[Cacheable](https://docs.spring.io/spring-framework/docs/4.2.4.RELEASE_to_4.2.5.RELEASE/Spring%20Framework%204.2.5.RELEASE/org/springframework/cache/annotation/Cacheable.html)

- **SpEL**
    - keySpring Expression Language (SpEL) expression for computing the key dynamically.
        
        
        > public abstract java.lang.String key
        > 
        
        Default is `""`, meaning all method parameters are considered as a key, unless a custom `[keyGenerator()](https://docs.spring.io/spring-framework/docs/4.2.4.RELEASE_to_4.2.5.RELEASE/Spring%20Framework%204.2.5.RELEASE/org/springframework/cache/annotation/Cacheable.html#keyGenerator--)` has been configured.
        
        The SpEL expression evaluates again a dedicated context that provides the following meta-data:
        
        - `#root.method`, `#root.target` and `#root.caches` for a reference to the `method`, target object and affected cache(s) respectively.
        - Shortcuts for the method name (`#root.methodName`) and target class (`#root.targetClass`) are also available.
        - Method arguments can be accessed by index. For instance the second argument can be access via `#root.args[1]`, `#p1` or `#a1`. Arguments can also be accessed by name if that information is available.
        
        **Default:**""
        

## **@CacheEvict**

메서드가 호출 될 때 저장된 캐시를 삭제해주는 어노테이션이다.

| @CacheEvict 속성 | 설명 | Default |
| --- | --- | --- |
| value, cacheName | ehcache.xml 또는 ehcache config에 구성된 cache name. | {} |
| key | 동일한 cache name을 사용하지만 구분될 필요가 있을 경우 사용되는 값, (상수 권장). | "" |
| cacheManager | 사용 할 CacheManager 지정 (EHCacheCacheManager, RedisCacheManager 등) . | "" |
| condition | SpEL 표현식을 작성하여 참일 경우에만 캐싱이 적용됨 (or, and 등 조건식, 논리연산 가능).

EX) 
@CacheEvict(key = "testKey", condition="#caching") 
public Object removeSome(boolean caching) | "" |
| allEntries | 선언된 메서드로 캐싱된 캐시 리소스를 모두 삭제한다. | false |
| beforeInvocation | true - 메서드 수행 이전 캐시 리소스 삭제
false - 메서드 수행 후 캐시 리소스 삭제가 된다. | false |

## **@CachePut**

저장된 캐시를 갱신할 때 사용되며 메서드의 몸체가 매번 수행된다.

| @CachePut 속성 | 설명 | Default |
| --- | --- | --- |
| value, cacheName | ehcache.xml 또는 ehcach config에 구성된 cache name. | {} |
| key | 동일한 cache name을 사용하지만 구분될 필요가 있을 경우 사용되는 값, (상수 권장). | "" |
| cacheManager | 사용 할 CacheManager 지정 (EHCacheCacheManager, RedisCacheManager 등) . | "" |
| condition | SpEL 표현식을 작성하여 참일 경우에만 캐싱이 적용됨, (or, and 등 조건식, 논리연산 가능).EX) @Cacheable(key = "testKey", condition="#caching") public Object modifySome(boolean caching) | "" |
| unless | condition과 반대로 참일 경우에만 캐싱이 적용되지 않는다. | "" |

## **@Caching**

하나의 메서드를 호출 할 때 Cacheable, CacheEvict 등 여러개의 캐싱 동작을 수행해야 할 때 사용된다.

| @Caching 속성 | 설명 | Default |
| --- | --- | --- |
| cacheable[] | 적용 될 @Cacheable array를 등록한다.  | {} |
| evict[] | 적용 될 @CacheEvict array를 등록한다.  | {} |
| put[] | 적용 될 @Cacheput array를 등록한다.  | {} |

## **@CacheConfig**

클래스 단위로 캐시 설정을 동일하게 하고 싶을 때 사용한다.

| @CacheConfig 속성 | 설명 | Default |
| --- | --- | --- |
| cacheNames | 캐시명. | {} |
| cacheManager | 사용 할 CacheManager 지정 (EHCacheCacheManager, RedisCacheManager 등) . |  |

실제 저장되는 데이터 예시
