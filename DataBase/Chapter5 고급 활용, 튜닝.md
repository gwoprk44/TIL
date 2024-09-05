---
aliases:
  - DB
tags:
  - Resource
  - Dev
  - DB
---

# 인덱스 스캔

##  Index Range Scan

- 인덱스 루트 블록에서 리프 블록까지 수직적으로 탐색한 후에 리프 블록을 필요한 범위(Range)만 스캔하는 방식
- B\Tree 인덱스의 가장 일반적이고 정상적인 형태의 액세스 방식
- Index Range Scan 실행계획

```sql
Execution Plan ------------------------------------------------------
0 SELECT STATEMENT Optimizer=ALL_ROWS
1 0 TABLE ACCESS (BY INDEX ROWID) OF 'EMP' (TABLE)
2 1   INDEX (RANGE SCAN) OF 'EMP_DEPTNO_IDX' (INDEX)
```

## Index Full Scan

- 수직적 탐색없이 인덱스 리프 블록을 처음부터 끝까지 수평적으로 탐색하는 방식
- 대개는 데이터 검색을 위한 최적의 인덱스가 없을 때 차선으로 선택
- Index Full Scan 실행계획

```sql
Execution Plan ----------------------------------------------------------
0 SELECT STATEMENT Optimizer=ALL_ROWS
1 0 TABLE ACCESS (BY INDEX ROWID) OF 'EMP' (TABLE)
2 1   INDEX (FULL SCAN) OF 'EMP_IDX' (INDEX)
```

### Index Full Scan의 효용성

- 인덱스 선두 칼럼(ename)이 조건절에 없으면 옵티마이저는 우선적으로 Table Full Scan을 고려
- 대용량 테이블이어서 Table Full Scan의 부담이 크다면 ?  
    - 데이터 저장공간은 ‘가로×세로’ 즉, ‘칼럼길이×레코드수’에 의해 결정되므로  
    대개 인덱스가 차지하는 면적은 테이블보다 훨씬 적게 마련

#### 연봉이 5,000을 초과하는 사원이 전체 중 극히 일부라면?

- 만약 인덱스 스캔 단계에서 대부분 레코드를 필터링하고 일부에 대해서만 테이블 액세스가 발생하는 경우  
    => 옵티마이저는 Index Full Scan 방식을 선택한다.

#### 인덱스를 이용한 소트 연산을 대체하는 경우

- Index Full Scan은 Index Range Scan과 마찬가지로 그 결과집합이 인덱스 칼럼 순으로 정렬된다.  
    => Sort Order By 연산을 생략할 목적으로 사용될 수도 있다.

```sql
SQL> select /*+ first_rows */ * from emp  where sal > 1000  order by ename;
 Execution Plan --------------------------------------------------
0 SELECT STATEMENT Optimizer=HINT: FIRST_ROWS
1 0 TABLE ACCESS (BY INDEX ROWID) OF 'EMP' (TABLE)
2 1   INDEX (FULL SCAN) OF 'EMP_IDX' (INDEX)
```

- 대부분 사원의 연봉이 1,000을 초과하므로 Index Full Scan을 하면 거의 모든 레코드에 대해 테이블 액세스가 발생한다. 따라서 Table Full Scan 보다 오히려 불리하다.
- 만약 SAL이 인덱스 선두 칼럼이어서 Index Range Scan 하더라도 마찬가지다.그럼에도 여기서 인덱스가 사용된 것은 사용자가 first_rows 힌트를 이용해 옵티마이저 모드를 바꾸었기 때문이다.
- 소트 연산을 생략함으로써 전체 집합 중 처음 일부만을 빠르게 리턴할 목적으로 Index Full Scan 방식이 선택되었다.
- 사용자가 그러나 처음 의도와 다르게 데이터 읽기를 멈추지 않고 끝까지 fetch 한다면 Full Table Scan한 것보다 훨씬 더 많은 I/O를 일으키면서 서버 자원을 낭비하게 된다.

## Index Unique Scan

- 수직적 탐색만으로 데이터를 찾는 스캔 방식
- Unique 인덱스를 ‘=’ 조건으로 탐색하는 경우에 작동
- Index Unique Scan 실행계획

```sql
SQL> select empno, ename from emp where empno = 7788;
Execution Plan -----------------------------------------------
0 SELECT STATEMENT Optimizer=ALL_ROWS
1 0 TABLE ACCESS (BY INDEX ROWID) OF 'EMP'
2 1   INDEX (UNIQUE SCAN) OF 'PK_EMP' (UNIQUE)
```

## Index Skip Scan

- 인덱스 선두 칼럼이 조건절에 빠졌어도 인덱스를 활용하는 새로운 스캔방식(Oracle 9i버전 이상)
- 인덱스 선두 칼럼이 조건절로 사용되지 않으면 옵티마이저는 기본적으로 Table Full Scan을 선택한다.
- 또는, Table Full Scan보다 I/O를 줄일 수 있거나 정렬된 결과를 쉽게 얻을 수 있다면 Index Full Scan 방식을 사용한다.
- Index Skip Scan 실행계획 : 성별과 연봉 두 칼럼으로 구성된 결합 인덱스에서 선두 칼럼인 성별 조건이 빠진 SQL문이 Index Skip Scan 방식으로 수행될 때

```sql
SQL> select * from 사원 where 연봉 between 2000 and 4000;
Execution Plan --------------------------------------------------
0 SELECT STATEMENT Optimizer=ALL_ROWS
1 0 TABLE ACCESS (BY INDEX ROWID) OF '사원' (TABLE)
2 1   INDEX (SKIP SCAN) OF '사원_IDX' (INDEX)
```
### Index Skip Scan 내부 수행원리

- 루트 또는 브랜치 블록에서 읽은 칼럼 값 정보를 이용해 조건에 부합하는 레코드를 포함할 “**가능성이 있는**”  
    하위 블록(브랜치 또는 리프 블록)만 골라서 액세스하는 방식
- 조건절에 빠진 인덱스 선두 칼럼의 Distinct Value 개수가 적고 후행 칼럼의 Distinct Value 개수가 많을 때 유용하다.
### Index Skip Scan 대안 : In-List

- Index Skip Scan에 의존하는 대신, 아래와 같이 성별 값을 In-List로 제공하는 방식
- INLIST ITERATOR라고 표시된 부분 => 조건절 In-List에 제공된 값의 종류만큼 인덱스 탐색을 반복 수행
- 직접 성별에 대한 조건식을 추가해 주면 Index Skip Scan에 의존하지 않고도 빠르게 결과집합을 얻을 수 있다.  
    단, 효과를 발휘하려면 In-List로 제공하는 값의 종류가 적어야 한다.

```sql
SQL> select * from 사원  where 연봉 between 2000 and 4000  and 성별 in ('남', '여')
Execution Plan --------------------------------------------------
0 SELECT STATEMENT Optimizer=ALL_ROWS
1 0 INLIST ITERATOR
2 1 TABLE ACCESS (BY INDEX ROWID) OF '사원' (TABLE)
3 2   INDEX (RANGE SCAN) OF '사원_IDX' (INDEX)
```

## Index Fast Full Scan

- Index Full Scan보다 빠르다.
- Index Fast Full Scan이 Index Full Scan보다 빠른 이유  
    => 인덱스 트리 구조를 무시하고 인덱스 세그먼트 전체를 Multiblock Read 방식으로 스캔

## Index Range Scan Descending

- 인덱스를 뒤에서부터 앞쪽으로 스캔하기 때문에 내림차순으로 정렬된 결과집합을 얻는다.

```sql
SQL> select * from emp  where empno is not null  order by empno desc
Execution Plan -------------------------------------------------------------
0 SELECT STATEMENT Optimizer=ALL_ROWS
1 0 TABLE ACCESS (BY INDEX ROWID) OF 'EMP' (TABLE)
2 1   INDEX (RANGE SCAN DESCENDING) OF 'PK_EMP' (INDEX (UNIQUE))
```

- max 값을 구하고자 할 때도 해당 칼럼에 인덱스가 있으면 인덱스를 뒤에서부터 한 건만 읽고 멈추는 실행계획이 자동으로 수립된다.

```sql
SQL> create index emp_x02 on emp(deptno, sal);
SQL> select deptno, dname, loc 2 ,(select max(sal) from emp where deptno = d.deptno) 3 from dept d
Execution Plan -------------------------------------------------------------
0 SELECT STATEMENT Optimizer=ALL_ROWS
1 0 SORT (AGGREGATE)
2 1 FIRST ROW
3 2  INDEX (RANGE SCAN (MIN/MAX)) OF 'EMP_X02' (INDEX)
4 0 TABLE ACCESS (FULL) OF 'DEPT' (TABLE)
```
