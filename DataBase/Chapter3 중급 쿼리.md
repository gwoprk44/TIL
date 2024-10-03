# JOIN

하나의 쿼리로 다수의 테이블을 조회할 수 있다. 조인은 열 값으로 테이블 행을 연결한다.

> 조인 조건

조인할 두 테이블의 열이 기술된 조건. 평가 결과가 TRUE인 행을 반환한다.

- 카티션 곱 : 조인 조건이 없는 조인. 의도한 경우가 아니라면 조인 조건이 누락하였을 때 발생한다.
- 등가 조인 : 조인 조건이 모두 등호(=)인 조인. 값이 동일한 경우에만 행이 반환.
- 비등가 조인 : 등호 외에 다른 조인 조건이 있는 조인.

> 조인 범위

조인 범위는 이너와 아우터로 구분이 가능하다.

- INNER JOIN: 두 테이블에서 일치하는 값을 가진 행을 반환합니다.  

- LEFT JOIN(또는 LEFT OUTER JOIN)  
   왼쪽 테이블의 모든 행과 오른쪽 테이블의 일치하는 행을 반환합니다. 일치하는 항목이 없으면 오른쪽 테이블의 열에 대해 NULL 값이 반환됩니다.  

- RIGHT JOIN(또는 RIGHT OUTER JOIN)  
   오른쪽 테이블의 모든 행과 왼쪽 테이블의 일치하는 행을 반환합니다. 일치하는 항목이 없으면 왼쪽 테이블의 열에 대해 NULL 값이 반환됩니다.  

- FULL OUTER JOIN  
   왼쪽 또는 오른쪽 테이블에 일치하는 항목이 있으면 모든 행을 반환합니다. 일치하는 항목이 없으면 일치하지 않는 테이블의 열에 대해 NULL 값이 반환됩니다.

> 조인 차수

조인되는 테이블의 차수를 의미한다. 이는 조인 조건, 조인 기준에 따라 변경될 수 있다.

![|1000](Pasted%20image%2020231228202731.png)

- 2개 이상의 테이블에서 데이터를 검색하기 위해서 사용
- FROM 절에 두 개 이상의 테이블을 명시한다(View, Subquery도 가능)
- 공통된 컬럼이 없다면, 두 테이블의 공통컬럼을 가진 다른 테이블과 JOIN한 후 목표 테이블과 JOIN
- 두 테이블의 모든 조합 확인

```
SELECT * FROM TABLE1, TABLE2;
```

- 만약 테이블1, 테이블 2에 각 각 3개의 정보가 있다면 모든 데이터를 조합하므로 총 9개의 데이터가 나옴
- 조건을 걸어서 데이터에 알맞은 값을 매칭 시켜줘야 함

### SELF JOIN

- 자기 자신의 테이블과 합치는 것
- 찾고자 하는 값이 자신의 테이블에 있을 때 사용

```
SELECT a.칼럼명, b.칼럼명 ...
FROM TABLE1 A JOIN TABLE1 B
ON A.컬럼 = B.다른컬럼
```

### INNER JOIN 사용법

- 두 테이블에 교집합이라고 생각하면 된다
- 두 테이블에 공통된 값을 출력해준다

```
SELECT * FROM TABLEA
INNER JOIN TABLEB
ON TABLEA.컬럼 = TABLEB.컬럼
```

### FULL OUTER JOIN

- 두 테이블에 합집합이라고 생각하면 된다
- 공통된 값들은 공통된 값끼리 묶어져서 나오고, 공통되지 않은 값들도 모두 다 출력됨

```
SELECT [TABLEA.]속성명,[TABLEB]속성명
FROM TABLEA FULL OUTER JOIN TABLEB
ON TABLEA.컬럼 = TABLEB.컬럼
```

### LEFT OUTER JOIN

- 두 테이블 중에서 오른쪽 테이블에 조인시킬 컬럼의 값이 없는 경우에 사용한다
- 왼쪽 테이블(TABLE A)의 값은 모두 나오지만 오른쪽 테이블 (TABLE B)의 값은 매칭되는게 없으면 출력되지않음

```
SELECT * FROM TABLEA
LEFT OUTER JOIN TABLEB
ON TABLEA.칼럼 = TABLEB.칼럼
```

### RIGHT OUTER JOIN

- 두 테이블 중에서 오른쪽 테이블에 조인시킬 컬럼의 값이 없는 경우에 사용하게 된다
- 오른쪽 테이블 (TABLE B)의 값은 모두 다 나오지만 왼쪽 테이블 (TABLE A)의 값은 매칭되는게 없으면 출력되지 않음

```
SELECT * FROM TABLEB
RIGHT OUTER JOIN TABLEA
ON TABLEB.칼럼 = TABLEA.칼럼
```

# 서브 쿼리

서브 쿼리란 다른 테이블의 값을 기준으로 한 테이블에서 데이터를 검색할 수 있도록 다른 쿼리 내부에 중첩된 쿼리를 의미한다. 즉, 다른 쿼리 내부에 포함된 `SELECT` 문을 의미한다.

데이터 필터링, 정렬, 또는 그룹화와 같은 다양한 방법으로 사용된다.

## SELECT 절 서브쿼리 ( 스칼라 서브쿼리 )

**스칼라 서브쿼리 ( Scalar Subqueries )​** 라고 불리며 **SELECT 절 안에 서브쿼리가 들어있다.**

이 때, 서브쿼리의 결과는 반드시 **단일 행**이나 SUM, COUNT 등의 집계 함수를 거친 **단일 값으로 리턴**되어야 한다.

이유는 서브쿼리를 끝마친 값하나를 메인쿼리에서 SELECT 하기 때문.

예를 들어 홍길동 학생의 학과를 조회한다고 가정하면 다음과 같다.

```SQL
SELECT 학생이름,
       (  SELECT 학과.학과이름
            FROM 학과
           WHERE 학과.학과ID = 학생.학생ID ) AS 학과이름
  FROM 학생
 WHERE 학생이름 = '홍길동' ;
```

## FROM 절 서브쿼리 ( 인라인뷰 서브쿼리 )

**인라인뷰 ( Inline Views )** 라고 불리며 **FROM 절 안에 서브쿼리가 들어있다.**

이 때, 서브쿼리의 결과는 반드시 **하나의 테이블로 리턴**되어야 한다.

이유는 서브쿼리를 끝마친 테이블 하나를 메인쿼리의 FROM 에서 테이블로 잡기 때문.

수학 과목을 수강하는 학생들의 점수를 조회한다고 가정하면 다음과 같다.

```SQL
SELECT 학생이름, 수학점수
  FROM ( SELECT 학생.학생이름 AS 학생이름,
                과목.과목점수 AS 수학점수
           FROM 학생, 과목
          WHERE 학생.학생이름 = 과목.학생이름
            AND  과목.과목이름 = '수학' ) ;
```

## WHERE 절 서브쿼리 ( 중첩 서브쿼리 )

**중첩 서브쿼리 ( Nested Subqueries )** 라고 불리며 **WHERE 절 안에 서브쿼리가 들어있다.**

가장 자주 쓰이는 대중적인 서브쿼리이며 **단일행과 복수행 둘 다 리턴이 가능**하다.

이유는 서브쿼리를 끝마친 값들을 메인쿼리의 조건절을 통해 비교등을 하기 때문.

수학 과목을 수강하는 학생들의 모든 정보를 조회한다고 가정하면 다음과 같다.

```SQL
SELECT *
  FROM 학생
 WHERE 학생.학생이름 IN ( SELECT 과목.학생이름 FROM 과목 WHERE 과목.과목이름 = '수학' ) ;
```

이렇게 서브쿼리는 어느 위치에 선언하냐에 따라 나누어진다는 것을 알 수 있다.

그런데 위에서 언급한 단일행 리턴, 복수행 리턴은 무슨 말인가? 하면 아래를 참고해보자.

## 단일행 서브쿼리

- 서브쿼리의 수행결과가 **오직 하나의 ROW(행)만을 반환.**

- 이 하나의 결과를 가지고 메인쿼리는 비교연산자를 통해 쿼리를 수행함.

- 비교연산자는 **단일행 비교연산자를 사용 ( >, >=, <, <=, =, ... ).**

```SQL
// ex ) 사원들의 평균 급여보다 더 많은 급여를 받는 사원을 검색
SELECT  ENAME, SAL
  FROM  EMP
 WHERE  SAL > ( SELECT  AVG(SAL)
                  FROM  EMP);
```

## 다중행 서브쿼리

- 서브쿼리의 수행결과가 **두 건 이상의 데이터를 반환.**

- 비교연산자는 **다중행 비교연산자를 사용 ( IN, ANY, SOME, ALL, EXISTS ).**

**다중행 비교연산자가 뭐지?**

### IN

메인쿼리의 비교조건이 서브쿼리의 결과중에서 하나라도 일치하면 참.

### ALL

메인쿼리의 비교조건이 서브쿼리의 검색결과와 **모든 값**이 일치하면 참.

- **메인쿼리 < ALL ( 서브쿼리 )** : 서브쿼리의 결과와 비교하여 최소값 반환.

- **메인쿼리 > ALL ( 서브쿼리 )** : 서브쿼리의 결과와 비교하여 최대값 반환.

```SQL
// ex ) 30번 소속 사원들 중 급여를 가장 많이 받는 사원보다 더 많은 급여를 받는 사람의 이름과 급여를 출력
SELECT  ENAME, SAL
  FROM  EMP
 WHERE  SAL > ALL ( SELECT  SAL
                      FROM  EMP
                     WHERE  DEPTNO = 30 );
```

### ANY

메인쿼리의 비교조건이 서브쿼리의 검색결과와 **하나 이상**이 일치하면 참.

- **메인쿼리 < ANY ( 서브쿼리 )** : 서브쿼리의 결과와 비교해 메인쿼리의 데이터중 한개라도 서브쿼리 결과보다 작다면 최소값 반환.

- **메인쿼리 > ANY ( 서브쿼리 )** : 서브쿼리의 결과와 비교해 메인쿼리의 데이터중 한개라도 서브쿼리 결과보다 크다면 최대값 반환.

### EXISTS

메인쿼리의 비교조건이 서브쿼리의 검색결과중에 **하나라도 만족하는 값이 존재**하면 참.

EXISTS 는 해당 로우가 존재하는지의 여부만 확인.

IN은 실제 존재하는 데이터들의 모든 값까지 확인.

NOT EXISTS는 메인쿼리의 컬럼명과 서브쿼리의 컬럼명을 비교하여 일치하지 않으면 메인쿼리 테이블의 모든 ROW(행)을 반환.

# 집합 연산자

> 테이블을 구성하는 행집합에 대해 테이블의 부분집합을 결과로 반환하는 연산자를 의미한다.

|                 |                                            |
| --------------- | ------------------------------------------ |
| **집합 연산자** | **의미**                                   |
| **UNION**       | 두 집합에 대해 중복되는 행을 제외한 합집합 |
| **UNION ALL**   | 두 집합에 대해 중복되는 행을 포함한 합집합 |
| **MINUS**       | 두 집합 간의 차집합                        |
| **INTERSECT**   | 두 집합 간의 교집합                        |

집합 연산자 사용법은 다음과 같다.

```sql
SELECT 명령문1 [UNION | UNION ALL | INTERSECT | MINUS] SELECT 명령문2;
```

## UNION 연산자 (합집합)

UNION ALL 연산자는 데이터 집합을 수평으로 연결하여 기술 순서대로 데이터 집합이 반환된다.

UNION 연산자는 중복 값이 제거된 합집합을 새성한다. 이 때, 중복값을 제거하기 위해 SORT가 발생.

![](https://blog.kakaocdn.net/dn/bOFFSJ/btrvocYODM7/KCzyEOnH6KJ41mZsxMk3Hk/img.png)

## INTERSECT 연산자

중복값이 제거된 교집합을 생성한다. 이 때 SORT가 발생한다.

![](Pasted%20image%2020231230133611.png)

## MINUS 연산자

중복 값이 제거된 차집합을 생성한다. 역시, SORT가 발생한다.

![](Pasted%20image%2020231230133650.png)

# Top-N 쿼리

상위 N개의 행을 조회하는 문법.

ROWNUM / TOP N / ROW LIMITING 세 가지 방식으로 사용이 가능하다.

## ROWNUM

ORACLE에서 가져오고자하는 행 수를 제한할 때 `ROWNUM < 2` 와 같이 사용 가능하다.

ORDER BY로 정렬된 순의 데이터를 출력하고자 하면 ROWNUM을 잘못 사용하고 있는 것이다.

```SQL
SELECT ENAME, SAM FROM EMP
WHERE ROWNUM < 4
ORDER BY SAL DESC;
```

위와 같이 SQL문을 작성하면 ORDER BY는 결과에 영향을 주지 않기 때문에

랜덤으로 데이터를 3개 뽑은 후 데이터를 정렬해서 출력한다.

**ORDER BY 가 없다면 ORACLE 의 ROWNUM 조건과 SQL SERVER 의 TOP 절은 같은 결과를 보인다.**

ORDER BY 절이 사용되는 경우 ORACLE 은 ROWNUM 조건을 ORDER BY 절보다 먼저 처리되는 WHERE 절에서

처리하므로, **정렬 후 원하는 데이터를 얻기 위해 인라인 뷰에서 먼저 데이터를 정렬한 후 메인 쿼리에서 ROWNUM 을 사용해야한다.**

```SQL
SELECT ENAME, SAL
	FROM ( SELECT ENAME, SAL
    	FROM EMP
        ORDER BY SAL DESC
        )
WHERE ROWNUM <=3;
```

## TOP 절

결과 집합으로 나오는 행의 수를 제한할 수 있다.

**TOP (N) WITH TIES 로 동일 수치의 데이터는 추가로 더 추출**할 수 있다.

```SQL
SELECT TOP(2) WITH TIES
	ENAME,SAL
    FROM EMP
ORDER BY SAL DESC;
```

## ROW LIMITING 절

ORDER BY 절 다음에 기술하며, *ORDER BY 와 함께 수행된다.*

ROW와 ROWS 의 구분은 없다.

[ FETCH { FIRST | NEXT } [ { rowcount | percent PERCENT } ] { ROW | ROWS } { ONLY | WITH TIES } ]

**FETCH** : 반환할 행의 개수나 백분율을 지정한다.

**ONLY** : 지정된 행의 개수나 백분율만큼 행을 반환한다.

**WITH TIES** : 마지막 행에 대한 동순위를 포함해서 반환한다.

```SQL
SELECT EMPNO, SAL
FROM EMP
ORDER BY SAL,
EMPNO FETCH FIRST 5 ROWS ONLY;
```

[ OFFSET offset { ROW | ROWS } ]

**OFFSET** offset : 건너뛸 행의 개수를 지정한다.

```SQL
SELECT EMPNO, SAL
FROM EMP
ORDER BY SAL,
EMPNO OFFSET 5 ROWS;
```

# 계층 쿼리

계층 쿼리를 사용하면 순환 관계(Recursive) 관계를 가진 데이터를 조회 가능하다.

|                      |                                                                                                                           |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| **LEVEL**            | 현재 테이블에는 존재하지 않는 컬럼 <br>오라클의 모든 SQL에서 사용할수 있는 것으로 해당 데이터가 몇 번째 단계이냐를 의미함 |
| **START WITH**       | 어디부터 시작할 것인지를 정함 ( 최상위 레코드 )                                                                           |
| **CONNECT BY PRIOR** | 계층 구조에서 각 행의 연결 관계를 설정 <br>EMP 테이블에서 EMPNO, MGR 같은                                                 |
| **PRIOR**            | 어디부터 시작할 것인지를 정해 준다.                                                                                       |

## START WITH

계층 질의의 루트(부모행)로 사용될 행을 지정.

서브 쿼리를 사용할 수도 있다.

STAART WITH 구문에서는 어떤 레코드를 최상위 레코드로 결정할지 지정한다.

## CONNECT_BY_ROOT

계층구조 쿼리에서 LEVEL이 0인 최상위 ROW의 정볼를 얻어올 수 있다.

PRIOR 연산자와 함께 사용하여 계층구조로 표현 가능하다.

> CONNECT BY PRIOR 자식컬럼 = 부모컬럼

부모에서 자식으로 트리를 구성(Top Down)

> CONNECT BY PRIOR 부모컬럼 = 자식컬럼

자식에서 부모로 트리를 구성(Bottom Up)

```SQL
SELECT LPAD(' ', 4*(LEVEL-1)) || ename ename, empno,
CONNECT_BY_ROOT  empno "Root empno", level
  FROM emp
 START WITH job='PRESIDENT'
CONNECT BY PRIOR empno=mgr;


ENAME                    EMPNO  Root empno     LEVEL
------------------     ------- -----------   -------
KING                      7839    7839           1
    JONES                 7566    7839           2
        SCOTT             7788    7839           3
            ADAMS         7876    7839           4
        FORD              7902    7839           3
            SMITH         7369    7839           4
```

![](https://blog.kakaocdn.net/dn/LTJEK/btrASvJ5DLQ/caSc4E1fE33kfgZ2lDghVK/img.png)

계층형 구조 (하향식)

## CONNECT_BY_ISLEAF

**계층구조 쿼리에서 로우의 최하위 레벨(Leaf) 여부를 반환. 최하위 레벨이면 1, 아니면 0**

```SQL
SELECT LPAD(' ', 4*(LEVEL-1)) || ename ename, empno,
       CONNECT_BY_ISLEAF "leaf", level
  FROM emp
 START WITH job='PRESIDENT'
CONNECT BY NOCYCLE PRIOR empno=mgr;


ENAME                     EMPNO       leaf      LEVEL
-------------------- ---------- ---------- ----------
KING                       7839          0          1
    JONES                  7566          0          2
        SCOTT              7788          0          3
            ADAMS          7876          1          4
        FORD               7902          1          3
    BLAKE                  7698          0          2
        MARTIN             7654          1          3
        TURNER             7844          1          3
        JAMES              7900          1          3
    CLARK                  7782          0          2
        MILLER             7934          1          3
```

## SYS_CONNECT_BY_PATH  

**계층구조 쿼리에서 현재 로우 까지의 PATH 정보를 가져올수 있다.**

```SQL
-- 사이즈조절
COL PATH FORMAT A40


-- SYS_CONNECT_BY_PATH 예제
SELECT LPAD(' ', 4*(LEVEL-1)) || ename ename, empno,
       SYS_CONNECT_BY_PATH(ename, '/') "PATH"
  FROM emp
 START WITH job='PRESIDENT'
CONNECT BY PRIOR empno=mgr;


ENAME                     EMPNO PATH
-------------------- ---------- ---------------------------
KING                       7839 /KING
    JONES                  7566 /KING/JONES
        SCOTT              7788 /KING/JONES/SCOTT
            ADAMS          7876 /KING/JONES/SCOTT/ADAMS
        FORD               7902 /KING/JONES/FORD
    BLAKE                  7698 /KING/BLAKE
        MARTIN             7654 /KING/BLAKE/MARTIN
        TURNER             7844 /KING/BLAKE/TURNER
        JAMES              7900 /KING/BLAKE/JAMES
    CLARK                  7782 /KING/CLARK
        MILLER             7934 /KING/CLARK/MILLER
```

**Leaf Node(최하위, 맨 마지막 끝) 의 전체 PATH 정보를 출력**

```SQL
SELECT LEVEL, SUBSTR(SYS_CONNECT_BY_PATH(ename, ','), 2) path
  FROM emp
 WHERE CONNECT_BY_ISLEAF = 1
 START WITH mgr IS NULL
CONNECT BY PRIOR empno = mgr;


    LEVEL PATH
--------- -------------------------
        4 KING,JONES,SCOTT,ADAMS
        3 KING,JONES,FORD
        3 KING,BLAKE,MARTIN
        3 KING,BLAKE,TURNER
        3 KING,BLAKE,JAMES
        3 KING,CLARK,MILLER
```

## ORDER SIBLINGS BY  

**계층구조 쿼리에서 상관관계를 유지하면서 정렬을 할 수 있게 해준다.**

```SQL
COL ename FORMAT A25
COL ename2 FORMAT A10


-- ORDER SIBLINGS BY 예
-- 정렬이 정상적으로 수행된 것을 확인
SELECT LPAD(' ', 4*(LEVEL-1)) || ename ename,
       ename ename2, empno, level
  FROM emp
 START WITH job='PRESIDENT'
CONNECT BY NOCYCLE PRIOR empno=mgr
 ORDER SIBLINGS BY ename2;


ENAME                ENAME2          EMPNO      LEVEL
-------------------- ---------- ---------- ----------
KING                 KING             7839          1
    BLAKE            BLAKE            7698          2
        JAMES        JAMES            7900          3
        MARTIN       MARTIN           7654          3
        TURNER       TURNER           7844          3
    CLARK            CLARK            7782          2
        MILLER       MILLER           7934          3
    JONES            JONES            7566          2
        FORD         FORD             7902          3
        SCOTT        SCOTT            7788          3
            ADAMS    ADAMS            7876          4




-- ORDER BY 예
-- 정렬이 이상하게 수행된 것을 확인
SELECT LPAD(' ', 4*(LEVEL-1)) || ename ename,
       ename ename2, empno, level
  FROM emp
 START WITH job='PRESIDENT'
CONNECT BY NOCYCLE PRIOR empno=mgr
 ORDER BY ename2;


ENAME                ENAME2          EMPNO      LEVEL
-------------------- ---------- ---------- ----------
            ADAMS    ADAMS            7876          4
    BLAKE            BLAKE            7698          2
    CLARK            CLARK            7782          2
        FORD         FORD             7902          3
        JAMES        JAMES            7900          3
    JONES            JONES            7566          2
KING                 KING             7839          1
        MARTIN       MARTIN           7654          3
        MILLER       MILLER           7934          3
        SCOTT        SCOTT            7788          3
        TURNER       TURNER           7844          3
```
