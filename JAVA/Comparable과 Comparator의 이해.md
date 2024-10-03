# Comparable

---

`Comparable`은 `Java.lang` package에 있는 `인터페이스`이며 정렬을 위해 사용되는데, 보통 `기본형(Primitive Type)`을 정렬하는데 사용하지는 않습니다.

`Comparable`을 사용하는것은 `객체(Object)`의 정렬기준을 만들어 주기 위해서입니다.

만약 `String`타입의 자료들을 정렬하기 위해 배열을 만들고, 정렬한다면 간단하게 `Arrays.sort()`메서드를 사용하여 사전순서대로 정렬할 수 있습니다.

하지만 객체는 정렬기준이 없기때문에 Comparable 인터페이스를 객체의 클래스에서 구현하여 정렬기준을 만들어주는 방식으로 사용됩니다.

Comparable 인터페이스의 구현체는 반드시 `compareTo(<T> e)`를 구현해야 합니다.

`Comparable 인터페이스 구현 예시`

```java

public class CompareTest {
	public static void main(String[] args) {

		List<Emp> list = new ArrayList<>();

		list.add(new Emp("A001", "김수한", 10000));
		list.add(new Emp("B001", "무거북", 20000));
		list.add(new Emp("C001", "이와두", 12000));
		list.add(new Emp("D001", "루미삼", 50000));
		list.add(new Emp("E001", "천갑자", 30000));

		System.out.println("연봉기준 오름차순 정렬");
		Collections.sort(list);

		for(Emp e : list) {
			System.out.println(e);
		}
	}
}

class Emp implements Comparable<Emp> {

	private String emp_no;
	private String emp_name;
	private int salary;

	public Emp(String emp_no, String emp_name, int salary) {
		super();
		this.emp_no = emp_no;
		this.emp_name = emp_name;
		this.salary = salary;
	}

	public String getEmp_no() {
		return emp_no;
	}

	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "emp [emp_no=" + emp_no + ", emp_name=" + emp_name + ", salary=" + salary + "]";
	}

	@Override
	public int compareTo(Emp emp) {
		// 연봉기준 오름차순 정렬이 되도록 구현
		return salary - emp.getSalary();
	}
}
```

`출력결과`

```java
연봉기준 오름차순 정렬
emp [emp_no=A001, emp_name=김수한, salary=10000]
emp [emp_no=C001, emp_name=이와두, salary=12000]
emp [emp_no=B001, emp_name=무거북, salary=20000]
emp [emp_no=E001, emp_name=천갑자, salary=30000]
emp [emp_no=D001, emp_name=루미삼, salary=50000]
```

위의 예시에서 `Emp`클래스는 `Comparable`의 구현체입니다.

따라서 `compareTo(Emp emp)`메서드를 오버라이드 하였고, 오름차순정렬이 되도록 구현했습니다.

또한 `Emp`객체를 관리하는 `List`는 배열이 `CollectionFrame`이기때문에 `Arrays.sort`가 아닌 `Collections.sort()`를 사용하였습니다.

compareTo() 메서드는 자기자신의 salary와 매개변수로 넘어온 객체의 salary를 비교합니다.

- compareTo(Emp emp) 의 반환값
  - salary > emp.getSalary()
    - `return 1`
  - salary == emp.getSalary()
    - `return 0`
  - salary < emp.getSalary()
    - `return -1`

이렇게 정렬의 대상이 되는 객체의 클래스에 `Comparable` 인터페이스를 구현하면, 객체들의 정렬기준을 정할 수 있습니다.

# Comparator

---

`Comparator`는 `Java.util` package에 있는 인터페이스입니다.

`Comparable`과 마찬가지로 `Comparator`는 객체의 정렬에 사용됩니다

하지만 `Comparator`의 구현체는 클래스 자체가 정렬기준으로 사용됩니다.

한마디로 `외부 정렬기준`을 정의한다는 것입니다

한 가지 예를들어보면 지금 우리가 선언한 Emp클래스는 이미 Comparable 인터페이스를 구현했기때문에 정렬기준을 가지고 있습니다.

만약 사용자가 Emp클래스에 정해진 정렬기준이 아닌 다른 정렬기준으로 데이터를 정렬하고 싶다면 어떻게 해야할까요?

Emp클래스 내부에 구현되어 있는 compareTO() 메서드를 수정하는것은 임시방편일 뿐입니다.

이러한 경우 사용할 수 있는것이 Comparator를 구현한 외부 정렬자를 사용하는 것입니다

`Comparator 인터페이스 구현 예시`

```java

public class CompareTest {
	public static void main(String[] args) {

		List<Emp> list = new ArrayList<>();

		list.add(new Emp("A001", "김수한", 10000));
		list.add(new Emp("B001", "무거북", 20000));
		list.add(new Emp("C001", "이와두", 12000));
		list.add(new Emp("D001", "루미삼", 50000));
		list.add(new Emp("E001", "천갑자", 30000));

		System.out.println("연봉기준 내림차순 정렬");
		Collections.sort(list, new SalaryDesc());

		for(Emp e : list) {
			System.out.println(e);
		}
		System.out.println();

		System.out.println("이름기준 내림차순 정렬");
		Collections.sort(list, new NameDesc());

		for(Emp e : list) {
			System.out.println(e);
		}
	}
}

class SalaryDesc implements Comparator<Emp> {

	@Override
	public int compare(Emp emp1, Emp emp2) {
		// 연봉기준 내림차순 정렬이 되도록 구현
		return emp1.compareTo(emp2) * -1;
	}
}

class NameDesc implements Comparator<Emp> {

	@Override
	public int compare(Emp emp1, Emp emp2) {
		//이름기준 오름차순 정렬이 되도록 구현
		return emp1.getEmp_name().compareTo(emp2.getEmp_name()) * -1;
	}
}
```

`출력결과`

```java
연봉기준 내림차순 정렬
emp [emp_no=D001, emp_name=루미삼, salary=50000]
emp [emp_no=E001, emp_name=천갑자, salary=30000]
emp [emp_no=B001, emp_name=무거북, salary=20000]
emp [emp_no=C001, emp_name=이와두, salary=12000]
emp [emp_no=A001, emp_name=김수한, salary=10000]

이름기준 내림차순 정렬
emp [emp_no=E001, emp_name=천갑자, salary=30000]
emp [emp_no=C001, emp_name=이와두, salary=12000]
emp [emp_no=B001, emp_name=무거북, salary=20000]
emp [emp_no=D001, emp_name=루미삼, salary=50000]
emp [emp_no=A001, emp_name=김수한, salary=10000]
```

`Comparable` 인터페이스를 구현할 때와 조금 다른것을 볼 수 있습니다

우선 `Collections.sort()`메서드의 매개변수가 2개가 되었습니다.

외부 정렬자(Comparator의 구현체)를 사용할 때는 `Collections.sort()`의 매개변수로 정렬기준을 넘겨주어야 합니다

그리고 `Comparator`의 구현체는 `compare(Emp emp1, Emp emp2)`메서드를 구현하는데 매개변수가 2개입니다

현재 자기자신의 값과 비교하는 compareTo메서드와 다르게 매개변수로 받은 2개의 객체가 가진 값을 비교하기 때문입니다.

마지막으로 연봉기준 내림차순 정렬을 구현한 `SalaryDesc`클래스를 보면, Emp클래스에 이미 구현되어 있는 compareTo메서드를 재사용하였습니다.

이미 오름차순으로 정렬기준이 잡혀있는 comepareTo메서드의 결과에 -1을 곱하면 내림차순으로 쉽게 구현할 수 있습니다.

이처럼 이미 정해진 정렬기준 외 다른 정렬기준을 사용하고 싶다면 `Comparator`의 구현체를 사용하여 여러가지 정렬기준을 사용할 수 있습니다.

# Summary

---

- Comparable
  - java.lang package
  - 객체의 정렬기준을 정해줄 때 사용한다.
  - Comparable 인터페이스의 구현체는 compareTo메서드를 구현해야 한다.
- Comparator
  - java.util package
  - 이미 정해진 정렬기준 외 다른 정렬기준을 사용하고 싶을때 사용한다.
  - Comparator 인터페이스의 구현체는 compare메서드를 구현해야 한다.
  - Comparator 인터페이스의 구현체는 그 자체가 정렬자로 사용된다. (정렬기준)
