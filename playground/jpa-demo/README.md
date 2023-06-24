# JPA
_Jakarta/Java Persistence API, 자바 진영의 ORM 기술 표준_

### SQL을 직접 다룰 때의 문제점

##### 반복되는 작업

1. 회원 조회용 SQL을 작성한다.
`SELECT MEMBER_ID, NAME FROM MEMBER M WHERE MEMBER_ID = ?`
2. JDBC API를 사용해서 SQL을 실행한다.
`ResultSet rs = stmt.executeQuery(sql);`
3. 조회 결과를 Member 객체로 매핑한다.

##### SQL에 의존적인 개발
 
물리 적으로 SQL과 JDBC API를 데이터 접근 계층에 숨기는 데 성공했을지는 몰라도 논리적으로는 엔티티와 아주 강한 의존관계를 가지고 있다. 이런 강한 의존관계 때 문에 회원을 조회할 때는 물론이고 회원 객체에 필드를 하나 추가할 때도 DAO 의 CRUD 코드와 SQL 대부분을 변경해야 하는 문제가 발생한다.

애플리케이션에서 SQL을 직접 다룰 때 발생하는 문제점을 요약하면 다음과 같다.

- 진정한 의미의 계층 분할이 어렵다.
   - DAO를 열어서 어떤 SQL이 실행되고 어떤 객체들이 함께 조회되는지 일일이 확인해야 함
- 엔티티를 신뢰할 수 없다.
  - 조회 컬럼을 누락시킬 때 null 이 발생해도 알기 어려움 
- SQL에 의존적인 개발을 피하기 어렵다. 
  - 데이터베이스는 객체 구조와는 다른 데이터 중심의 구조를 가지므로 객체를 데이터베이스에 직접 저장하거나 조회할 수는 없음
  - 따라서 개발자가 객체지향 애플리케이션과 데이터베이스 중간에서 SQL과 JDBC API를 사용해서 변환 작업을 직접 해주어야 함

<br/>


## JPA

#### ORM object-Relational Mapping
- Java Persistence API (≤ version 2.2)
- Jakarta EE 9 (JCP → 이클립스 재단으로 이관,  ≤ JPA version 3.0)
- Spring 6 버전 부터 Jakarta EE 9+ 지원

#### JPA 특징

- Annotation 기반의 매핑 설정
- XML 파일을 이용한 매핑
- String, int LocalDate 등 기본 타입 지원 뿐만 아니라, Java Class 에 대한 매핑 가능
- Relationship 설정: 1:1, 1:N, N:1, N:M

#### 상속에 대한 매핑 지원

``` sql
CREATE USER 'jpauser'@'localhost' IDENTIFIED BY 'test1234!'
CREATE USER 'jpauser'@'%' IDENTIFIED BY 'test1234!'

GRANT ALL PRIVILEGES ON jpademo.* TO 'jpauser'@'localhost';
GRANT ALL PRIVILEGES ON jpademo.* TO 'jpauser'@'%';
```

```sql
CREATE TABLE jpademo.user (
email carchar(50) not null primary key,
name varchar (50),
create_date datetime
) engine innodb character set utf8mb4;
```

- JPA: API의 모음으로, 일종의 스펙 및 표준.
- Hibernate: 스펙을 실제로 구현한 구현체 중 하나

<br/><br/>

### JPA Dialect
- JPA는 특정 데이터베이스에 종속적이지 않은 기술이기 때문에 다른 데이터베이스로 손쉽게 교체할 수 있다
- SQL 표준을 지키지 않거나 **특정 데이터베이스 만의 고유한 기능**을 JPA에서는 방언(Dialect)이라 한다.

<br/><br/>

### Entity Manager Factory

JPA를 시작하려면 우선 `persistence.xml`의 설정 정보를 사용해서 엔티티 매니저 팩토리를 생성해야 한다.
이때 Persistence 클래스를 사용하는데 이 클래스는 엔티티 매니저 팩토리를 생성해서 JPA를 사용할 수 있게 준비한다.

```java
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpabook");
```

- EntityManagerFactory는 영속단위 기준으로 생성됨
- persistence.xml 파일의 `<persistence-unit name="{persistent-name}"> ...` 에서 persistent-name 이 영속 단위 기준으로 초기화됨


- 변경 내용을 적용할 때에는 해당 내용을 잘 보관하고 있다가 트랜잭션을 커밋하는 시점에 변경된 내용을 찾아서 실제 업데이트 쿼리를 실행. -> 영속 컨텍스트 덕분
- 영속 컨텍스트
  - DB에서 읽어온 객체, 또는 운영 프로그램에서 저장한 객체 (EntityManager를 통해 저장한 객체) 또는  통해 읽어온 객체 등 매핑되는 객체를 저장하고 있는 일종의 메모리 공간.
  - 하는 일: 영속 객체를 보관하다가 커밋하는 시점에 변경이 발생했는지를 판단하고 DB 반영.

<br/><br/>

### Entity Manager

```java
EntityManger entityManger = entityManagerFactory.createEntityManager();
```

엔티티 매니저를 사용해서 엔티티를 데이터베이스에 등록/수정/삭제/조회할 수 있다. 
참고로 엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계가 있으므로 스레드간에 공유하거나 재사용하면 안 된다.


- EntityManager를 사용해서 Entity 단위로 CRUD 처리
- 변경은 Transaction 범위 내에서 실행

#### 1. persist()

#### 2. find()

```
String str = em.find(String.class, "1");
User user = em.find(User.class, 11);
```

- EntityType 에 따라 ID Type이 일치해야 함
- 일치하지 않으면 Exception 발생

#### 3. remove()

- `find()`로 불러온 Entity 객체를 전달해야 삭제가 되며, 없다면 오류가 발생, 또한 Transaction 범위 밖에서 remove에 전달하면 오류가 발생
- 또한, `em.remove(user);`를 진행하고 `tx.commit()`을 하기 전인데 다른 프로세스가 데이터를 삭제하면 익셉션 발생

#### 4. merge()

<br/><br/>

### Transaction Management

JPA를 사용하면 항상 트랜잭션 안에서 데이터를 변경해야 한다. 트랜잭션 없이 데이터를 변경하면 예외가 발생한다.

<br/><br/>

### JPQL

하나 이상의 회원 목록을 조회하는 다음 코드를 살펴보자

```java
//목록 조회
TypedQuery<Member> query = entityManager.createQuery("select m from Member m", Member.class);
List<Member> members = query.getResultList();
```

- JPQL은 엔티티 객체를 대상으로 쿼리한다. 쉽게 이야기해서 클래스와 필드를 대상으로 쿼리한다.
- SQL은 데이터베이스 테이블을 대상으로 쿼리한다.
- JPQL은 데이터베이스 테이블을 전혀 알지 못하고 엔티티 객체를 참조한다.


<br/><br/>

## Persistence Context

영속성 컨텍스트란 엔티티를 영구 저장하는 환경이다.
엔티티 매니저로 엔티티를 저장하거나 조회하면 엔티티 매니저는 영속성 컨텍스트에 엔티티를 보관하고 관리한다.

```java
entityManager.persist(member);
```

영속성 컨텍스트는 엔티티 매니저를 생성할 때 하나 만들어진다.




### Annotation
- `@Entity`: 엔티티 클래스 설정, 필수
- `@Table`: 매핑 테이블 지정
- `@Id`: 식별자 속성에 설정, 필수
- `@Column`: Mapping할 Column 명 지정, 지정하지 않으면 필드명/프로퍼티명 사용
- `@Enumerated` (Enum)
- `@Temporal`: `java.util.Date`, `java.util.Calendar` 매핑 (Java8 이후부턴 잘 사용하지 않음)


### @Table

- 생략 시 클래스 이름과 동일한 이름에 매핑

**속성**
- `name`: Table name
- `catalog`: 카탈로그 이름 (ex. MySQL DB 명)
- `schema`: 스키마 이름 (ex. Oracle Schema 이름)

### @Enumerated
- `EnumType.STRING`: enum 타입 값 이름을 저장, 문자열 타입 칼럼 매핑
- `EnumType.ORDINAL`: default. enum 타입 값의 순서 저장, 숫자 타입 칼럼에 매핑

```java
public enum Grade {
S1, S2, S3, S4, S5, S6, S7
}
```

#### EnumType.STRING → "S1"

```java
Grade.S1.name()
```

#### EnumType.ORDINAL → 0

```java
Grade.S1.ordinal()
```


## Entity 식별 방식

<small>
1. 직접 할당
2. 식별 칼럼 방식
3. 시퀀스 사용 방식
4. 테이블 저장 방식
</small>

#### GenerationType.IDENTITY

- `@GeneratedValue(strategy = @GenerationType.IDENTITY)`
- DB 식별 칼럼에 매핑 (ex. MySQL 자동 증가 칼럼)
- DB가 식별자를 생성하므로 객체 생성시에 식별값을 설정하지 않음
- INSERT Query 실행 후 사용
- `EntityManager#persist()` 호출 시점에 INSERT 쿼리 실행
- `persist()` 실행할 때 객체에 식별자 값 할당


#### 시퀀스 사용 방식

- Sequence를 사용해서 식별자 생성
- JPA가 식별자 생성 처리 → 객체 생성시에 식별 값을 설정하지 않음

**설정 방식**
- `@SequenceGenerator`로 시퀀스 생성기 설정
- `@GeneratedValue`의 generator로 시퀀스 생성기 지정
- `EntityManager#persist()` 호출 시점에 시퀀스 사용
- `persist()` 실행할 때 객체에 식별자 값 할당됨
- INSERT 쿼리는 실행하지 않음

```java
@Id
@SequenceGenerator(
name = "log_seq_gen",
sequenceName = "activity_seq",
schema = "crm",
allocationSize = 1
)
@GeneratedValue(generator = "log_seq_gen")
private Long id;
```

allocationSize는 1 로 지정. 그 이상은 ID값 중복 될 수 있음


### Table 사용 방식

- 테이블을 시퀀스처럼 사용
- 테이블에 엔티티를 위한 키를 보과
- 해당 테이블을 이용해서 다음 식별자 생성

**설정 방식**
- `@TableGenerator`로 테이블 생성기 설정
- `@GeneratedValue`의 generator로 테이블 생성기 지정
- `EntityManager#persist()` 호출 시점에 테이블 사용
- `persist()` 할 때 테이블을 이용해서 식별자 구하고 이를 엔티티에 할당
- INSERT 쿼리는 실행하지 않음

```mysql
create table id_seq (
entity varchar(100) not null primary key,
nextval bigint
) engine innodb character set utf8mb4;
```

```java
@Entity
@Table(name = "access_log")
public class AccessLog {
    @Id
    @TableGenerator(
            name = "accessIdGen",
            table = "id_seq",
            pkColumnName = "entity",
            pkColumnValue = "accesslog",
            valueColumnName = "nextval",
            initialValue = 0,
            allocationSize = 1
    )
    @GeneratedValue(generator = "accessIdGen")
    private Long id;
    // ...
}
```


```java
@Embeddable
public class Address {
@Column(name = "addr1")
private String address1;
@Column(name = "addr2")
private String address2;
@Column(name = "zipcode")
private string zipcode;

    protected Address() {
    }

    // ... constructor, getter...
}
```

```java
@Entity
@Table(name = "hotel_info")
public class Hotel {
@Id
@Column(name = "hotel_id")
private String id;

    // ...
    @Embedded
    private Address address;
}
```

<br/>

### 저장
``` java
tx.begin();
Address address = new Address("주소1", "주소2", "12345");
Hotel hotel = new Hotel("H00", "HN", 2022, Grade.S7, address);
em.persist(hotel);
tx.commit();
```

```mysql
insert into hotel_info
(addr1, addr2, zipcode, created, grade, modified, nm, year, hotel_id)
values
(?, ?, ?, ?, ?, ?, ?, ?, ?)
```

<br/>

### 조회

```java
Hotel hotel = em.find(Hotel.class, "H00");
logger.info("주소: {}", hotel.getAddress());
```

- address 객체를 가져와서 Address Entity에 알맞게 넣어줌

<br/>

## Entity Mapping

- 객체와 테이블 매핑: @Entity, @Table
- 기본 키 매핑: @Id
- 필드와 컬럼 매핑: @Column
- 연관관계 매핑: @ManyToOne, @JoinColumn

<br/>

### @Entity

- 테이블과 매핑 시 붙이는 필수 어노테이션

**properties**

| 속성   | 기능                                                  | 기본값    |
|------|-----------------------------------------------------|--------|
| name | JPA에서 사용할 엔티티 이름 지정. 보통 기본값인 클래스 이름 사용. 중복 이름 지정 불가 | 클래스 명  |


- 기본 생성자 필수 (public, protected 생성자)
- `final` 클래스, `enum`, `interface`, `inner` 클래스에는 사용 불가
- 저장할 필드에 `final`을 사용하면 안됨


<br/>

### @Table

- 엔티티와 매핑할 테이블 지정

**properties**

| 속성                | 기능                                              | 기본값       |
|-------------------|-------------------------------------------------|-----------|
| name              | 매핑할 테이블 이름                                      | 엔티티 이름 사용 |
| catalog           | catalog 기능이 있는 데이터베이스에서 catalog 매핑              |           |
| schema            | schema 기능이 있는 데이터베이스에서 schema 매핑                |           |
| uniqueConstraints | DDL 생성 시 유니크 조건 생성. 2개 이상의 복합 유니크 제약조건도 만들 수 있음 |           |

<br/>

### 데이터베이스 스키마 자동 생성

`spring.jpa.properties.hibernate.show_sql=true`

콘솔에 실행되는 테이블 생성 DDL<small>Data Definition Language</small> 출력

<br/>

### DDL 생성 기능

```java
@Entity
@Table(name="MEMBER")
public class Member {
    
    @Id
    @Column(name="ID")
    private String id;
    
    @Column(name = "NAME", nullable = false, length = 10)
    private String username;
  
    //...
}
```

- `nullable = false`: `not null` 제약 조건 추가
- `length = 10`: 자동 생성되는 DDL 문자 크기 지정 가능

```java
@Entity(name = "Member")
@Table(name = "MEMBER", uniqueConstraints = {
        @UniqueConstraint(
                name = "NAME_AGE_UNIQUE",
                columnNames = {"NAME", "AGE"})})
public class Member {
    
    @Id 
    @column(name = "id")
    private String id;
    
    @Column(name = "name")
    private String username;
    
    private Integer age;
    // ..
}
```

위와 같이 설정하면 아래와 같은 DDL이 생성된다.

```sql
ALTER TABLE MEMBER
  ADD CONSTRAINT NAME_AGE_UNIQUE UNIQUE (NAME, AGE)
```

### 기본 키 매핑

- 직접 할당: 기본키를 애플리케이션에서 직접 할당
- 자동 생성: 대리 키 사용 방식
  - IDENTITY: 기본 키 생성을 데이터베이스에 위임
  - SEQUENCE: 데이터베이스 시퀀스를 사용해서 기본 키를 할당
  - TABLE: 키 생성 테이블 사용


데이터베이스 벤더마다 지원하는 방식이 다르기 때문에 이처럼 다양


### 필드와 컬럼 매핑: 레퍼런스





