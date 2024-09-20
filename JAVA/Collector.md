# Collector이란?

java의 Collector 인터페이스는 Stream API에서 데이터를 수집하는 방법을 정의하는데 사용됩니다. 여러 가지 메서드와 기능이 있으며, 이를 통해 스트림의 요소를 다양한 형태로 집계할 수 있습니다. 주요 메서드와 그 설명은 다음과 같다.

## 주요 메서드

### toList()

스트림의 요소를 List로 수집합니다.

```java
List<String> list = stream.collect(Collectors.toList());
```

### toSet()

스트림의 요소를 Set으로 수집합니다. 중복된 요소는 제거됩니다.

```java
Set<String> set = stream.collect(Collectors.toSet());
```

### toMap()

스트림의 요소를 키-값 쌍으로 매핑하여 Map으로 수집합니다.

```java
Map<Integer, String> map = stream.collect(Collectors.toMap(String::length, Function.identity()));
```

### joining()

스트림의 문자열 요소를 연결하여 하나의 문자열로 만듭니다. 구분자를 지정할 수 있습니다.

```java
String result = stream.collect(Collectors.joining(", "));
```

### groupingBy()

스트림의 요소를 주어진 분류 함수에 따라 그룹화하여 Map으로 수집합니다.

```java
Map<Integer, List<String>> groupedByLength = stream.collect(Collectors.groupingBy(String::length));
```

### partitioningBy()

스트림의 요소를 두 그룹으로 나누어 Map<Boolean, List<T>> 형태로 수집합니다.

```java
Map<Boolean, List<String>> partitioned = stream.collect(Collectors.partitioningBy(s -> s.length() > 5));
```

### counting()

스트림의 요소 개수를 세어 Long으로 반환합니다.

```java
long count = stream.collect(Collectors.counting());
```

### summarizingInt()

스트림의 요소에 대해 통계 정보를 수집합니다(최솟값, 최댓값, 평균, 합계 등).

```java
IntSummaryStatistics stats = stream.collect(Collectors.summarizingInt(String::length));
```

### reducing()

스트림의 요소를 지정된 연산으로 결합하여 하나의 결과를 생성합니다

```java
Optional<String> concatenated = stream.collect(Collectors.reducing((s1, s2) -> s1 + s2));
```

---

# 특징

유연성: 다양한 형태로 데이터를 수집할 수 있어 여러 상황에서 유용하게 사용됩니다.

병렬 처리 지원: Collector는 병렬 스트림에서도 사용할 수 있으며, 효율적인 데이터 처리를 지원합니다.

타입 안전성: 제네릭을 사용하여 타입 안전성을 제공합니다.
