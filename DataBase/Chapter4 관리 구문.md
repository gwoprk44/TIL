---
aliases:
  - DB
tags:
  - Resource
  - Dev
  - DB
---

# DML문

테이블 내부를 조작하는 구문들로 구성되어 있다.

## INSERT 문

기존 테이블에 행을 삽입하는 구문으로 아래와 같이 사용되게 된다.

```sql
INSERT INTO table_name (column1, column2, ...) 
VALUES (7777, 'SAM', ...);
```

방식으로 컬럼을 지정하고 해당 컬럼에 값을 집어넣는 방식이다.

하나의 행, 그리고 특정 컬럼에 값을 집어 넣기에,

빈 열에는 값이 들어가지 않거나, DEFAULT 설정이 되어있으면 해당 값이 들어간다.

데이터 값을 직접 집어넣는 방식이기에,

여러 데이터를 넣을때는 잘 사용되는 방식이 아니다.

그리고 INSERT INTO 뒤에 열을 기술하지 않는다면

values 처럼 데이터가 들어오는 항목에는

전체 열과 같은 갯수의 데이터가 들어와야한다.

  

그리고 만약 기존 테이블에 데이터가 있다면,

서브 쿼리를 통해서 여러 행의 값을 삽입하는 것이 가능하다.

```SQL
INSERT INTO table_name --(column1, column2) 
SELECT column1, column2 from table_name2 where ...;
```

  

그리고 INSERT구문은 INSERT ~ INTO 사이에 ALL을 기술하느냐,

FIRST ~ WHEN을 쓰느냐에 따라 INTO 뒤에 모두 삽입하는 것이 가능하거나

조건적으로 삽입하는 것이 가능하다.

```sql
INSERT ALL
WHEN column1 >= 5 THEN INTO table_name1 
WHEN column1 >=105 THEN INTO table_name2
ELSE INTO table_name3
SELECT column1, column2
FROM table_name3;
```

  

첫 구문 처럼 ALL을 넣으면 table_name1, table_name2에

모두 데이터가 들어가지만

```sql
INSERT FIRST
WHEN column1 >= 5 THEN INTO table_name1 
WHEN column1 >=105 THEN INTO table_name2
ELSE INTO table_name3
SELECT column1, column2
FROM table_name3;
```

두 번째 처럼 FIRST를 넣게되면 5이상의 값은

table_name1에만 들어가게 된다.

## UPDATE , DELETE 문

기존 테이블 행의 값을 갱신하기 위해 사용하는 구문으로 아래와 같이 사용된다.

```sql
UPDATE table_name1 
SET column1 = 1000, column2 = 2000 
WHERE column3 = 70;
```

쉼표로 구분을 해주면 한 번에 여러 열 갱신도 가능하며

인라인 뷰를 통해서, null로 갱신되는 경우가 있는 case를 방지할 수 있다.

```sql
UPDATE table_name1 a 
SET (a.column1, a.column2) = 
(SELECT SUM(x.column1), SUM(x.column2) 
FROM table_name2 x 
WHERE x.column3 = a.column3)
```

해당 쿼리는 table_name1에만 존재하는 column3과 같이 있는 행의

column1, column2의 값을 null로 갱신한다.

```sql
WHERE EXISTS 
(SELECT 1 
FROM table_name2 x 
WHERE x.column3 = a.column3);
```

하지만 맨 뒤에 WHERE절로 exists를 추가하면

null로 갱신되는 것을 방지할 수 있다.

해당 쿼리는 대신, table_name2를 2번 사용하기에

더 효율적인 쿼리를 소개하려고 한다.

```sql
UPDATE
(SELECT a.column1, a.column2, b.column1 as col_n, b.column2 as col2_n
FROM table_name1 a
, (SELECT column3, SUM(x.column1) as column1, SUM(x.column2) as column2 
FROM table_name2 
GROUP BY column3) b 
WHERE b.column3 = a.column3)
SET column1 = col_n, column2 = col2_n;
```

table_name2를 inner join으로 해서 column3끼리 대응되는 테이블들로 먼저 만들고

해당 값들을 바꾸는 방식이다.

테이블을 한 번씩만 사용하기에 훨씬 효율적이다.

  

다만 에러가 발생하는 경우는 여러개 있다.

MERGE를 통해서, 조인 차수가 1:M의 구조가 된다면

여러번 갱신이 되기 때문에 이러한 경우에는 에러가 발생한다.

추가로 PK 제약조건으로 1번만 갱신되는 것이 보장되지 않는다면

에러가 발생하게 된다.

즉 UPDATE구문은 값이 한번만 변경되지 않고 여러번 변경이 된다면 에러를 발생 시킨다.

  

DELETE구문은 단순하게 테이블의 기존 행을 삭제하는 것으로

만약 WHERE절이 없다면 전체 행을 삭제하고, 서브쿼리 사용이 가능하다.

```sql
DELETE 
FROM table_name1 
WHERE column1 = 100; --조건부 삭제

DELETE 
FROM table_name1; --전체 삭제

DELETE 
FROM table_name1 a 
WHERE not exists(
    SELECT 1
    FROM table_name2 x 
    WHERE x.column1 = a.column1
    ); --서브쿼리에 없는 항목 삭제
```

그렇기에 UPDATE와 DELETE구문은 사용시에는,

미리 SELECT절로 자세히 다 확인 후에 해당 SELECT를

UPDATE, DELETE로 간단히 변경하는 것을 권장.

  

## MERGE 구문

MERGE구문은 위에서 언급된 삽입, 갱신, 삭제가 모두 가능한 구문이다.

대신 JOIN구문 처럼 사용이 되기에, 해당 구문에 대한 숙지가 중요하다.

```sql
MERGE
INTO table_name1 t
USING table_name2 s
ON (t.column1 = s.column2)
WHEN MATCHED THEN
UPDATE
SET t.column2 = s.column2 - 500
WHERE t.column4 = 500
WHEN NOT MATCHED THEN
INSERT (t.column1, t.column3, t.column4)
VALUES (s.column1, s.column3, s.column4);
```

MERGE 구문의 핵심은 USING절, ON절이다.

어떤 테이블을 이용할 것인지, 그리고 그 기준이 어떤 것인지

정확히 나타내야하기 때문이다.

  

게다가 ON절에 기술된 열은 일반적으로 갱신할 수 없다.

하지만, ROWID, ROW_NUMBER등을 사용하면 갱신이 가능하다.

```sql
MERGE 
INTO table_name1 t
USING 
(SELECT a.column1_n, b.ROWID as rid
FROM 
(SELECT column1, column1 + ROW_NUMBER () OVER (ORDER BY column1) AS column1_n 
FROM table_name2) a
, table_name1 b
WHERE b.column1 = a.column1) s
ON (t.ROWID = s.rid)
WHEN MATCHED THEN
UPDATE SET t.column1 = s.column1_n ;
```

정확히는 ROWID들끼리 이용해서 하는 것이기에,

쿼리가 길어지고 복잡해진다는 단점이 존재한다.

  

## ERROR 로깅

DML구문은 공통적으로 중간에 에러가 발생하면 이전에 사항들을 모두 롤백시킨다.

이를 방지하기 위해, DML 수행 중 에러 발생시,

에러를 기록하고 다음 행에 대해서 진행을 할 수 있다.

```sql
BEGIN
DBMS_ERRLOG.CREATE_ERROR_LOG (dml_table_name => 'T1'
, err_log_table_name => 'E1');
```

PL/SQL 처리를 수행 한 이후,

```sql
INSERT INTO table_name (column1, column2, ...) VALUES (7777, 'SAM')
LOG ERRORS INTO e1('1') REJECT LIMIT UNLIMITED;
```

수행하는 DML 쿼리 뒤에 LOG ERRORS를 추가하면

e1 테이블에서 해당 에러 결과를 확인할 수 있게 된다.

이러한 테이블은 에러 체킹이 끝나서 더 이상 활용을 하지 않는다면

DROP TABLE을 통해서 삭제를 하면 된다.


# TCS문

트랜잭션을 제어하는 구문이다.

>[!NOTE] 트랜잭션
>트랜잭션이란 함께 수행하여야 하는 작업의 논리적인 단위. 예를 들어 계좌이체를 진행한다 가정할때 
>출금계좌의 잔고를 차감하는 작업과 입금계좌의 잔고를 증가시키는 작업은 동시에 하나의 트랜잭션으로 이루어져야 한다.


은행의 송금 방식의 핵심은

중간에 어떠한 상황으로 인해 송금이 진행이 되지 않았다면

다시 송금 전으로 돌아갈 수 있어야된다는 것과

송금이 완료되면 이를 되돌리기 위해서는 추가적인 작업이 필요하다는 것이다.


송금의 단계를 진행하고 이를 완료하는 것을

SQL구문에서는 commit이라고 하며

```SQL
COMMIT;
```

을 수행하게 되면 트랜잭션이 종료되게 된다.

보통은 DML, DDL 구문의 수행 이후에,

정상적인 트랜잭션 내용을 반영하기 위해서 수행을 해준다.

  

그리고 중간에 송금 전 상황으로 돌아갈 수 있게 해주는 것을 롤백이라고 한다.

COMMIT을 하지 않고, 변경 내용을 모두 취소하기 위해서 사용한다.

```SQL
ROLLBACK;
```

을 수행하게 되면 취소 후에 트랜잭션을 종료하게 된다.


한 트랜잭션 안에 변경 사항이 많게 되었을 때,

위에처럼 ROLLBACK을 사용하게 되면 모든 변경 사항들이 취소가 되게 된다.

그렇기 때문에 중간 중간 원하는 지점으로 ROLLBACK을 하고 싶은 경우를 위해

SAVEPOINT가 존재한다.

```SQL
변경 쿼리1;
SAVEPOINT point이름1;

변경 쿼리2;
SAVEPOINT point이름2;
```

원하는 변경 사항 이후에 해당 쿼리를 실행시켜 주게 되면

SAVEPOINT가 생성이 되고

```
ROLLBACK TO SAVEPOINT point이름1;
```

을 수행하게 되면

변경 쿼리2가 수행이 되지 않은 상태로 트랜잭션이 롤백된다.

이 상황에서

```
ROLLBACK TO SAVEPOINT point이름2;
```

를 하게 되면, 이미 이전 시점으로 롤백이 되었기에

해당 쿼리는 에러가 발생하게 된다.

## 구조

트랜잭션은 DML문이나 SET TRANSACTION문이 실행되면 시작되고, COMMIT문이나 ROLLBACK 문이 실행되면 종료된다. 

트랜잭션이 시작되면 내부적으로 언두 세그먼트가 생성, 트랜잭션에 ID부여된다.

트랜잭션 내부 ID는 언두 세그먼트의 번호, 슬롯, 시퀀스의 조합으로 생성.

# 오라클 Lock

오라클은 공유 리소스와 사용자 데이터를 보호할 목적으로 다음과 같은 종류의 Lock을 사용함.

- DML Lock
    - 애플리케이션 측면에서 가장 중요하게 다룰 Lock
    - 다중 트랜잭션이 동시에 액세스하는 사용자 데이터의 무결성을 보호해준다.
    - Table Lock과 Row Lock이 있음
- DDL Lock
- 래치
    - SGA에 공유된 각종 자료구조를 보호하기 위해 사용됨.
- 버퍼 Lock
    - 버퍼 블록에 대한 액세스 직렬화하기 위해 사용
- 라이브러리 캐시 Lock/Pin
    - 라이브러리 캐시에 공유된 SQL 커서와 PL/SQL 프로그램을 보호하기 위하 사용
- 기타 등등 Lock

## DML Row Lock

- 두 개의 동시 트랜잭션이 같은 로우를 변경하는 것을 방지.
- 하나의 로우를 변경하려면 로우 Lock을 먼저 설정해야 함.
- UPDATE, DELETE 할 때 다른 트랜잭션이 UPDATE, DELETE 할 수 없음.
- INSERT에 대한 로우 Lock 경합은 Unique 인덱스가 있을때 발생.
    - Unique 인덱스가 있는 상황에서 두 트랜잭션이 같은 값을 입력하려고 할때 경합 발생
    - 이 경우, 후행 트랜잭션은 기다렸다 선행 트랜잭션이 커밋하면 INSERT 실패하고 롤백한다.
    - 두 트랜잭션이 다른 값을 입력하거나 Unique 인덱스가 아예 없으면 경합은 발생하지 않는다.

### MVCC 모델

- **MVCC 모델을 사용하는 오라클은 select 문에 로우 lock을 사용하지 않음. (postgresql도 그럴듯)**
- MVCC 모델을 사용하면 DML과 SELECT는 서로 진행을 방해하지 않음.
- 오라클은 DML과 SELECT는 서로 진행을 방해하지 않는다. SELECT 끼리도 마찬가지다.
- DML 끼리는 서로를 방해할 수 있다.

### MVCC 모델이 아닌 DBMS

- MVCC 모델을 사용하지 않으면 SELECT문에 공유 lock을 사용함.
- 공유 lock끼리는 호환이 되나, 공유 lock과 배타적 lock은 호환되지 않아, DML과 SELECT는 서로 방해될 수 있다.
- **다른 트랜잭션이 읽고 있는 로우를 변경하려면 다음 레코드로 이동할 때까지 기다려야 함. 그리고 다른 트랜잭션이 변경 중인 로우를 읽으려면 커밋할 때까지 기다려야 한다.**

> DML 로우 Lock에 대한 성능 저하를 방지하려면, 온라인 트랜잭션을 처리하는 주간에 Lock을 필요이상으로 오래 유지하지 않도록 커밋 시점을 조절해야 한다.

## DML 테이블 Lock (TM Lock)

오라클은 DML 로우 Lock을 설정하기 전에 테이블 Lock을 먼저 설정함.

현재 트랜잭션이 갱신중인 테이블 구조를 다른 트랜잭션이 변경하지 못하게 막기위함.

### Lock 모드간 호환성 정리

- RS : row share
- RX : row exclusive
- S : share
- SRX : share row exclusive
- X : exclusive

테이블 lock이라고 하면 테이블 전체 lock 걸린다고 생각하기 쉬운데, 그게 아니라 자신이 해당 테이블에서 현재 작업을 수행중인지 알리는 일종의 플래그다.

그래서 여러 테이블 lock 모드가 있다. 어떤 모드 사용했는지에 따라 후행 트랜잭션이 할수 있는 작업 범위가 결정된다.

## 대상 리소스가 사용중일 때 진로 선택

lock 얻으려는 리소스가 사용중일 때, 프로세스는 3가지 방법 중 하나를 택함.

1. lock이 해제될 때까지 기다린다.
2. 일정 시간만 기다리다 포기
3. 기다리지 않고 포기

그러나 `NOWAIT` 키워드를 사용하면 바로 작업 포기시킬 수 있다.

`lock table emp in exclusive mode NOWAIT`

## Lock을 푸는 열쇠, 커밋

블로킹은 선행 트랜잭션이 결정한 Lock 때문에 후행 트랜잭션이 작업못하고 멈춘 상태이다.

해소 방법은 커밋 또는 롤백 뿐이다.

**데드락**은 두 트랜잭션이 각각 특정 리소스에 Lock을 설정한 상태에서 맞은편 트랜잭션이 lock을 설정한 리소스에 또 lock을 설정하려고 진행하는 상황을 말함.

오라클에서 교착상태가 발생하면, 이를 먼저 인지한 트랜잭션이 문장 수준 롤백을 진행한 후에 아래 에러 메시지를 던진다.

`ORA-00060: deadlock detected while waiting for resource`

교착상태를 발생시킨 문장 하나만 롤백

교착상태가 해소되어도 블로킹 상태라 트랜잭션은 커밋 또는 롤백을 결정해야 함.

프로그램 내에서 예외처리(커밋 또는 롤백)를 하지 않는다면, 대기 상태를 지속하게 되므로 주의

### 4가지 커밋 명령

- WAIT : LGWR가 로그버퍼를 파일에 기록했다는 완료메시지를 받을때까지 기다림
- NOWAIT : LGWR가 완료메시지 기다리지 않고 다음 트랜잭션 진행
- IMMEDIATE : 커밋 명령을 받을 때마다 LGWR가 로그버퍼를 파일에 기록
- BATCH : 세션 내부에 트랜잭션 데이터를 일정량 버퍼링했다 일괄 처리

# 트랜잭션 동시성 제어

## 비관적 동시성 제어

- 사용자들이 같은 데이터를 동시에 수정할 것을 가정함.
- 한 사용자가 데이터를 읽는 시점에 lock을 걸고 조회 or 갱신처리가 완료될 때까지 유지.
- lock은 다른 사용자들이 같은 데이터 수정 못하게 만들어서 비관적 동시성 제어는 잘못사용시 동시성이 나빠진다.
- 비관적 동시성 제어는 자칫 시스템 동시성을 심각하게 떨어뜨릴 우려가 있지만 FOR UPDATE 에 WAIIT 또는 NOWAIT 옵션을 사용하면 LOCK을 얻기 위해 무한정 기다리지 않아도 됨.
- 그리고 다른 트랜 잭션에 의해 LOCK이 걸렸을 때 Exception을 만나게 되어 트랜잭션을 종료할 수 있다 ⇒ 오히려 동시성 증가

## 낙관적 동시성 제어

- 사용자들이 같은 데이터를 동시에 수정하지 않을 것을 가정함.
- 데이터를 읽을 때, lock을 설정하지 않는다.
- 읽는 시점에 lock을 사용하진 않았지만, 데이터를 수정하고자 하는 시점에 앞서 읽은 데이터가 다른 사용자에 의해 변경되었는지 체크해야 한다.

## **데이터 품질과 동시성 향상을 위한 제언**

- FOR UPDATE 사용을 두려워 하지 말자
- 다중 트랜잭션이 존재하는 데이터베이스 환경에서 공유자원에 대한 액세스 직렬화는 필수
- 데이터 변경할 목적으로 읽으면 당연히 LOCK을 걸어야 함
- 금융권은 필수! FOR UPDATE를 알고 쓰고 필요한 상황이면 정확히 사용하고, 동시성이 나빠지지 않게 WAIT 또는 NOWAIT 옵션을 활용한 예외처리를 잘하자
- 불필요하게 LOCK을 오래 유지하지 말고, 트랜잭션의 원자성을 보장하는 범위 내에서 가급적 빨리 커밋하자.
- 꼭 주간에 수행할 필요가 없는 배치 프로그램은 야간 시간대에 수행하자.
- 낙관적, 비관적 동시성 제어를 같이 사용하는 방법도 있다. 

# DDL문
## 테이블

데이터를 저장할 수 있는 기본적인 오브젝트로,

행과 열로 구성되어있고, 오브젝트 안에다가 제약조건과 인덱스를 생성할 수 있다.

만드는 과정인 `CREATE`, 수정이 가능한 `ALTER`,

테이블을 삭제하는 `DROP`, 테이블 내 데이터를 전부 지우는 `TRUNCATE`가 있다.

```SQL
CREATE TABLE table_name1(
    column_1 number
    , column2 number(2) default 200
    , column3 number(10) default 10 not null
);
```

테이블 안에 컬럼 명칭과 타입, 경우에 따라서 기본 값과

not null이라는 컬럼 내에 값이 존재해야된다는 설정을 추가 할 수 있다.

```
CREATE TABLE table_name1 select * from table_name2 where 0=1;
```

또한 기존 테이블을 활용하여 서브쿼리로 생성하는 것도 가능하며

이러한 방식을 축약해서 CTAS라고 부른다.

다만 이러한 방식은 열 타입과 NOT NULL 제약조건은 들고오지만

기본 값 설정은 가져오지 않는다.


`ALTER` 쿼리는 테이블 이름 변경, 읽기 전용 테이블로 변경이 가능해진다.

```
ALTER TABLE table_name1 RENAME TO new_table1;
```

`DROP` 쿼리는 테이블을 삭제할 수 있으며, 추가적으로

`PURGE`를 통해 휴지통 기능을 사용하지 않고 바로 삭제할 수 있고(데이터 복구 불가)

`CASCADE CONSTRAINTS` 를 통해 FK조건을 함께 삭제가 가능하다.

```
DROP TABLE new_table1 CASCADE CONSTRAINTS PURGE;
```

마지막으로 `TRUNCATE`의 경우, 테이블을 초기화 하는데

테이블 저장 공간을 해제 하므로 DELETE와 다르게 롤백이 불가능하다

```
TRUNCATE TABLE new_table1;
```

### 테이블 구조

위에서 테이블 생성, 수정, 삭제를 보았는데

이러한 테이블들에도 여러 가지 유형들이 존재한다.

위에서 CREATE를 통해서 만들어진 테이블 모두 `힙 테이블`이라는

임의의 위치에 데이터를 저장하는 테이블이다.

  

다음 테이블은 `인덱스 구조 테이블`로,

pk 제약 조건의 열 순서에 따라 데이터가 정렬되어 저장된다.

```SQL
CREATE TABLE table_name1(
    column_1 NUMBER
    , column2 NUMBER
    , CONSTRAINT table_name1_pk PRIMARY KEY (column1)
)
ORGANIZATION INDEX ;
```

  

그리고 트랜잭션 종료되면 초기화 되는 `임시 테이블`이 있다.

```SQL
CREATE GLOBAL TEMPORARY TABLE table_name1(
    column_1 NUMBER
    , column2 NUMBER
)
ON COMMIT DELETE ROWS;
```

마지막에 DELETE ROWS는 트랜젝션 레벨로 데이터를 저장하며

PRESERVE ROWS는 세션 레벨로 데이터를 저장한다.(다시 접속시 사라짐)
## 뷰

간단하게 이야기 하면 SELECT문을 DB에 저장하는 오브젝트이다

테이블 처럼 사용이 가능하고 JOIN, UNION ALL등 결합해서 만드는 방식이 대부분이다.

```SQL
CREATE OR REPLACE VIEW view1 AS
SELECT * FROM table_name1 WHERE column1 >= 1;
```

테이블 처럼 데이터 UPDATE, DELETE 등이 가능하지만

이러한 기능은 테이블에도 있으므로, 테이블을 참조해서 뷰를 만드는 경우

데이터를 직접 VIEW에 넣기보다는 테이블에 넣는 방식을 사용한다.

  

뷰의 가장 큰 장점은 데이터 보안성 그리고 독립성이다.

사용자에게 일부만 공개를 해야되는 데이터의 경우,

일부 컬럼만 들어있는 VIEW를 생성 한 다음,

해당 VIEW에 접근할 수 있는 권한만 부여하면 된다.

기존에 테이블을 수정하면 이를 바라보는 사용자들이 이에 맞춰서 수정을 하여야 하지만

뷰를 바라보게 하면, 뷰 안의 쿼리를 수정 후에

as를 통해서 컬럼 명을 그대로 유지하는 방식으로 수정을 하지 않게 해도 된다.

## 열

테이블을 구성하는 기본 단위로 테이블이 만들어진 다음에

새로 추가하거나, 변경, 삭제 등을 가능하게 할 수 있다.

```sql
ALTER TABLE table_name1 ADD (column2 NUMBER DEFAULT 11 NOT NULL, column3 VARCHAR2(14)); --컬럼 추가

ALTER TABLE table_name1 ADD (column2 NUMBER(2), column3 VARCHAR2(14)); --컬럼 추가

ALTER TABLE table_name1 MODIFY (column1 DEFAULT 11); --컬럼 DEFAULT 값 변경

ALTER TABLE table_name1 RENAME COLUMN column22 TO column2; --컬럼 이름 변경

ALTER TABLE table_name1 DROP (column3, column5); --컬럼 삭제

ALTER TABLE table_name1 SET UNUSED (column2, column4); --미 사용 열로 변경
```

ALTER 쿼리 뒤에 테이블 이름을 넣고 원하는 동작을 수행하면 되는 방식으로

여러 개의 컬럼을 다룰 경우 ()가 들어가게 된다.

RENAME의 경우, COLUMN이 변경 되는 것을 인지시켜주기 위해 COLUMN이 추가 되고

원래 이름에서 나중 이름을 지정해주기 위해 TO가 추가 된다.

SET UNUSED와 다르게 테이블에서 컬럼을 안 보이게 하는 INVISIBLE도 존재한다.

주로 열 순서를 변경할 때 사용한다.

### 열 타입

컬럼의 데이터 타입은 크게 문자, 숫자, 날짜, 이진 분류로 나뉜다.

먼저 문자 중에 제일 가장 많이 쓰이는 `VARCHAR2`이다.

최대 길이는 4000바이트 이며, 그 이상으로 하고 싶은 경우 보통 CLOB를 사용한다.

한 가지 생각이 들 수 있는게, 그러면 문자 타입은 다 CLOB만 사용해도 되는거 아니냐 라고 들 수 있다.

하지만, CLOB 타입은 WHERE 조건과 같은 비교 조건을 쓸 수가 없다.

(ex WHERE column = ‘A’) –> ERROR

단, LIKE는 ERROR가 발생하지 않는다.

  

숫자 컬럼은 NUMBER가 가장 많이 쓰이며, 괄호 안에 정수, 소수 부문으로 나뉜다

그렇기에 보통 NUMBER(3,2)로 되어있으면, 뒤에 소수점은 2번째 자리까지만 허용되고

앞의 정수 부문은 1개의 숫자만 들어간다

그렇기에 총 범위는 -9.99 ~ 9.99 사이의 값만 들어간다.

추가 예시로 NUMBER(2,-2)는 -9900 ~ 9900의 정수 값이 들어가게 된다.

### 제약조건

데이터 무결성 보장을 위한 기능으로 `NOT NULL, UNIQUE, PK, FK, CHECK` 제약 조건이 있다.

CONSTRAINT를 통해 설정이 가능하거나, ALTER TABLE을 통해서 추가/제거하면 된다.

NOT NULL은 열에 NULL 값이 들어가지 않도록 보장하는 기능이다.

UNIQUE는 이와 다른 것이, NOT NULL은 NULL이 들어오는 것을 방지하는 것이지만

UNIQUE는 값이 중복해서 들어오는 것을 방지해준다.

전체 열에 NULL이 들어오면 중복 검사를 하지 않는다는 점도 있다.

  

PK는 NOT NULL과 UNIQUE의 제약 조건들이 합쳐진 제약 조건으로

ORACLE에서는 PK가 설정되어 있다면 NOT NULL 설정이 되어있다.

또한 테이블 당 하나의 PK만 설정이 가능하다.

```
ALTER TABLE table_name1 ADD CONSTRAINT table1_pk PRIMARY KEY (column1);
```

이런 식으로, PK를 추가 할수도 있다.

마지막으로 FK는 `참조 무결성`을 보장하는 제약조건으로

부모 테이블의 갱신/삭제시 수행 규칙을 지정할 수 있다.

NO ACTION은 에러가 발생하고, CASCADE는 자식 테이블에 내용이 반영되고

SET NULL, SET DEFAULT는 자식 테이블의 값을 해당 값으로 변경 한다.

RESTRICT는 참조하는 행을 갱신, 삭제할 수 없게 한다.

```SQL
ALTER TABLE table_name2 
ADD CONSTRAINT table2_fk1 FOREIGN KEY (column1) 
REFEREBCE table_name1 (column2)
ON DELETE CASCADE;
```

단, 참조하는 부모 테이블의 열은 UNIQUE 혹은 PK 설정이 되어있어야 한다.

만약 설정시에 특별한 조건을 설정하지 않았을때,

자식 테이블이 참조하고 있는 값이 있다면 부모 테이블은 값을 갱신/삭제 할 수가 없다.

마지막으로 FK 제약조건을 생성한 열은(자식 테이블) 인덱스를 필수로 생성하여야 한다.

UPDATE 등으로 인한 블로킹으로 인한 장애 발생이 일어날 수 있다.

## INDEX

데이터를 빠르게 검색하기 위한 오브젝트로 테이블에 종속 되어 있으며

하나의 테이블에 여러 개의 인덱스를 생성 할 수 있다

```
CREATE UNIQUE INDEX table1_u1 ON table_name1 (column1, column2);
```

앞에 UNIQUE가 붙으면 고유한 값으로 구성이 된 인덱스이다.


이러한 인덱스를 비할성화 시키는 방법이 있는데

```
ALTER INDEX table1_u1 UNUSABLE;
```

해당 방법을 사용하게 되면, 해당 인덱스는 UNIQUE 인덱스이기 때문에

비활성화가 되면 데이터를 INSERT 할 수 없게 된다.


```
ALTER INDEX table1_u1 REBUILD;
```

해당 쿼리는 단편화 해소나 테이블 스페이스 변경을 위해 주로 사용하며,

인덱스가 재 구축 된다.


그리고 PK와 UNIQUE 조건은 내부적으로 인덱스를 사용하기에

생성할 때, 해당 제약조건에서 사용할 수 있는 인덱스가 없으면 자동으로 생성 된다

그리고 수동으로 해당 조건을 지정할 수 있는데

```
ALTER TABLE table_name1 ADD CONSTRAINT table1_pk PRIMARY KEY (column1, column2) USING INDEX table1_pk;
```

USING을 이용해서 지정할 수 있다.

## 파티션

다수의 물리적 파티션이 하나의 논리적 오브젝트로 관리되는 것으로

주로 테이블에서는 파티션 키를 통해서 분할이 되며 테이블과 동일한 구조로 생성이 된다.

파티션에는 주로 RANGE 파티션, HASH 파티션이 있는데

먼저 `RANGE 파티션`의 경우

```SQL
CREATE TABKE table_name1 (column1 NUMBER, column2 DATE)
PARTITION BY RANGE(column2) (
    PARTITION p_name1 VALUES LESS THEN (DATE '2022-02-01')
    , PARTITION p_name2 VALUES LESS THEN (DATE '2022-03-01')
    , PARTITION p_name3 VALUES LESS THEN (DATE '2022-04-01')
    , PARTITION p_name4 VALUES LESS THEN (MAXVALUE)
);
```

방식으로 생성이 되며, 주로 마지막 값을 지정하기 힘들기 때문에

MAXVALUE를 넣는 방식으로 마무리 한다.

  

이러한 파티션은 SELECT 구문에서도 활용이 가능한데

```
SELECT * FROM table_name1 PARTITION FOR ('2022-04-03')
```

‘2022-04-03’에 해당하는 테이블의 파티션을 조회하는 방식이다.

또한 파티션은 컬럼을 다중으로 활용하여 생성이 가능하며,

RANGE() 에서 쉼표만 추가해서 넣어주면 된다.

  

`HASH 파티션`의 경우, 범위를 지정하지 않고

데이터가 무작위로 분산되는데, OLTP 시스템의 블록 경합을 해소하기 위해 주로 사용 된다.

이외에도, LIST를 활용하는 파티션, 파티션 키 없이 임의로 데이터를 분할하는 SYSTEM 파티션 등이 있다.

  

파티션 테이블 말고도 `파티션 인덱스`가 존재하는데

이는 `다수의 물리적 인덱스 파티션을 하나의 논리적 인덱스로 사용`할 수 있다.

파티션 테이블과 동일한 구조로 파티셔닝이 되는 로컬 파티션 인덱스,

테이블과 관계없이 파티셔닝 되는 글로벌 파티션 인덱스가 있다.

이러한 파티션 인덱스는 파티션 키가 인덱스의 선두 열이면 PREFIXED 파티션 인덱스,

그게 아니라면 NONPREFIXED 파티션 인덱스가 된다.

이 중에서도 NONPREFIXED 글로벌 파티션 인덱스는 생성이 되지 않는다.

```SQL
CREATE UNIQUE INDEX table1_u1 ON table_name1 (column1, column2) LOCAL;

CREATE UNIQUE INDEX table1_x1 ON table_name1 (column3, column2) GLOBAL PARTITION BY HASH (column3) PARTITIONS 2;
```

  

이러한 파티션이 가장 중요하게 사용되는 이유는 데이터가 누적되고 많아지게 되어서 관리하게 되는 시점이다.

RANGE 파티션이 많이 쓰이는 이유는 일간, 월간 등으로 구간을 나누고

파티션 관리 정책을 내부에서 수립하게 되면, 그 이후에는

예전 데이터가 있는 파티션은 삭제하고 새로운 데이터를 위한 파티션을 새로 만들기만 하면 된다.

```SQL
ALTER TABLE table_name1 ADD PARTITION partition4 VALUES LESS THAN (MAXVALUE);

ALTER TABLE table_name1 DROP PARTITION partition4;
```

다만 이러한 파티션 추가는 마지막 파티션 보다 키 값이 큰 파티션만 추가가 가능하다.

그렇기 때문에, 보통 마지막 파티션을 MAXVALUE로 설정하고

일반적인 RANGE 파티션이 LESS THEN으로 구간을 나누는 이유이다.

  

```SQL
ALTER TABLE table_name1 MERGE PARTITIONS partition4, partition5 INTO PATITION partition5;

ALTER TABLE table_name1 MERGE PARTITIONS partition4 TO partition5 INTO PATITION partition5;
```

파티션은 MERGE를 사용하면 병합이 가능한데

범위를 지정해서 병합을 할 수 있게 할 수 있다.

```SQL
ALTER TABLE table_name1 SPLIT PARTITIONS partition4 AT (4) INTO (PATITION partiton3, PARTITION partiton4);

ALTER TABLE table_name1 SPLIT PARTITIONS partition4 INTO(
    PATITION partiton2 VALUES LESS THEN (2)
    , PATITION partiton3 VALUES LESS THEN (4)
    , PARTITION partiton4);
```

첫 코드는 4 값을 기준으로 파티션을 분할할 수 있게 해주는 코드이다.

하나의 파티션을 2개로 분할 할때는 AT으로 기준 값을 정해주고

INTO 안에는 파티션과 명칭을 설정해주면 된다.

3개 이상으로 할 때는 INTO 안에 파티션 기준 값을 넣어주고 분할을 진행하면 된다.

  

```SQL
ALTER TABLE table_name1 EXCHANGE PARTITIONS partition1
WITH TABLE table_name2 INCLUDING INDEXES WITHOUT VALIDATION;
```

table_name1의 partition1을 table_name2로 교체하는 작업입니다.

table_name1의 파티션 중 하나는 table_name2가 된다는 것을 의미하며

주로 파티션 일부만 작업 후, 다시 교체하려는 경우

로딩한 데이터를 파티션에 넣을때 사용합니다.

보통 운영중인 파티션 테이블을 바로 작업하게 되면 문제가 생기기 때문에

이러한 방식으로 사용을 하긴 하지만, 급한 경우가 아니라면

해당 방법 보다는 PM 작업시에, 다른 작업을 통해서 파티션 테이블의 문제를 해결합니다.

게다가 로컬 인덱스라면 크게 문제가 안되지만

글로벌 인덱스가 설정된 경우에는 REBULID를 해줘야되기 때문에

주의를 기울여서 작업을 수행하여야 한다.

```SQL
ALTER TABLE table_name1 RENAME PARTITION partition1 TO partition2; --로컬 파티션 인덱스의 파티션 명은 추가 작업 필요

ALTER TABLE table_name1 TRUNCATE PARTITION partition1;

ALTER TABLE table_name1 MODIFY PATITION p_name3 INDEXING ON;
```

파티션 이름 수정과 파티션 TRUNCATE는 다음과 같은 SQL 구문으로 수행이 가능하며,

INDEXING ON은 바로 다음에 설명 하도록 하겠다.

  

파티션에는 설정에 따라 부분 인덱스를 설정이 가능한데,

```SQL
PARTITION BY RANGE(column2) (
    , PARTITION p_name3 VALUES LESS THEN (DATE '2022-04-01') INDEXING ON 
    , PARTITION p_name4 VALUES LESS THEN (MAXVALUE) INDEXING OFF
);
```

파티션 생성 당시에 설정을 추가할 수 있다.

```SQL
CREATE INDEX table1_X1 ON table_name1 (column1, column2) INDEXING FULL;

CREATE INDEX table1_X2 ON table_name1 (column1, column2) INDEXING PARTIAL;
```

이후에 INDEX 생성을 할 때, FULL 대신 PARTIAL을 넣어서

INDEXING ON 된 파티션들에만 인덱싱 설정할 수 있다.

## SYNONYM(시너님)

오브젝트에 동의어를 부여하는 오브젝트로

주로 긴 이름을 짧게 축약하거나 스키마를 기술하지 않고 사용할 수 있게 해준다.

PUBLIC SYNONYM, PRIVATE SYNONYM 두 가지로 나뉘는데

```SQL
GRANT CREATE SYNONYM TO user1; --제거는 REVOKE
```

SYNONYM 생성은 해당 계정에 SYNONYM 권한 부여가 된 이후에 가능하다.

  

```SQL
CREATE OR REPLACE PUBLIC SYNONYM table_name1 FOR schema.table_name1

CREATE OR REPLACE SYNONYM table_name2 FOR schema.table_name2
```

주로 스키마가 많은 db라 테이블 명칭만 가지고 바로 사용할 수 있도록 하는 경우가 많다.

PUBLIC을 붙이면 공용으로 사용할 수 있는 SYNONYM이 되고

붙이지 않는다면 PRIVATE이 된다.

PRIAVATE SYNONYM은 PUBLIC SYNONYM과 달리,

사용자마다 만드는 것이기 때문에 사용하는 것이기 때문에

```SQL
CREATE OR REPLACE SYNONYM abc FOR user1.table_name2

CREATE OR REPLACE SYNONYM abc FOR user2.table_name2
```

PUBLIC과 다르게 SYNONYM 이름이 같더라도 에러가 발생되지 않는다.

## DBLINK

다른 DB에서 현재 DB로 오브젝트를 엑세스 할 수 있도록 하는 오브젝트이다.

SYNONYM과 같이 권한이 있어야 생성이 가능하다.

```SQL
CREATE PUBLIC DATABASE LINK db_link1 CONNECT TO user_name IDENTIFIED BY password USING 'oracle_sid';

CREATE DATABASE LINK db_link1
CONNECT TO user_name
IDENTIFIED BY "password"
USING '(DESCRIPTION =
(ADDRESS = (PROTOCOL = TCP)(HOST = db_ip_입력)(PORT = 1521))
(CONNECT_DATA =
    (SERVER = DEDICATED)
    (SERVICE_NAME = oracle_sid)
)
)';
```

SYNONYM과 마찬가지로 PUBLIC, PRIVATE가 존재.

db_link1은 DB LINK 명칭, user_name는 연결하고자 하는 USER,

password는 연결하고자 하는 USER의 PASSWORD를 대체해서 입력하면 된다.

생성은 어렵지만 사용법은 간단한 것이 원하는 테이블 명칭 뒤에 @db_link1 같이

DB LINK 명칭을 적으면 된다.

```sql
SELECT * FROM other_db_table@db_link1;
```

  
