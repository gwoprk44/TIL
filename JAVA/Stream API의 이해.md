# Stream API란?

JDK8부터 Stream API와 람다식, 함수형 인터페이스 등을 지원하면서 Java로 함수형 프로그래밍을 할 수 있는 API를 제공하고 있다. 이 중 Stream API에는 데이터를 처리하는데 자주 사용되는 함수들을 정의해 두었다. 이 API 를 이용하면 컬렉션 요소를 하나씩 차례대로 처리하여 원하는 결과를 얻을 수 있다. 필터링, 매핑, 정렬 등 다양한 방식으로 가공을 하며 코드의 가독성과 성능을 높일 수 있다는 특징이 있다.

---

# [Stream API을 사용하지 않았을 때]

nameList를 선언해서 정렬 후 출력하는 코드다.
충분히 잘 짜여진 코드지만, 이를 Stream API를 이용하여 더 간결하게 정리할 수 있다.

```java
List<String> nameList = new ArrayList<>(Arrays.asList("kang","yoon","park"));

//원본 데이터 정렬
Collections.sort(nameList);

//원본 데이터 출럭
for (String str : nameList) {
    System.out.println(str);
}
```

---

# [Stream API을 사용했을 때]

아래와 같이 Stream API를 사용하면 데이터 원본을 변경하지 않고, 별도의 stream을 생성하여 더 간결하고 가독성 있게 구현할 수 있다.
Stream은 일회용이기 때문에 한번 사용이 끝나면, 재사용이 불가능하다는 특징이 있다.
Stream은 내부적으로 반복하는 문법을 forEach() 메서드에 숨기고 있기 때문에, 보다 간결한 코드 작성이 가능하다.

```java
List<String> nameList = new ArrayList<>(Arrays.asList("kang","yoon","park"));

//원본 데이터가 아닌 별도의 stream 생성
Stream<String> nameStream = nameList.stream();

//데이터 정렬 후 출력
nameStream.sorted().forEach(System.out::println);
```

---

# Stream 연산 종류

## Stream 생성

Stream API를 사용하기 위해 Stream 객체를 생성하는 단계이다. Stream API는 다양한 데이터로부터 스트림을 생성할 수 있다. ex) 배열 스트림 , 리스트 스트림, 원시 스트림 ..

아래 예제로 살펴보자.

```java
//배열에서 스트림 생성
String[] animals = {"lion", "tiger"};
Stream stream1 = Arrays.stream(animals);

//리스트에서 스트림 생성
ArrayList<String> nameList = new ArrayList<>(Arrays.asList("kang","yoon"));
Stream<String> stream2 = nameList.stream();

//원시 스트림 생성
Stream<String> stream3 = Stream.of("a","b","c");
Stream<String> stream4 = Arrays.stream(new String[] {"a","b","c"});
```

※Collection 인터페이스에는 steram()메서드가 정의되어 있기 때문에 Collection인터페이스를 구현한 객체들은 모두 .stream()메서드를 이용하여 생성이 가능하다.

## 가공(중간 연산)

원본의 데이터를 별도의 데이터로 가공하기 위한 중간 연산이다. 연산 결과를 Stream으로 다시 반환하기 때문에 연속해서 중간 연산을 이어 갈 수 있다. ex) filter(필터링), map(데이터 변환), sorted(정렬), distinct(중복 제거) ..

아래 예시 코드를 보면서 살펴보자.

```java
public class StreamTest {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("david", "david", "yoon", "park");

        //filter 예시
        List<String> filteredNames = names.stream()
                .filter(name -> name.length() > 4) //길이가 4 초과인 문자열 필터
                .collect(Collectors.toList());

        System.out.println(filteredNames); //[david, david]

        //map 예시
        List<String> upperCaseNames = names.stream()
                .map(String::toUpperCase) //대문자로 변환
                .collect(Collectors.toList());

        System.out.println(upperCaseNames); //[DAVID, DAVID, YOON, PARK]

        //distinct 예시
        List<String> distinctNames = names.stream()
                .distinct() //중복 객체 제거
                .collect(Collectors.toList());

        System.out.println(distinctNames); //[david, yoon, park]
    }
}
```

※distinct()는 중복된 데이터를 검사하기 위해 equals()메서드를 사용한다. 따라서 만약 내가 만든 클래스를 Stream으로 사용하고자 하면, equals(), hashCode()메서드를 오버라이드하여 별도로 구현해야 한다.

메서드를 별도로 구현하지 않는다면, 아래 예시와 같이 같은 animal객체를 갖는 animalList Stream에 distinct로 가공해도 size는 1이 아닌 2가 나올 것이다.

```java
public class distinctTest {
    public static void main(String[] args){
        Animal animal1 = new Animal("Lion",4);
        Animal animal2 = new Animal("Lion",4);

        List<Animal> animalList =new ArrayList<>();
        animalList.add(animal1);
        animalList.add(animal2);

        int size = animalList
                .stream()
                .distinct()
                .collect(Collectors.toList())
                .size();
        System.out.println(size); //1이 나와야 되는데 2 출력
    }
}
```

따라서 아래와 같이 Animal 클래스에 name과 age가 같으면 같은 객체로 생각한다는 equals()메서드와 hashCode()메서드를 재정의 해주면, size는 2가 아닌 1이 나온다.

```java
public class Animal {

    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        //현재 객체와 o의 참조값이 같은지 확인
        if (this == o)
            return true;

        //o가 Animal의 인스턴스인지 확인
        if (!(o instanceof Animal))
            return false;

        //값을 비교하기 위해 Animal객체로 casting
        Animal animal = (Animal) o;

        //name이 String이므로 equals()로 비교 -> NullPointerException 발생 X
        return Objects.equals(name, animal.name);
    }

    @Override
    public int hashCode() {
        //name, age필드의 해시코드를 반환
        //만약 equals()메서드가 name필드만을 기준으로 객체를 비교했다면,
        //Objecdts.hash(name)을 반환하도록 구현해야 한다.
        //동일한 name, age필드를 가진 Animal객체는 동일한 해시코드를 반환하게 된다.
        return Objects.hash(name);
    }

}
```

## 최종 연산

중간 연산을 통해 생성된 Stream을 바탕으로 결과를 만든다. 결과 생성을 위한 최종 연산에는 다음과 같은 것이 있다.

ex) max(최댓값), sum(합), average(평균), count(갯수), collect(원하는 컬렉션으로 변환)

```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
//min()
int min = list.stream().min(Integer::compareTo).orElse(0);
System.out.println(min); //1

//max()
int max = list.stream().max(Integer::compareTo).orElse(0);
System.out.println(max); //5

//average()
double average = list.stream().mapToInt(Integer::intValue).average().orElse(0.0);
System.out.println(average); //3.0

//collect()
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Set<Integer> even = numbers.stream()
                            .filter(n -> n % 2 == 0)
                            .collect(Collectors.toSet()); // 요소들을 set으로 변환
```

---

# 정리

Stream 연산 세가지에 대해서 간략하게만 살펴보았다. 자세한 기능은 1,2,3단계를 모두 사용한 예시를 보며 정리해보자.

```java
List<Integer> numList = Arrays.asList(3, 7, 2, 9, 5, 1);

numList.stream() //컬렉션에서 스트림 생성
        .sorted(Comparator.reverseOrder()) // 내림차순 정렬
        .filter(n -> n > 6) //조건에 맞게 필터링
        .map(n -> n*2) //가공
        .forEach(System.out::println); //18 14 출력
```
