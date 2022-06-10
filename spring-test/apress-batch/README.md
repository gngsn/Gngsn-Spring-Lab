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
4. 거래명세서 생성하기7
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

<br/>



