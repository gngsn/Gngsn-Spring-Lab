# JPA
_Jakarta/Java Persistence API, 자바 진영의 ORM 기술 표준_


### SQL을 직접 다룰 때의 문제점

- 진정한 의미의 계층 분할이 어렵다.
- 엔티티를 신뢰할 수 없다.
- SQL에 의존적인 개발을 피하기 어렵다.


### JPA Dialect
JPA는 특정 데이터베이스에 종속적이지 않은 기술이기 때문에 다른 데이터베이스로 손쉽게 교체할 수 있다
SQL 표준을 지키지 않거나 **특정 데이터베이스 만의 고유한 기능**을 JPA에서는 방언(Dialect)이라 한다.

### Entity Manager Factory

JPA를 시작하려면 우선 `persistence.xml`의 설정 정보를 사용해서 엔티티 매니저 팩토리를 생성해야 한다.
이때 Persistence 클래스를 사용하는데 이 클래스는 엔티티 매니저 팩토리를 생성해서 JPA를 사용할 수 있게 준비한다.

```java
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpabook");
```

### Entity Manager

```java
EntityManger entityManger = entityManagerFactory.createEntityManager();
```

엔티티 매니저를 사용해서 엔티티를 데이터베이스에 등록/수정/삭제/조회할 수 있다. 
참고로 엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계가 있으므로 스레드간에 공유하거나 재사용하면 안 된다.


### Transaction Management

JPA를 사용하면 항상 트랜잭션 안에서 데이터를 변경해야 한다. 트랜잭션 없이 데이터를 변경하면 예외가 발생한다.

### JPQL

하나 이상의 회원 목록을 조회하는 다음 코드를 살펴보자

```java
//목록 조회
TypedQuery<Member> query = entityManager.createQuery("select m from Member m", Member.class);
List<Member> members = query.getResultList();
```

JPQL은 엔티티 객체를 대상으로 쿼리한다. 쉽게 이야기해서 클래스와 필드를 대상으로 쿼리한다.
SQL은 데이터베이스 테이블을 대상으로 쿼리한다.
JPQL은 데이터베이스 테이블을 전혀 알지 못하고 엔티티 객체를 참조한다.


## Persistence Context

영속성 컨텍스트란 엔티티를 영구 저장하는 환경이다.
엔티티 매니저로 엔티티를 저장하거나 조회하면 엔티티 매니저는 영속성 컨텍스트에 엔티티를 보관하고 관리한다.

```java
entityManager.persist(member);
```

영속성 컨텍스트는 엔티티 매니저를 생성할 때 하나 만들어진다.



