# Chapter 5. Mapping Basic (연관 관계 기초)

_목표: 객체의 참조와 테이블의 외래 키 매핑_

<br/>

_**Table of Content**_

- 단방향 연관관계
- 연관관계 사용
- 양방향 연관관계
- 연관관계의 주인
- 양방향 연관관계 저장
- 양방향 연관관계의 주의점

<br/>

**Terms**

#### ✔️ 방향 _Direction_

- 단방향: 회원과 팀이 관계가 있을 때 '회원 → 팀' 또는 '팀 → 회원' **둘 중 한 쪽만 참조**
- 양방향: '회원 → 팀', '팀 → 회원' **양쪽 서로 참조**

_방향은 객체 관계에만 존재하고 테이블 관계는 항상 양방향_

#### ✔️ 다중성 _Multiplicity_
- 다대일(N:1)
- 일대다(1:N)
- 일대일(1:1)
- 다대다(N:M)

#### ✔️ 연관관계의 주인 _Owner_
- 객체를 양방향 연관관계로 만들면 연관관계의 주인을 정해 야 한다.

<br/>

---

## 단방향 연관관계

#### TL;DR. ️객체 연관관계 vs 테이블 연관관계
- **객체**는 참조(주소)로 연관관계를 맺음
    - 참조 `a.getB().getC()` 를 사용
    - 객체의 연관관계는 **단방향**
      - 양방향으로 참조하려면 단방향 연관관계를2개 만들어야 함
      - A → B (`a.b`), B → A (`b.a`)
- **테이블**은 외래 키로 연관관계를 맺음
    - 조인 `join` 사용
    - 테이블의 연관관계는 **양방향**


#### 다대일(N:1), 단방향

<img src="./image/img1.png" />

<br/>

### 객체 연관관계

- 회원 객체와 팀 객체는 **단방향 관계**
- 회원 객체는 `Member.team` 필드(멤버변수)로 팀 객체와 연관관계를 맺음
  - **회원**은 **팀**을 알 수 있음: `Member.team` 필드 접근 
  - **팀**은 **회원**을 알 수 없음.

> 객체 연관관계와 테이블 연관관계의 가장 큰 차이는 객체 연관관계는 "양방향 관계가 아니라 서로 다른 단방향 관계 2개"로 구성한다는 것" 

<br/>

<table>
<tr><th>단방향 연관관계</th><th>양방향 연관관계</th></tr>
<tr><td>

```java
class B {
    A b;
}

class B {}
```
</td><td>

``` java
class A {
    B b;
}
class B {
    A a;
}
```

</td>
</tr></table>

#### Example) 순수한 객체 연관관계 

<table>
<tr>
<th>Objects</th>
<td>

```java
public class Member {
    private String id; 
    private String username;

    private Team team; // 팀의 참조를 보관

    public void setTeam(Team team) {
        this.team = team;
    }
    
    // Getter, Setter ...
}
```
</td>
<td>

```java
public class Team {
    private String id;
    private String name;

    // Getter, Setter ...
}
```

</td></tr>
<tr><th>Relationship</th>
<td colspan="2">
회원 1과 회원2를 팀 1에 소속

```java
public static void main(String[] args) {
    Member member 1 = new Member("member1", "회원");
    Member member2 = new Member("member2", "회원2");
    
    Team team1 = new Team("team1", "팀1");

    member1.setTeam(team1);
    member2.setTeam(team1);

    Team findTeam = member1.getTeam();
}
```

</td></tr></table>

<br/>

#### 테이블 연관관계

- 회원 테이블과 팀 테이블은 **양방향 관계**
- 회원 테이블은 `TEAM_ID` 외래 키로 팀 테이블과 연관관계를 맺음
- 회원 테이블의 `TEAM_ID` **외래 키**를 통해 회원과 팀을 조인할 수 있고 반대로 팀과 회원도 조인할 수 있음
    - `TEAM_ID` 외래 키로 `MEMBER JOIN TEAM` 과 `TEAM JOIN MEMBER`

<br/>


#### Example) 순수한 객체 연관관계

<table>
<tr>
<th>Objects</th>
<td>

```sql
CREATE TABLE MEMBER (
        MEMBER_ID VARCHAR(255) NOT NULL,,
        TEAM_ID VARCHAR(255),
        USERNAME VARCHAR(255),
        PRIMARY KEY (MEMBER_ID)
)
```
</td>
<td>

```sql
CREATE TABLE TEAM (
        TEAM_ID VARCHAR(255) NOT NULL,
        NAME VARCHAR(255),
        PRIMARY KEY (TEAM_ID)
)
```

</td></tr>
<tr><th>Relationship</th>
<td colspan="2">

```sql
ALTER TABLE MEMBER ADD CONSTRAINT FK_MEMBER_TEAM 
    FOREIGN KEY (TEAM_ID) 
    REFERENCES TEAM
```

```sql
INSERT INTO TEAM (TEAM_ID, NAME) VALUES ('team1', '팀1'); 
INSERT INTO MEMBER(MEMBER_ID, TEAM_ID, USERNAME) 
VALUES ('member1', 'team1', '회원1');
INSERT INTO MEMBER(MEMBER_ID, TEAM_ID, USERNAME) 
VALUES ('member2', 'team2', '회원2');
```

```sql
SELECT T.* 
FROM MEMBER M 
    JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID 
WHERE M.MEMBER_ID = 'member1'
```

</td></tr></table>

<br/>
