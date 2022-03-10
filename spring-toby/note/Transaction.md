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

### 코드 - 경계 설정

스프링의 트랜잭션 매니저는 모두 `PlatformTransactionManager`를 구현

트랜잭션을 처리하기 위해 PlatformTransactionManager의 메소드를 직접 사용해도 되지만, 

예외가 발생하면 트랜잭션 롤백해줘야 하기 때문에 try/catch 블록을 써야 함

PlatformTransactionManager의 메소드를 직접 사용하지 말고 템플릿/콜백 방식의 `TransactionTemplate`을 사용하면 편리함

``` java
public class TestService {
    TransactionTemplate transactionTemplate;

    MemberDAO memberDAO;

    void init (PlatformTransactionManager transactionManager) {
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

<br/><br/>

## 트랜잭션 속성

<br/><br/>

## 데이터 액세스 기술 트랜잭션의 통합

<br/><br/>

## JTA를 이용한 글로벌/분산 트랜잭션
