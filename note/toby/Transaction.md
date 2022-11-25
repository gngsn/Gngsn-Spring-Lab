# Transaction

<br/>

> " 객체 지향 원칙에 충실한 POJO에 엔터프라이즈 서비스를 제공한다. "
>
> 스프링이 처음 등장했을 때의 모토

<br>

EJB가 지원했던 엔터프라이즈 서비스에서 가장 매력적인 것은 바로 **선언적 트랜잭션**이다.

<br/>

<small> <i> * 엔터프라이즈 자바빈즈(Enterprise JavaBeans; EJB) <br/> : 기업환경의 시스템을 구현하기 위한 서버측 컴포넌트 모델. 즉, EJB는 애플리케이션의 업무 로직을 가지고 있는 서버 애플리케이션이다.</i> </small>

<br/>

**선언적 트랙잭션 경계설정**

- 코드 내에서 직접 트랜잭션을 관리하고 트랜잭션 정보를 파라미터로 넘겨 사용하지 않아도 된다.
- 트랜잭션 스크립트 코드 탈피 가능 
  - <details>
    <summary>트랜잭션 스크립트? </summary>
    <i>하나의 트랜잭션 안에서 동작해야 하는 코드를 한 군데 모아서 만드는 방식</i>

    보통 1 트랜잭션 = 1 메소드로 구성되는데 아래와 같은 형식을 가진다
    <br>
    <pre>
    메소드의 앞부분에서 DB를 연결
            ⬇
    트랜잭션을 시작하는 코드
            ⬇
    트랜잭션 안에서 DB를 액세스하는 코드
    혹은 그 결과를 가지고 비즈니스 로직을 적용하는 코드
    </pre>
    <br>
    위와 같은 방식, 분리를 위해서는 Connection정보를 달고 다녀야 함
    </details>
- 의미 있는 작은 단위로 분리되어 있는 데이터 액세스 로직과 비즈니스 로직 컴포넌트와 메소드를 조합해서 하나의 트랜잭션에서 동작하게 만드는 것도 간단
  - 코드의 중복을 제거하고 작은 단위의 컴포넌트로 쪼개서 개발한 후 조합해서 사용 가능

<br/><br/>

## 트랜잭션 추상화와 동기화

스프링은 데이터 액세스 기술과 트랜잭션 서비스 사이의 종속성을 제거하고 스프링이 제공하는 트랜잭션 추상계층을 이용해서 트랜잭션 기능을 활용하도록 만들어준다. -> 트랜잭션 서비스의 종류나 환경이 바뀌더락도 트랜잭션을 사용하는 코드는 그대로 유지 (유연성)

트랜잭션 동기화: 트랜잭션 추상화, 데이터 액세스 기술을 위한 템플릿과 더불어 선언적 트랜잭션을 가능하게 해주는 핵심기능 

<br>

### PlatformTransactionManager

[🔗 Spring.io - PlatformTransactionManager](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html)

*스프링 트랜잭션 추상화의 핵심 인터페이스*

<br>

선언적 트랜잭션을 사용한다면 `PlatformTransactionManager` 인터페이스의 사용법을 몰라도 되지만,
추상화된 인터페이스를 구현한 트랜잭션 서비스 클래스의 종류를 알고 적정란 것을 선택해서 빈으로 등록하는 방법은 알자

<br>

**트랜잭션 경계를 지정**
- 트랜잭션이 어디서 시작하고 종료하는지 결정
- 종료할 때 정상 종료(commit)인지 비정상 종료(rollback)인지 결정
- 그래서 `begin()`이 아니라, **적절한 트랜잭션**을 가져온다고 해서 `getTransaction()` 사용

<br>

모든 스프링의 트랜잭션 기능과 코드는 이 인터페이스를 통해서 로우레벨의 트랜잭션 서비스를 이용할 수 있다.

<br>

``` java
public interface PlatformTransactionManager extends TransactionManager {

	TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
			throws TransactionException;

	void commit(TransactionStatus status) throws TransactionException;

	void rollback(TransactionStatus status) throws TransactionException;

}
```
<br>

`getTransaction()` 메서드의 인자인 `TransactionDefinition` 인터페이스와 반환 타입인 `TransactionStatus` 인터페이스를 살펴보면 아래와 같다

<details>
<summary><b><code>TransactionDefinition</code> : 트랜잭션의 네 가지 속성을 가지고 있다. </b></summary>

``` java
public interface TransactionDefinition {
	int PROPAGATION_REQUIRED = 0;
	int PROPAGATION_SUPPORTS = 1;
	int PROPAGATION_MANDATORY = 2;
	int PROPAGATION_REQUIRES_NEW = 3;
	int PROPAGATION_NOT_SUPPORTED = 4;
	int PROPAGATION_NEVER = 5;
	int PROPAGATION_NESTED = 6;
	int ISOLATION_DEFAULT = -1;
	int ISOLATION_READ_UNCOMMITTED = 1;  // same as java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
	int ISOLATION_READ_COMMITTED = 2;  // same as java.sql.Connection.TRANSACTION_READ_COMMITTED;
	int ISOLATION_REPEATABLE_READ = 4;  // same as java.sql.Connection.TRANSACTION_REPEATABLE_READ;
	int ISOLATION_SERIALIZABLE = 8;  // same as java.sql.Connection.TRANSACTION_SERIALIZABLE;
	int TIMEOUT_DEFAULT = -1;

	default int getPropagationBehavior() {
		return PROPAGATION_REQUIRED;
	}

	default int getIsolationLevel() {
		return ISOLATION_DEFAULT;
	}

	default int getTimeout() {
		return TIMEOUT_DEFAULT;
	}

	default boolean isReadOnly() {
		return false;
	}

	@Nullable
	default String getName() {
		return null;
	}

	static TransactionDefinition withDefaults() {
		return StaticTransactionDefinition.INSTANCE;
	}
}

```
</details>

<details>
<summary><b><code>TransactionStatus</code> : 현재 참여중인 트랜잭션의 ID, 구분 정보를 담고 커밋 또는 롤백 시 사용</b></summary>

``` java
public interface TransactionStatus extends TransactionExecution, SavepointManager, Flushable {

	boolean hasSavepoint();

	@Override
	void flush();
}

```
</details>

<br/><br/>

### 트랜잭션 매니저의 종류

<details>
<summary><b>✔️ DataSourceTransactionManager</b></summary>

*Connection의 트랜잭션 API를 이용해서 트랜잭션을 관리해주는 트랜잭션 매니저*

이 트랜잭션 매니저를 사용하려면 **트랜잭션을 적용할 DataSource가 스프링의 빈으로 등록**되어야 한다.

JDBC API를 이용해서 트랜잭션을 관리하는 데이터 액세스 기술인 JDBC와 iBatis SqlMap으로 만든 DAO에 적용할 수 있다.

DataSourceTransactionManager를 빈으로 등록할 때는 적용할 DAO가 사용하는 것과 동일한 DataSource를 빈으로 제공해야 한다.

DataSourceTransactionManager가 사용할 DataSource는 getConnection()이 호출될 때마다 매번 새로운 Connection을 돌려줘야 한다. ThreadLocal 등을 이용해 트랜잭션을 저장해두고 돌려주는 특별한 기능을 가진 DataSource를 사용하면 안된다. 

트랜잭션 매니저가 관리하는 Connection을 가져오려면 DataSource의 getConnection() 대신 스프링 DataSourceUtils 클래스의 스태틱 메소드를 사용한다. 

대개 JdbcTemplate 이용해서 DAO를 작성하면 되기 때문에 DataSourceUtils.getConnection()으로 Connection을 직접 가져와 사용할 일은 많지 않다.

<b>JdbcTemplate이나 SqlMapClientTemplate처럼 내부에서 Connection과 트랜잭션 작업을 알아서 처리해주는 템플릿을 사용하는 방법이 제일 좋다.</b>
<br>
</details>

<details>
<summary><b>✔️ JpaTransactionManager</b></summary>

*JPA를 이용하는 DAO에서 사용하는 트랜잭션 매니저*

물론 JTA에서는 필요없음

JpaTransactionManager는 `LocalContainerEntityManagerFactoryBean`타입의 빈을 등록해줘야한다.
DataSourceTransactionManager가 제공하는 **DataSource 레벨의 트랜잭션 관리 기능을 동시**에 하기 때문에
JpaTransactionManager를 사용하는 동시에 트랜잭션이 적용된 JDBC DAO를 사용할 수도 있다.
<br>
</details>
<details>
<summary><b>✔️ HibernateTransactionManager</b></summary>

*Hibernate를 이용하는 DAO에서 사용하는 트랜잭션 매니저*

DataSourceTransactionManager가 제공하는 **DataSource 레벨의 트랜잭션 관리 기능을 동시**에 하기 때문에
HibernateTransactionManager를 사용하는 동시에 트랜잭션이 적용된 JDBC DAO를 사용할 수도 있다.
<br>
</details>

<details>
<summary><b>✔️ JtaTransactionManager</b></summary>

*하나 이상의 DB 또는 트랜잭션 리소스가 참여하는 글로벌 트랜잭션을 적용하려면 JTA 이용*

JTA는 여러 개의 트랜잭션 리소스(DB, JMS 등)에 대한 작업을 하나의 트랜잭션으로 묶을 수 있고,
여러 대의 서버에 분산되어 진행되는 작업을 트랜잭션으로 연결해주기도 한다.
JTA 트랜잭션을 이용하려면 트랜잭션 서비스를 제공하는 WAS를 이용하거나 독립 JTA 서비스를 제공해주는 프레임워크를 사용해야 한다.

JraTransactionManager를 사용할 때는 DataSource도 서버에 등록된 XADataSource를 사용해야 한다.
JNDI를 이용해서 서버의 XA DataSource를 빈으로 등록하고 DAO나 EntityManagerFactory, SessionFactory 등에서 사용하게 해야 한다.
<br>
</details>
✔️ JmsTransactionManager, CciTransactionManager

<br>

DB가 하나라면 트랜잭션 매니저 또한 하나만 등록돼야 하고,

여러개라도 JTA를 이용한 글로벌 트랙잭션을 적용할것이라면 JtaTransactionManager 하나만 등록돼야 한다.

독립된 두 개 이상의 DB는 DataSource도 두 개가 등록되어 DB 수에 따른 트랜잭션 매니저를 등록할 수 있다.

<br/>

---

<br/>

## 트랜잭션 경계설정 전략

트랜잭션의 시작과 종료가 되는 경계는 보통 서비스 계층 오브젝트의 메소드다.

비즈니스 로직이 거의 없어 서비스 계층과 데이터 액세스 계층을 통합했다면, 통합된 계층의 메소드가 트랜잭션 경계가 될 것이다.

<br/>

**트랜잭션 경계 설정 방법**
- 코드에 의한 프로그램적인 방법 (트랜잭션을 다루는 코드를 직접 만듦)
- AOP를 이용한 선언적인 방법

<br/>

### 경계 설정 : 코드

<small> 실제로 많이 사용되지는 않지만, 테스트 코드에서 유용 </small>

스프링의 트랜잭션 매니저는 모두 `PlatformTransactionManager`를 구현

트랜잭션을 처리하기 위해 PlatformTransactionManager의 메소드를 직접 사용해도 되지만, 

예외가 발생하면 트랜잭션 롤백해줘야 하기 때문에 try/catch 블록을 써야 한다.

`PlatformTransactionManager`의 메소드를 직접 사용하지 말고 템플릿/콜백 방식의 `TransactionTemplate`을 사용하면 편리하다.

``` java
public class TestService {
    TransactionTemplate transactionTemplate;

    MemberDAO memberDAO;

    void init(PlatformTransactionManager transactionManager) {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Member> members = new ArrayList<>();
        this.transactionTemplate.execute(status ->
                members.stream().peek(member -> memberDAO.add(member))
        );
    }
}

```

-> 트랜잭션의 기본 속성을 변경하려면 TransactionTemplate을 만들 때 TransactionDefinition 오브젝트를 만들어서 파라미터로 제공해주면 된다.

<br/>

코드에 의한 방법을 사용하지 않아도 `PlatformTransactionManager`를 통한 트랙잰션 추상화에 대해서는 잘 이해하고 있어야한다. 

선언적 트랜잭션은 트랜잭션이 시작되고 적용되는 코드가 눈에 보이지 않기 때문에 자칫 실수하면 트랜잭션 저굥ㅇ과 관련된 찾기 힘든 버그를 만날 수 있다. 

이때 아래와 같이 트랜잭션을 직접 코드에서 가져와 확인 및 제어로 트랜잭션 적용 상태를 분석해볼 수 있다.

어디서든 `PlatformTransactionManager`를 DI 받아서 getTransaction()를 통해 현재 진행중인 트랜잭션을 확인할 수 있다.

두 개의 DAO나 서비스 계층 코드가 같은 트랜잭션 안에서 동작하고 있는지도 확인해볼 수 있다.

<br/><br/>

### 경계 설정 : 선언적 트랜잭션 경계설정

선언적 트랜잭션을 이용하면 코드에는 전혀 영향을 주지 않으면서 특정 메소드 실행 전후에 트랜잭션이 시작되고 종료되거나 기존 트랜잭션에 참여하도록 만들 수 있다.

데코레이터 패턴을 적용한 트랜잭션 프록시 빈을 사용해야 한다.

선언적 트랜잭션 경계설정은 트랜잭션 **프록시 빈** 덕분에 가능한 것이다. 

트랜잭션은 대부분 성격이 비슷하다. 때문에 적용 대상마다 일일이 선언해주기보다는 일괄적으로 선언하는 것이 편리하다.

그래서 간단한 설정으로 특정 부가기능을 임의의 타깃 오브젝트에 부여해줄 수 있는 프록시 AOP를 주로 활용한다.

<br/>

### ✔️ AOP

트랜잭션 경계설정 부가기능을 사용할 어드바이스의 정의와 부여 대상인 포인트컷을 선정하여 어드바이저를 사용할 수 있다.

AOP의 어드바이스가 적용되는 위치인 조인 포인트는 메소드이다.

**포인트컷은 원한다면 메소드 단위까지 상세하게 지정할 수 있고, 기본적으로 인터페이스에 적용된다는 사실도 기억하자.**

인터페이스를 적용하는 이유는 AOP의 동작원리인 JDK 다이내믹 프록시는 인터페이스를 이용해 프록시를 만들기 때문이다. 

인터페이스 없이 설정을 통해 등록된 빈에도 AOP를 적용할 수 있다. 하지만 AOP의 타깃은 가능한 한 인터페이스를 사용할 것을 권장한다. 

-> 트랜잭션을 반영하고 싶은 범위나 메소드가 있을 텐데, 하나하나 설정해주기에는 불필요한 수정자나 내부에서 사용할 메소드(가령 private 메소드같은)까지 트랜잭션이 적용되면
쓸데없는 트랜잭션 경계설정 작업을 수행하느라 그만큼 시간과 리소스를 소모하게 된다.

<br/>

### ✔️ @Transactional

이 접근 방법에선 설정파일에 명시적으로 포인트컷과 어드바이스를 정의하지 않는다.

트랜잭션이 적용될 타깃 인터페이스나 클래스, 메소드 등에 @Transactional 애노테이션을 부여해서 트랜잭션 대상으로 지정하고 트랜잭션의 속성을 제공한다.

Transactional Annotation은 Target이 `TYPE`과 `METHOD` 다. 
즉, `TYPE` - Class, interface, enum 이나, `METHOD` - Method 에 적용할 수 있다는 말이다.

혹은 동시에 붙일 수 있는데, 만약 아래와 같이 동시에 붙으면 메소드에 붙은 애노테이션이 우선시되어 적용된다.

<br/>

``` java
@Transactional
public interface MemberAnnotService {
    @Transactional(readOnly=true)
    void readMember(long memberId);

    void addMember(List<Member> members);
}
```
<small>`readOnly=true` Option: true 시 insert, update, delete 실행 시 예외 발생</small>
<br/>

@Transactional 적용 대상은 미리 결정하고 애플리케이션 안에서 통일하는 게 좋다.
타입 레벨과 메소드 레벨에 동시에 @Transactional이 혼용되어 적용되는 건 바람직하지 못하다.

<br/><br/>

### AOP VS @Transactional

이 두 방법은 각각 장단점이 있다. 
AOP는 조금 복잡해보이지만 코드에는 영향을 주지 않고 일괄적으로 트랜잭션을 적용하거나 변경할 수 있다는 장점이 있다.
@Transactional는 일일이 대상에 부여하여 번거롭지만, 훨씬 세밀한 설정이 가능하다.

<br/><br/>

### 프록시 모드

스프링의 AOP는 기본적으로 다이내믹 프록시 기법을 이용해 동작한다. 다이내믹 프록시를 적용하려면 인터페이스가 있어야 한다.
인터페이스의 사용은 DI에서도 가장 기본 원칙인 만큼 문제 될 것 없지만 클래스에 사용할 수 밖에 없는 경우에는 클래스 프록시 모드를 사용할 수 있다. 

프록시는 기능을 사용하려는 오브젝트인 클라이언트와 서비스를 제공하는 오브젝트인 타깃 오브젝트 사이에 데코레이터 패턴을 써서 투명하게 추가된다. 
투명하다는 건, 프록시가 추가되더라도 클라이언트와 타깃 오브젝트의 코드를 수정할 필요도 없고 기본 기능에도 영향을 주지 않는다는 뜻이다.

프록시가 적용되면 클라이언트는 프록시를 타깃 오브젝트라고 생각하고 프록시의 메소드를 호출한다. 프록시는 클라이언트로부터 요청을 받으면 타깃 오브젝트의 메소드로 위임해준다. 타깃 오브젝트에 위임하는 과정에서 부가 작업을 추가할 수 있다. 트랜잭션 AOP에 의해 추가된 프록시하면 타깃 오브젝트 메소드 호출 전에 트랜잭션을 시작하고 호출 후에 트랜잭션을 커밋하거나 롤백해줄 것이다.

여기서 프록시는 클라이언트가 타깃 오브젝트를 호출하는 과정에서만 동작한다는 점을 주목하자.

타깃 오브젝트의 메소드가 자기 자신의 다른 메소드를 호출할 때는 어떻게 될까? 이때도 프록시가 동작할까? 
그렇지 않다. 이미 프록시를 거쳐서 타깃 오브젝트 까지 작업이 진행됐으므로 타깃 오브젝트에서 자신의 메소드를 호출할 때는 프록시를 거치지 않는다. 
클라이언트에서 프록시를 통해 들어온 호출은 당연히 프록시의 기능이 동작한다. 
타깃 오브젝트 안에서의 호출 이므로 프록시를 통하지 않고 직접 타깃 오브젝트의 메소드로 호출이 일어난다.

만약, 이것이 문제가 된다면 AspectJ AOP 사용을 고려해볼 수 있다.
<small>프록시 대신 클래스 바이트코드를 직접 변경해서 부가기능을 수행하기 때문에 위의 문제를 해결된다. </small>


<br/><br/>

## 트랜잭션 속성

트랜잭션의 경계를 설정할 때 네 가지 트랜잭션 속성을 지정할 수 있다.

또, 선언적 트랜잭션에서는 롤백과 커밋의 기준을 변경하기 위해 두 가지 추가 속성을 지정할 수 있다.
즉, 선언적 트랜잭션은 여섯가지의 속성을 가진다고 볼 수 있는 것이다.

``` java 
@Transactional(
	readOnly=...,
	isolation=...,
	propagation=...,
	timeout=...,
	rollbackFor=..., rollbackForClassName=...,
	noRollbackFor=..., noRollbackForClassName=...
)
```

<br/><br/>

### 📌 Propagation
***트랜잭션 전파***

<br/>

트랜잭션을 시작하거나 기존 트랜잭션에 참여하는 방법을 결정하는 속성이다.

선언적 트랜잭션 방식의 장점은 여러 트랜잭션 적용 범위를 묶어서 커다란 트랜잭션 경계를 만들 수 있다.

그렇다면 어떤 속성을 지정할 수 있는지 알아보자.

<br/>

<details>
<summary>✔️ REQUIRED - (Default) 이미 시작된 트랜잭션이 있으면 참여하고, 없으면 새로 생성해 시작한다 </summary>
Default. 모든 트랜잭션 매니저가 지원하며, 대개 이 속성이면 충분.
미리 시작된 트랜잭션이 있으면 참여하고, 없으면 새로 시작.

자연스럽고 간단한 트랜잭션 전파 바이식이지만 사용해보면 매우 강력하고 유용하다.

하나의 트랜잭션이 시작된 후 다른 트랜잭션 경계가 설정된 메소드를 호출하면 자연스럽게 같은 트랜잭션으로 묶인다.

</details>

<details>
<summary>✔️ SUPPORT - 이미 시작된 트랜잭션이 있으면 참여하고, 없으면 트랜잭션 없이 진행한다 </summary>
 트랜잭션이 없지만 해당 경계 안에서 Connection이나 하이버네이트 Session을 공유할 수 있다.

</details>

<details>
<summary>✔️ MANDATORY - 이미 시작된 트랜잭션이 있으면 참여하고, 없으면 예외를 발생한다 </summary>
혼자서는 독립적으로 트랜잭션을 진행하면 안되는 경우에 사용한다.
</details>

✔️ REQUIRES_NEW - 항상 새로운 트랜잭션을 시작하고, 이미 시작된 트랜잭션이 있으면 보류하고 생성한다

✔️ NOT_SUPPORTED - 트랜잭션을 전혀 사용하지 않고, 이미 시작된 트랜잭션이 있으면 보류한다

✔️ NEVER - 강제로 트랜잭션을 사용하지 않고, 이미 시작된 트랜잭션이 있으면 예외를 발생한다

✔️ NESTED - 이미 시작된 트랜잭션이 있으면 중첩 트랜잭션을 시작하고, 부모 트랜잭션에 영향을 주지 않는다

<details>
<summary>✔️ NESTED - 이미 시작된 트랜잭션이 있으면 중첩 트랜잭션을 시작하고,부모 트랜잭션에 영향을 주지 않는다 </summary>
중첩 트랜잭션이라 트랜잭션 안에 트랜잭션을 만든다.
먼저 시작된 부모 트랜잭션의 커밋과 롤백에는 영향을 받지만 자신의 커밋과 롤백은 부모 트랜잭션에 영향을 주지 않는다.

예를 들어 로그 파일을 저장하는 로직은 메인 로직의 중첩 트랜잭션으로 진행 될 수 있는데,
로그 저장 로직에 문제가 있어도 메인 로직은 실행이 되어야 하기 때문에 영향을 주면 안되지만
메인 로직에 문제가 있어서 롤백을 하면 로그 저장 로직에도 영향을 줄 수 있어야 한다.
</details>

<br/><br/>


**AOP를 통한 propagation 설정 방법**

<br/>

``` java
	TransactionInterceptor txAdvice = new TransactionInterceptor();
	Properties txAttrProperty = new Properties();

	// case 1
	DefaultTransactionAttribute defaultAttribute = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
	// or
	DefaultTransactionAttribute defaultAttribute = new DefaultTransactionAttribute();
	masterAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	// case 2

	String transactionAttributesDefinition = defaultAttribute.toString();

    txAttributes.setProperty("*", transactionAttributesDefinition);
	txAdvice.setTransactionAttributes(txAttributes);
```


<br/>

**@Transactional를 통한 propagation 설정 방법**

``` java
public interface MemberService {
    @Transactional(propagation = Propagation.PROPAGATION_REQUIRED)
    void addMember(List<Member> members);
}
```

<br/><br/>

### 📌 Isolation
***트랜잭션 격리 수준***

<br/>

트랜잭션 격리 수준은 동시에 여러 트랜잭션이 진행될 때에 트랜잭션의 작업 결과를 다른 트랜잭션에게 어떻게 노출할 것인지를 결정하는 기준이다.

<details>
<summary>✔️ DEFAULT - 데이터 액세스 기술 또는 DB의 디폴트 설정을 따름</summary>
대부분의 DB는 READ_COMMITTED를 기본 격리 수준으로 갖지만, 다를수도 있기 때문에 DB와 드라이버 문서를 살펴봐야한다.
</details>
<details>
<summary>✔️ READ_UNCOMMITTED (level 0) - 커밋되지 않는 데이터에 대한 읽기 허용</summary>
- <b>Dirty Read</b>가 발생 가능
<br><br>
가장 낮은 격리 수준이다. 하나의 트랜잭션이 커밋되기 전에 그 변화가 다른 트랜잭션에 그대로 노출되는 문제가 있다.

하지만 가장 빠르기 때문에 데이터의 일관성이 조금 떨어지더라도 성능을 극대화할 때 의도적으로 사용할 수 있다. 
</details>
<details>
<summary>✔️ READ_COMMITTED (level 1) - 커밋된 데이터에 대한 읽기 허용</summary>
-<b>Dirty Read</b> 방지
<br><br>
가장 많이 사용되는 격리 수준이다.
커밋되지 않은 정보는 읽을 수 없다. 대신 하나의 트랜잭션이 읽은 로우를 다른 트랜잭션이 수정할 수 있다.
이 때문에 처음 트랜잭션이 같은 로우를 다시 읽었을 때 다른 내용일 수 있다.
</details>

<details>
<summary>✔️ REPEATABLE_READ (level 2) - 동일 필드에 대해 다중 접근 시 모두 동일한 결과를 보장</summary>
- <b>Non-Repeatable Read</b> 방지
<br><br>
하나의 트랜잭션이 읽은 로우를 다른 트랜잭션이 수정하는 것을 막아준다. 하지만 새로운 로우를 추가하는 것은 제한하지 않는다.
따라서 SELECT로 조건에 맞는 로우를 전부 가져오는 경우 트랜잭션이 끝나기 전에 새로 추가된 로우가 발견될 수 있다.
</details>
<details>
<summary>✔️ SERIALIZABLE (level 3) - 동일 필드에 대해 다중 접근 시 모두 동일한 결과를 보장</summary>
- <b>Phantom Read</b> 방지
<br><br>
데이터의 일관성 및 동시성을 위해 MVCC(Multi Version Concurrency Control)을 사용하지 않는다.
(MVCC: 다중 사용자 데이터베이스 성능을 위한 기술로 데이터 조회 시 LOCK을 사용하지 않고 데이터의 버전을 관리해 데이터의 일관성 및 동시성을 높이는 기술)
트랜잭션이 완료될 때까지 SELECT 문장이 사용하는 모든 데이터에 shared lock이 걸리므로 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정 및 입력이 불가능하다.

가장 안전한 격리수준이지만 가장 성능이 떨어지기 때문에 극단적으로 안전한 작업이 필요한 경우가 아니라면 자주 사용되지 않는다.
</details>

<br/>

Isolation은 아래와 같이 설정할 수 있습니다.

**AOP를 통한 propagation 설정 방법**

``` java
	DefaultTransactionAttribute defaultAttribute = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
	masterAttribute.setIsolationLevel(1);
```

<br/>

**@Transactional를 통한 propagation 설정 방법**

``` java
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
```

<br/><br/>

### 📌 Timeout
***트랜잭션 제한시간***

**초 단위**로 제한 시간을 지정할 수 있다.
디폴트는 시스템의 제한 시간을 따르는 것이다. 
직접 지정하는 경우 이 기능을 지원하지 못하는 일부 트랜잭션 매니저는 예외를 발생할 수 있다.

<br/><br/>

### 📌 ReadOnly
***트랜잭션 읽기 전용***

트랜잭션을 읽기 전용으로 설정할 수 있다.
성능을 최적화하기 위해 사용할 수도 있고, 특정 트랜잭션 작업 안에서 쓰기 작업이 일어나는 것을 의도적으로 방지하기 위해 사용할 수도 있다.
하지만 일부 트랜잭션 매니저의 경우 읽기 전용 속성을 무시할 수도 있으니 주의해야 한다.
일반적으로 INSERT, UPDATE, DELETE 같은 쓰기 작업이 진행되면 예외가 발생한다.

<br/><br/>

### 📌 RollbackFor, RollbackForClassName
***트랜잭션 롤백 대상 지정***

선언적 트랜잭션에서는 런타임 예외가 발생하면 롤백한다.
예외가 발생하지 않거나 **체크 예외가 발생하면 커밋**한다.

체크 예외를 커밋 대상으로 삼는 이유는 체크예외가 예외적인 상황에서 사용되기 보다는, 리턴값을 대신해서 비즈니스 적인 의미를 담은 결과를 돌려주는 용도로 많이 사용되기 때문이다.
스프링에서는 데이터 액세스 기술의 예외는 런타임 예외로 전화돼서 던져지므로 런타임 예외만 롤백 대상으로 삼은 것이다.

하지만 원한다면 기본 동작장식을 바꿀 수 있다. 체크 예외지만 롤백 대상으로 삼아햐 하는 것이 있다면 `rollbackFor`이나 `rollbackForClassName`로 설정할 수 있다.

**AOP를 통한 propagation 설정 방법**

``` java 
RuleBasedTransactionAttribute masterAttribute = new RuleBasedTransactionAttribute();
masterAttribute.setRollbackRules(rollbackRules);
```

<br/>

**@Transactional를 통한 propagation 설정 방법**

``` java 
@Transactional(rollbackFor = Exception.class)
```

<br/><br/>

### 📌 noRollbackFor, noRollbackForClassName
***트랜잭션 롤백 예외 대상 지정***

기본적으로 롤백 대상인 런타임 예외를 트랙잭션 커밋 대상으로 지정해준다.
사용법은 위와 동일하다.


<br/><br/>

## 데이터 액세스 기술 트랜잭션의 통합

<br/><br/>

## JTA를 이용한 글로벌/분산 트랜잭션
