### Spring Batch 

Apress 거래명세서 


- 매월 말 고객의 모든 계좌, 지난 달에 발생한 모든 거래, 계좌에 입금된 총 금액, 계좌에서 차감된 총 금액, 현재 잔액을 나열한 거래 명세서를 전달받는다.

<br/>

**제작할 4개의 STEP**

1. 변경할 고객 정보 불러오기
   1. 다양한 레코드로 구성된 csv 파일 하나 읽음
   2. 파일 내 다양한 레코드 형식으로 작성된 데이터는 고객 정보를 갱신하는 데 사용
2. 거래 정보 불러오기
   1. 정보를 불러오기 전, 이 내용을 데이터베이스의 고객 레코드에 적용
3. 거래 정보 반영하기
   1. 발생한 거래 정보는 XML 문서 형태로 제공되며 이를 읽어들여 데이터베이스의 새 레코드로 기록
   2. 모든 레코드가 데이터베이스에 적재되면, 모든 입금(credits)내역을 더하고 모든 출금(debits) 내역을 빼 현재 잔액에 적용해야 한다
4. 거래명세서 생성하기
   1. 고객 별 거래 명세서 파일이 하나씩 생성
   2. 각 파일의 헤더에는 고객의 주소가 포함됨
   3. 파일 데이터: 고객이 보유한 하나 이상의 계좌에 대해 계좌별로 계좌 헤더, 모든 거래 내역, 총 입금, 총 출금, 잔액 정보가 모두 출력


<br/>

Apress 거래명세를 위한 DB DDL

``` SQL
CREATE SCHEMA `apress-trading` ;

CREATE TABLE `apress-trading`.`customer` (
  `id` INT NOT NULL COMMENT '고객번호',
  `first_name` VARCHAR(45) NOT NULL COMMENT '고객이름(성)',
  `last_name` VARCHAR(45) NOT NULL COMMENT '고객이름(이름)',
  `ssn` VARCHAR(45) NOT NULL COMMENT '주민번호',
  `email` VARCHAR(255) NOT NULL COMMENT '이메일',
  `phone` CHAR(15) NOT NULL COMMENT '핸드폰번호',
  `work_phone` CHAR(15) NOT NULL COMMENT '회사전화번호',
  `notification_pref` CHAR(1) NULL COMMENT '알림수신유무',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ssn_uinque_idx` (`ssn` ASC) VISIBLE);

CREATE TABLE `apress-trading`.`account` (
  `account_id` INT NOT NULL COMMENT '계좌식별번호(PK)',
  `balance` FLOAT NOT NULL COMMENT '잔고',
  `last_stat_at` TIMESTAMP NOT NULL COMMENT '마지막상태변경일시',
  PRIMARY KEY (`account_id`));

CREATE TABLE `apress-trading`.`transaction` (
  `tx_id` INT NOT NULL COMMENT '거래식별번호(PK)\n',
  `account_id` INT NOT NULL COMMENT '계좌식별번호(FK)',
  `credit` FLOAT NOT NULL COMMENT '신용결제',
  `debit` FLOAT NOT NULL COMMENT '현금카드결제',
  `timestamp` TIMESTAMP NOT NULL COMMENT '거래일시',
  PRIMARY KEY (`tx_id`));

CREATE TABLE `apress-trading`.`customer_account` (
  `customer_id` INT NOT NULL COMMENT '고객식별번호(PK)',
  `account_id` INT NOT NULL COMMENT '계좌식별번호(PK)',
  PRIMARY KEY (`customer_id`, `account_id`))
COMMENT = '고객/계좌 매핑테이블';
```

<br/><br/>


### JOB

> 처음부터 끝까지 독립적으로 실행할 수 있는 고유하며 순서가 지정된 여러 스텝의 목록

위의 정의를 하나씩 쪼개보면 아래와 같다.


#### 1. 처음부터 끝까지 : 

"배치 처리는 어떠한 완료 상태에 도달할 때까지 추가적인 상호작용 없이 실행하는 처리" 라고 정의했다. 
즉, 외부 의존성 없이 실행할 수 있는 일련의 스텝이다. 
예를 들어 세번째 스텝이 특정 디렉터리 내에 처리할 파일이 수신되기를 기다리지 않고, 도착했을 때 잡을 시작한다.

#### 2. 독립적으로 실행할 수 있는:

각 배치 잡은 외부 의존성의 영향을 받지 않고 실행할 수 있어야 한다. 
그렇다고 잡이 의존성을 가질 수 없다는 것은 아니고, 실제로도 외부 의존성이 없는 잡은 많지 않다.
결국 의존성은 생길 가능성이 크며 의존성 관리를 할 수 있어야 한다.
만약 접근하고자 하는 파일이 없다면 오류를 처리해야하고 파일이 전달될 때까지 기다린다.
잡은 스케줄러와 같은 트리거로 실행하고, 잡 자체는 자신이 처리하기로 정의된 모든 요소를 제어할 수 있다.

#### 3. 고유하며:

잡은 유일하다. 코어 스프링 프레임워크를 사용한 빈 구성 방식과 동이하게 자바나 XML을 사용해 구성하며, 구성한 내용을 재사용할 수 있다. 즉, 동일한 구성으로 필요한 만큼 잡을 실행할 수 있다.

#### 4. 순서가 지정된 여러 스텝의 목록

잡에서 스텝의 순서는 중요하다. 거래 정보를 시스템 내로 불러오기 전에는 고객의 거래명세서를 생성할 수 없고, 거래 내용을 잔액에 적용할 대까지 계좌의 잔약을 계산할 수 없다.
이 처럼 모든 스텝을 논리적인 순서대로 실행할 수 있도록 잡을 구성한다.


## Job 구성하기

잡의 기본 구성

``` java
@Bean
public Job job() {
   return this.jobBuilderFactory.get("basicJob")
            .start(step1())
            .build();
}

@Bean
public Step step1() {
   return this.stepBuilderFactory.get("step1")
               .tasklet((contribution, chunkContext) -> {
                  System.out.println("Hello, world!");
                  return RepeatStatus.FINISHED;
               }).build();
}
```



## Job LifeCycle

잡의 실행은 잡 러너 job runner에서 시작된다. 잡 러너는 잡 이름과 여러 파라미터를 받아들여 잡을 실행시키는 역할을 한다. 
스프링 배치는 두 가지 잡 러너를 제공한다.

#### CommandLineJobRunner
: 스크립트를 이용하거나 명령행에서 직접 잡을 실행할 때 사용한다. 스프링을 부트스트랩하고, 전달받은 파라미터를 사용해 요청된 잡을 실행한다.

> org.springframework.batch.core.launch.support.CommandLineJobRunner

#### JobRegistryBackgroundJobRunner
: 스프링을 부트스트랩해서 기동한 자바 프로세스 내에서 쿼츠Quartz나 JMX 후크와 같은 스케줄러를 사용해 잡을 실행한다면, 스프링이 부트스트랩될 때 실행가능한 잡을 가지고 있는 JobRegistry를 생성한다. JobRegistryBackgroundJobRunner는 JobRegistry를 생성하는데 사용한다.

> org.springframework.batch.core.launch.support.JobRegistryBackgroundJobRunner

별개로 JobLauncherCommandLineJobRunner를 사용해 잡을 시작하는 또 다른 방법을 제공한다. 이 CommandLineRunner 구현체는 별도의 구성이 없다면 기본적으로 ApplicationContext에 정의된 Job 타입의 모든 빈을 기동 시에 실행한다. 

사용자가 스프링 배치를 실행할 때 잡 러너를 사용하긴 하지만, 잡 러너는 프레임워크가 제공하는 표준 모듈이 아니다.



<br/><br/>

## Partition Job 구성 

[참고 링크](https://www.baeldung.com/spring-batch-partitioner)