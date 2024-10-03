카운팅 정렬은 수많은 정렬 알고리즘 중 시간복잡도가 *𝚶(𝑛)* 으로 엄청난 성능을 보여주는 알고리즘이다. 보통 빠르다는 정렬 알고리즘으로는 대표적으로 퀵 정렬(Quick Sort), 힙 정렬(Heap Sort), 합병 정렬(Merge Sort) 등이 있는데, 이들의 평균 시간복잡도는 *𝚶(nlogn)* 인 것에 비하면 엄청난 속도인 것은 당연지사다.

기본적으로 정렬이라하면 데이터끼리 직접 비교하는 경우가 많다. 그렇기 때문에 데이터 직접 비교를 사용하는 정렬 알고리즘의 경우  ***𝚶*****_(nlogn_*****)*** 보다 작아질 수 없는 것이 한계다. 그렇다면 카운팅 정렬은 어떻게 이를 극복한 것일까?

그럼에도 왜 퀵 정렬같은 *𝚶(nlogn)*  의 정렬 알고리즘을 많이 사용하는 것일까?

이에 대해 같이 알아보자.

## 정렬 방법

카운팅 정렬의 기본 메커니즘은 아주 단순하다. 데이터의 값이 몇 번 나왔는지를 세주는 것이다. 말 그대로 counting 하는 것이다.

그림으로 차근차근 이해해보자.

먼저 아래와 같은 배열이 있다고 가정해보자.

![](https://blog.kakaocdn.net/dn/tKb5x/btqEvhaLlLz/qSnkDsN5JB4KY8rQPhqRek/img.png)

### 과정 1

array 를 한 번 순회하면서 각 값이 나올 때마다 해당 값을 index 로 하는 새로운 배열(counting)의 값을 1 증가시킨다.

과정으로 보면

array[0] = 7 이므로 counting[7] 값을 1 증가,

array[1] = 2 이므로 counting[2] 값을 1 증가,

...

array[11] = 1 이므로 count[1] 값을 1 증가.

이렇게 과정을 거친다. 이 과정을 마치면 다음 그림처럼 된다.

![](https://blog.kakaocdn.net/dn/M2d0N/btqEuzQKkA3/kc1r9KKWnW1x7RcelhfFK0/img.png)

counting 배열의 의미는 다음과 같다.

0의 개수는 1개

1의 개수는 2개

2의 개수는 1개

...

이런식으로 counting 배열은 각 값의 개수가 담겨있는 배열이 된다.

### 과정 2

counting 배열의 각 값을 누적합으로 변환시킨다.

말로는 이해가 어려울 수 있으니, 그림으로 보여주겠다.

![](https://blog.kakaocdn.net/dn/muC6D/btqEtuiBgMx/GjorpguRqRsgvsklwgLZx0/img.png)

이런식으로 counting 배열 값들을 누적합으로 설정해준다.

즉, 우리는 다음과 같은 두 배열을 갖고 있게 되는 것이다.

![](https://blog.kakaocdn.net/dn/NaJnl/btqEtuiBnYn/L8LK2XYYKu8PhYr0nmZpK0/img.png)

여기서 우리가 알 수 있는 것은 정렬을 할 경우 **counting 배열의 각 값은 (시작점 - 1)을 알려준다는 것**이다.

무슨 말인지는 마지막 과정 3을 보면서 이해해보자.

### 과정 3

마지막 단계다.

앞서 잠깐 언급했듯이, counting 배열의 각 값은 (시작점 - 1)을 알려준다고 했다. 즉, 해당 값에 대응되는 위치에 배정하면 된다는 의미다. 간단하게 예를 들면 이렇게 되는 것이다.

array[0] = 7 이고, counting[7] = 12 이다. 여기서 countin[7] 의 값에 1 을 빼준 뒤 해당 값이 새로운 배열의 인덱스 11에 위치하게 된다.

array[1] = 2 이고, counting[2] = 4 이다. 여기서 countin[2] 의 값에 1 을 빼준 뒤 해당 값이 새로운 배열의 인덱스 3에 위치하게 된다.

이런식으로 쭉 하면 된다.

**다만 안정적으로 정렬하기 위해서는 array의 마지막 index 부터 순회하는 것이 좋다.**

그림으로 보자면 다음과 같다.

![](https://blog.kakaocdn.net/dn/KBSml/btqEvhosPPX/L70xRWd3gdGz9qXSBmiAN1/img.png)

이런식으로 하면 result 배열은 array 배열의 정렬된 형태로 있게 된다.

이 과정 자체가 두 수를 비교하는 과정이 없기 때문에 빠른 배치가 가능하다. 다만 보다시피 몇가지 단점 또한 존재한다.

바로 counting 배열이라는 새로운 배열을 선언해주어야 한다는 점이다. 생각보다 이 단점이 꽤 클 수 있는데, array 안에 있는 max값의 범위에 따라 counting 배열의 길이가 달라지게 된다. 예로들어 **10개의 원소를 정렬하고자 하는데, 수의 범위가 0~1억이라면 메모리가 매우 낭비가 된다**.

즉, Counting Sort가 효율적인 상황에서 쓰려면 수열의 길이보다 수의 범위가 극단적으로 크면 메모리가 엄청 낭비 될 수 있다는 것. 그렇기 때문에 각기 상황에 맞게 정렬 알고리즘을 써야하는데, 퀵 정렬이나 병합정렬 등이 선호되는 이유도 이에 있다.

(Quick 정렬의 경우 시간복잡도 평균값이 ***𝚶*****_(nlogn_*****)*** 으로 빠른편이면서 배열도 하나만 사용하기 때문에 공간복잡도는 *𝚶(𝑛)* 으로 시간과 메모리 둘 다 효율적이라는 점이다.)

## 구현

```java
public class CountingSort {
	public static void main(String[] args) {

		int[] array = new int[100];		// 수열의 원소 : 100개
		int[] counting = new int[31];	// 수의 범위 : 0 ~ 30
		int[] result = new int[100];	// 정렬 될 배열

		for(int i = 0; i < array.length; i++) {
			array[i] = (int)(Math.random()*31);	// 0 ~ 30
		}


		// Counting Sort
		// 과정 1
		for(int i = 0; i < array.length; i++) {
			// array 의 value 값을 index 로 하는 counting 배열 값 1 증가
			counting[array[i]]++;
		}

		// 과정 2
		for(int i = 1; i < counting.length; i++) {
			// 누적 합 해주기
			counting[i] += counting[i - 1];
		}

		// 과정 3
		for(int i = array.length - 1; i >= 0; i--) {
			/*
			 *  i 번쨰 원소를 인덱스로 하는 counting 배열을 1 감소시킨 뒤
			 *  counting 배열의 값을 인덱스로 하여 result에 value 값을 저장한다.
			 */
			int value = array[i];
			counting[value]--;
			result[counting[value]] = value;
		}



		/* 비교 출력 */

		// 초기 배열
		System.out.println("array[]");
		for(int i = 0; i < array.length; i++) {
			if(i % 10 == 0) System.out.println();
			System.out.print(array[i] + "\t");
		}
		System.out.println("\n\n");


		// Counting 배열
		System.out.println("counting[]");
		for(int i = 0; i < counting.length; i++) {
			if(i % 10 == 0) System.out.println();
			System.out.print(counting[i] + "\t");
		}
		System.out.println("\n\n");


		// 정렬된 배열
		System.out.println("result[]");
		for(int i = 0; i < result.length; i++) {
			if(i % 10 == 0) System.out.println();
			System.out.print(result[i] + "\t");
		}
		System.out.println();
	}

}
```
