---
aliases:
  - DB
tags:
  - Resource
  - Dev
  - DB
---

# SELECT문

데이터를 조회하는 구문으로서 기본 SELECT문은 SELECT 절과 FROM 절로 구성된다.

```SQL
SELECT 절
FROM 절;
```

SELECT절은 조회할 `열이나 표현식`을 기술하며 FROM절은 조회할 `테이블`을 기술한다.

## DISTINCT 키워드

SELECT 절에 DISTINCT 키워드나 UNIQUE 키워드를 기술하면 `중복 행`이 제거된 결과가 반환된다. 

`DISTINCT | UNIQUE | ALL`

## 표현식

값으로 평가될 수 있는 리터럴, 연산자, SQL 함수 등의 조합이다.

> CASE 표현식

IF-THEN-ELSE의 논리를 평가할 수 있다. 단순 CASE 표현식과 검색 CASE 표현식을 사용 가능하다.

> 단순 CASE 표현식

``` SQL
CASE expr
	{When comparison_expr THEN return_expr}
	[ELSE else_expr]
END
```

expr과 comparison_expr이 일치하는 첫 번째 return_expr, 일치하는 comparison_expr이 없으면 else_expr을 반환한다.

단순 CASE 표현식은 등가 비교만 가능하며 ELSE 절을 기술하지 않을 시 일치하는 WHEN 절이 없는 경우 널값이 반환된다.

> 검색 CASE 표현식

```
CASE 
	{WHEN condition THEN return_expr}
	{ELSE else_expr}
END
```

검색 CASE 표현식은 condition이 TRUE인 첫 번째 return_expr을 반환한다. TRUE인 condition이 없다면 else_expr이 반환된다.

# 단일 행 함수

## NVL 함수, NVL2 함수

NVL 함수는 expl이 널이 아니면 expr1, 널이면 expr2 반환한다. `NVL (expr1, expr2)`

NVL2 함수는 expl1이 널이 아니면 expr2, 널이면 expr3를 반환한다. `NVL2 (expr1, expr2, expr3)`

# WHERE 절

WHERE 절을 사용하면 행을 `선택`하여 조회할 수 있다.

```SQL
SELECT 절
FROM 절
WHERE 절;
```

`WHERE condition` WHERE절의 구문은 왼쪽과 같고. 조건은 행마다 평가되며 TRUE, FALSE, UNKNOWN 중 하나의 값을 반환.

## 비교 조건

> ANY 조건

목록의 일부를 비교하여 조건을 만족하는 행을 반환.

> ALL 조건

목록의 전체를 비교하여 조건을 만족하는 행을 반환. 목록을 모두 만족하여야 행이 반환.

## IN 조건

expr1이 expr2의 목록에 포함되는 행을 반환.

`expr [NOT] IN (expr2 [, exrp2])`

IN 조건은 OR 조건으로 평가되고 NOT IN 조건은 AND 조건으로 평가된다.

## LIKE 조건

char1이 char2 패턴과 일치하는 행을 반환. char1과 char2는 `문자 값`을 사용. 이를 패턴 일치 조건이라 부르기도 한다.

`char1 [NOT] LIKE char2 [ESCAPE esc_char]`

이 때 char2에 `%, -` 두 개의 특수 문자를 사용 가능하다. `%`는 0개 이상의 문자와 일치, `-`는 하나의 문자와 일치하는 행 조회가 가능하다.


# ORDER BY 절


SELECT 문의 결과를 정렬 할 때 사용한다.

- ASC : 오름차순 정렬
- DESC : 내림차순 정렬
- NULLS FIRST : 널을 앞쪽으로 정렬
- NULL LAST : 널을 뒤쪽으로 정렬

```sql
SELECT * FROM table_name

ORDER BY column_name ( ASC , DESC )
```

# GROUP BY 절과 HAVING 절

GROUP BY 절은 행 그룹 생성, HAVING 절은 조회할 행 그룹을 선택한다.

GROUP BY 는 같은 값을 가진 행끼리 하나의 그룹으로 뭉쳐줍니다. 예시를 보면 좀 더 이해하시기 쉬울 겁니다. 예를 들어 우리가 서점을 운영하고 있다고 가정해봅시다. 우리는 우리 서점이 몇 가지의 장르의 책을 보유하고 있으며, 각 장르마다 몇 개의 책을 보유하고 있는지 알고 싶습니다. 우리의 데이터베이스는 1개의 테이블로 구성이 되어있는데, 해당 테이블은 책 이름과 장르 그리고 재고의 숫자를 저장하고 있습니다. 아래 그림은 GROUP BY 가 데이터를 그룹핑하는 것을 시각적으로 보여줍니다. 각 장르마다의 책 재고 개수를 알고 싶은 것이기 때문에, GROUP BY 가 장르를 기준으로 그룹을 나눈 후, 각 그룹에 해당하는 값(여기서는 재고의 총합)을 계산합니다. 

![](https://blog.kakaocdn.net/dn/1kPUl/btqTEMascrt/P6HbT25ol4cWR4LfKDRK4k/img.png)


이번에는 좀 새로운 문제에 맞닥트렸습니다. 우리는 일별 평균 티겟 금액을 보고 싶습니다. 근데 여기서 추가 조건이 있어요. 방문 고객 수가 3명보다 적은 날짜는 제외하고 싶습니다. 이 조건을 달려면 우리는 쿼리문을 조금 다르게 작성해야 합니다. 한 번 보시죠.

> SELECT  
>  date,  
>  ROUND(**AVG(price)**, 2) AS avg_price  
> FROM visit  
> **GROUP BY** date  
> **HAVING** COUNG(*) > 3  
> ORDER BY date;

위 쿼리문에서 새로운 부분은 `HAVING COUNT(*) > 3 입니다.` HAVING 절은 GROUP BY 를 통해 데이터를 그룹핑 한 행에만 사용할 수 있습니다. 이 경우, 우리는 날짜로 이미 데이터를 그룹화하였기에 HAVING 절을 사용할 수 있었습니다. 날짜별로 그룹 지어진 데이터들의 개수가 3개보다 많아야 우리가 원하는 결과를 얻을 수 있습니다. 만약 그룹 지어진 데이터들이 이 조건을 만족하지 못한다면 결과에는 나타나지 않습니다. 위의 쿼리문을 실행하면 아래와 같은 결과를 얻게 됩니다.

|   |   |
|---|---|
|date|avg_price|
|2020-05-01|5.80|
|2020-05-15|7.00|
|2020-05-23|6.67|
|...||

