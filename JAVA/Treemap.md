# TreeMap

## TreeMap?

TreeMap은 자바에서 제공하는 컬렉션 클래스 중 하나로, 키와 값을 쌍으로 저장하는 Map 인터페이스를 구현합니다. TreeMap은 내부적으로 레드-블랙 트리(Red-Black Tree) 구조를 사용하여 데이터를 저장하고 정렬한다.

## 주요 특징

1. 자동 정렬: TreeMap에 저장된 키는 자동으로 오름차순으로 정렬된다. 숫자 타입의 경우 값으로, 문자열 타입의 경우 유니코드 순으로 정렬된다.

2. 레드-블랙 트리: TreeMap은 이진 탐색 트리의 문제점을 보완한 레드-블랙 트리를 사용하여 데이터의 균형을 유지한다. 이를 통해 데이터 추가나 삭제 시에도
   트리의 균형이 유지되어 효율적인 검색이 가능하다.

3. 성능: TreeMap은 HashMap보다 데이터 추가나 삭제 시 시간이 더 걸리지만, 정렬된 상태로 데이터를 유지해야 하거나 범위 검색이 필요한 경우 유리하다.

## 주요 함수

1. put(K key, V value): 주어진 키와 값을 TreeMap에 추가합니다. 만약 키가 이미 존재하면 기존 값을 새로운 값으로 대체합니다.

```Java
map.put(1, "사과");
```

2. get(Object key): 주어진 키에 해당하는 값을 반환합니다. 키가 존재하지 않으면 null을 반환합니다.

```Java
String value = map.get(1); // "사과"
```

3. remove(Object key): 주어진 키와 그에 대응하는 값을 TreeMap에서 제거합니다.

```Java
map.remove(1);
```

4. firstKey(): TreeMap에서 가장 작은 키를 반환합니다.

```Java
Integer firstKey = map.firstKey(); // 1
```

5. lastKey(): TreeMap에서 가장 큰 키를 반환합니다.

```Java
Integer lastKey = map.lastKey(); // 3
```

6. firstEntry(): TreeMap에서 가장 작은 키와 그에 대응하는 값을 포함하는 Map.Entry 객체를 반환합니다.

```Java
Map.Entry<Integer, String> firstEntry = map.firstEntry(); // 1=사과
```

7. lastEntry(): TreeMap에서 가장 큰 키와 그에 대응하는 값을 포함하는 Map.Entry 객체를 반환합니다.

```Java
Map.Entry<Integer, String> lastEntry = map.lastEntry(); // 3=수박
```

8. subMap(K fromKey, K toKey): 주어진 범위 내의 키와 그에 대응하는 값을 포함하는 부분 맵을 반환합니다.

```Java
SortedMap<Integer, String> subMap = map.subMap(1, 3); // {1=사과, 2=복숭아}
```

9. keySet(): TreeMap에 포함된 모든 키를 반환합니다.

```Java
Set<Integer> keys = map.keySet(); // [1, 2, 3]
```

10. values(): TreeMap에 포함된 모든 값을 반환합니다.

```Java
Collection<String> values = map.values(); // [사과, 복숭아, 수박]
```
