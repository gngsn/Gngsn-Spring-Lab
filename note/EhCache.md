# EhCache

[](https://www.ehcache.org/ehcache.xml)

# Cache

Cache? 가져오는데 비용이 드는 데이터를 한번 가져온 뒤 복사본을 속도가 빠른 임시 공간에 저장해둠으로써 애플리케이션 처리속도를 높이는 방식이다.

Cache를 사용하면 자주 사용되는 리소스가 존재 할시 리소스를 얻은 후 캐시 저장소에 만료시간과 함께 저장하고 사용자가 조회를 요청할 때 마다 만료시간 이전까지는 캐시 저장소에 있는 리소스를 사용함으로써 조회 성능을 대폭 향상 시킬 수 있다.

일반적으로 동일한 리소스 대해 빈번한 SELECT로 발생되는 DBMS 과부하를 줄이고자 사용한다.

스프링은 **캐시 추상화 (Cache Abstraction)**을 통해 편리한 캐싱 기능을 지원하고 있다.

## **📌 Spring Cache Abstraction ?**

[🔗 Spring.io Cache Abstraction](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)

스프링의 캐시 추상화는 캐시 기술에 종속되지 않으며 AOP를 통해 적용되어애플리케이션 코드를 수정하지 않고 캐시 부가기능을 추가할 수 있다.

즉, 사용자가 퍼블릭 인터페이스로 쉽게 캐싱 기능을 사용할 수 있는 것을 말한다.

더 자세히 말하면 캐싱이 필요한 비즈니스 로직에서 EhCache, redis 등 캐싱 인프라스트럭쳐에 의존하지않고 추상화된 퍼블릭 인터페이스로  캐싱을 할 수 있다.

캐시 추상화에서는 캐시 기술을 지원하는 캐시 매니저를 빈으로 등록해야 한다.

캐시 데이터를 ConcurrentHashMap에 저장하는 ConcurrentMapCacheManagerEhCache를 지원하는 EhCacheCacheManager, RedisCacheManager 등 다양한 캐시 매니저가 존재하며 캐싱 전략에 따라 적절한 캐시 매니저를 사용할 수 있다.

## **📌 EhCache 주요 내용**

| 코드 | 설명 |
| --- | --- |
| EhCacheManagerFactoryBean | CacacheManager의 적절한 관리 및 인스턴스를 제공하는데 필요하며 EhCache 설정 리소스를 구성한다.  |
| setConfigLocation | 지정된 경로를 통해 EhCache 설정 리소스를 로드한다. (미지정시 루트의 ehcache.xml파일을 찾음). |
| setShared | CacheManager 싱글톤 여부 (default= false). |
| @EnableCaching | Annotation을 사용하여 캐싱 기능을 이용하겠다고 선언. |

### ****📌 Cache 설정****

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

- Code
    
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd" updateCheck="false">
    <defaultCache
            eternal="false"
            maxElementsInMemory="500"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="0"
            timeToLiveSeconds="300"
            memoryStoreEvictionPolicy="LRU"/>
    
    <!-- 정보 제공할 DB정의 캐시 -->
    <cache
            name="platformTeamBooks"
            eternal="false"
            maxElementsInMemory="500"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="0"
            timeToLiveSeconds="300"
            memoryStoreEvictionPolicy="LRU"/>
    
    </ehcache>
    ```
    

### ****📌 디폴트 캐시 설정****

기본적으로, 선언된 Cache는 CacheManager.add(String cacheName)를 사용하여 적용된다.

이 요소는 옵션이며 CacheManager.add(String cacheName)가 존재하지 않을 때 사용하면 CacheException이 느려진다.

defaultCache에는 예약된 캐시 이름인 암묵적인 이름 "default"가 있으며,

DefaultCache는 ehCache CacheManager의 `addCacheIfAbsent` 에서 호출한다.

하단 코드를 참고하면 등록된 cacheName이 없을 때 이 DefaultCache가 호출되는 것을 알 수 있으며,

존재하지 않을 경우 "**`NO_DEFAULT_CACHE_ERROR_MSG`**“ 가 발생하는 것을 알 수 있다.

- Code
    
    ```java
    // net.sf.ehcache.CacheManager
     
    public class CacheManager {
     
        // ...
     
        public synchronized Ehcache addCacheIfAbsent(final String cacheName) {
            checkStatus ();
     
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
     
        // ...
    }
    ```
    

## @EnableCaching

Spring에서 @Cacheable과 같은 어노테이션 기반의 캐시 기능을 사용하기 위해서는 먼저 별도의 선언이 필요하다.

때문에, @EnableCaching 어노테이션을 설정 클래스에 추가해주어야 한다.

**✔ 사용 예시**

```java
@EnableCaching
@Configuration
public class EhCacheConfiguration { /* ... */}
```

## @Cacheable

👉🏻 적용할 메소드에 @Cacheable 어노테이션을 붙여주면 캐시에 데이터가 없을 경우에는 기존의 로직을 실행한 후에 캐시에 데이터를 추가하고, 캐시에 데이터가 있으면 캐시의 데이터를 반환

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

**✔ 사용 예시**

```java
@Cacheable(cacheNames = "platformTeamBooks", key = "#root.target + #root.methodName + '_'+ #p0", condition = "#user.type == 'ADMIN'")
public Book getPlatformTeamBook(int bookId) { /* ... */}
```

- 참고
    
    [Cacheable](https://docs.spring.io/spring-framework/docs/4.2.4.RELEASE_to_4.2.5.RELEASE/Spring%20Framework%204.2.5.RELEASE/org/springframework/cache/annotation/Cacheable.html)
    
    ### **SpEL**
    
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

👉🏻 메서드가 호출 될 때 저장된 캐시를 삭제해주는 어노테이션

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

**✔ 사용 예시**

```java
@CacheEvict(value = "platformTeamBooks", allEntries = true)
public Book insertPlatformTeamBook(BookVO bookVO) { /* ... */}
```

## **@CachePut**

👉🏻 캐시에 값을 저장하는 용도로만 사용

@Cacheable과 유사하게 실행 결과를 캐시에 저장하지만, 조회 시에 저장된 캐시의 내용을 사용하지는 않고 항상 메소드의 로직을 실행한다.

| @CachePut 속성 | 설명 | Default |
| --- | --- | --- |
| value, cacheName | ehcache.xml 또는 ehcach config에 구성된 cache name. | {} |
| key | 동일한 cache name을 사용하지만 구분될 필요가 있을 경우 사용되는 값, (상수 권장). | "" |
| cacheManager | 사용 할 CacheManager 지정 (EHCacheCacheManager, RedisCacheManager 등) . | "" |
| condition | SpEL 표현식을 작성하여 참일 경우에만 캐싱이 적용됨, (or, and 등 조건식, 논리연산 가능).EX) @Cacheable(key = "testKey", condition="#caching") public Object modifySome(boolean caching) | "" |
| unless | condition과 반대로 참일 경우에만 캐싱이 적용되지 않는다. | "" |

## **@Caching**

👉🏻 하나의 메서드를 호출 할 때 Cacheable, CacheEvict 등 여러 개의 캐싱 동작을 수행해야 할 때 사용

| @Caching 속성 | 설명 | Default |
| --- | --- | --- |
| cacheable[] | 적용 될 @Cacheable array를 등록한다.  | {} |
| evict[] | 적용 될 @CacheEvict array를 등록한다.  | {} |
| put[] | 적용 될 @Cacheput array를 등록한다.  | {} |

## **@CacheConfig**

👉🏻 클래스 단위로 캐시 설정을 동일하게 하고 싶을 때 사용

| @CacheConfig 속성 | 설명 | Default |
| --- | --- | --- |
| cacheNames | 캐시명. | {} |
| cacheManager | 사용 할 CacheManager 지정 (EHCacheCacheManager, RedisCacheManager 등) . |  |
