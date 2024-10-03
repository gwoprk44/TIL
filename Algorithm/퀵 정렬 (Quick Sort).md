퀵 정렬의 메커니즘은 크게 다음과 같다.

하나의 리스트를 **피벗(pivot)**을 기준으로 **두 개의 부분리스트**로 나누어 하나는 **피벗보다 작은 값들의 부분리스트,** 다른 하나는 **피벗보다 큰 값들의 부분리스트**로 정렬한 다음**, 각 부분리스트에 대해 다시 위 처럼 재귀적으로 수행**하여 정렬하는 방법이다.

이 부분에서 만약 알고리즘에 대해 잘 알고있다면 '**분할 정복(Divide and Conquer)'**을 떠올릴 것이다.

맞다. 퀵 정렬은 기본적으로 '분할 정복' 알고리즘을 기반으로 정렬되는 방식이다. 다만, 병합정렬(Merge Sort)과 다른 점은 병합정렬의 경우 하나의 리스트를 '절반'으로 나누어 분할 정복을 하고, 퀵 정렬(Quick Sort)의 경우 피벗(pivot)의 값에 따라 피벗보다 작은 값을 갖는 부분리스트와 피벗보다 큰 값을 갖는 부분리스트의 크기가 다를 수 있기 때문에 하나의 리스트에 대해 비균등하게 나뉜다는 점이 있다.

실제로도 정렬 방법에서 병합 정렬과 퀵 정렬은 많이 비교되곤 한다. (다만 일반적으로 병합정렬보다 퀵정렬이 빠르다.)

위 말만 들으면 잘 감이 안오실 것 같다.

구체적으로 알아보기에 앞서 미리 언급하자면 퀵 정렬은 데이터를 '비교'하면서 찾기 때문에 **'비교 정렬'**이며 정렬의 대상이 되는 데이터 외에 추가적인 공간을 필요로 하지 않는다. 그러므로 **'제자리 정렬(in-place sort)이다.'**

퀵 정렬은 병합정렬과는 다르게 하나의 피벗을 두고 두 개의 부분리스트를 만들 때 서로 떨어진 원소끼리 교환이 일어나기 때문에 **불안정정렬(Unstable Sort)** 알고리즘이기도 하다.

위 말만 듣는다면 이해하기는 어려울 테니 정렬 방법에 대해 구체적으로 알아보도록 하자.

---

## 정렬 방법

퀵 정렬의 전체적인 과정은 이렇다.

**1. 피벗을 하나 선택한다.**

**2. 피벗을 기준으로 양쪽에서 피벗보다 큰 값, 혹은 작은 값을 찾는다. 왼쪽에서부터는 피벗보다 큰 값을 찾고, 오른쪽에서부터는 피벗보다 작은 값을 찾는다.**

**3. 양 방향에서 찾은 두 원소를 교환한다.**

**4. 왼쪽에서 탐색하는 위치와 오른쪽에서 탐색하는 위치가 엇갈리지 않을 때 까지 2번으로 돌아가 위 과정을 반복한다.**

**5. 엇갈린 기점을 기준으로 두 개의 부분리스트로 나누어 1번으로 돌아가 **해당 부분리스트의 길이가 1이 아닐 때 까지 1번 과정을** 반복한다. **(Divide : 분할)\*\*\*\*

**6. 인접한 부분리스트끼리 합친다. (Conqure : 정복)**

위 6가지 과정에 의해 정렬되는 방식이다.

2~3번 과정의 경우 좀 더 구체적으로 말하자면 대부분의 구현은 현재 리스트의 양 끝에서 시작하여 왼쪽에서는 피벗보다 큰 값을 찾고, 오른쪽에서는 피벗보다 작은 값을 찾아 두 원소를 교환하는 방식이다. 그래야 피벗보다 작은 값은 왼쪽 부분에, 피벗보다 큰 값들은 오른쪽 부분에 치우치게 만들 수 있기 때문이다. 이를 흔히 호어(Hoare)방식이라고 한다.

피벗을 선택하는 과정은 여러 방법이 있는데, 대표적으로 세 가지가 있다.

현재 부분배열의 가장 왼쪽 원소가 피벗이 되는 방법, 중간 원소가 피벗이 되는 방법, 마지막 원소가 피벗이 되는 방법이다.

'피벗'을 하나 설정하고 **피벗보다 작은 값들은 왼쪽에, 큰 값들은 오른쪽에 치중하도록 하는  것**이다. 이 과정을 흔히 **파티셔닝(Partitioning)**이라고 한다.

그렇게 파티셔닝을 했다면 파티셔닝을 통해 배치 된 피벗의 위치를 기준으로 좌우 부분리스트로 나누어 각각의 리스트에 대해 재귀호출을 해주면 된다.

위 원리를 이해해야 앞으로 구현 할 dual-pivot quick sort(이중 피벗 퀵 정렬) 또한 이해하기가 쉬울 것이다.

그리고 혹시 몰라 움직이는 이미지로 이해할 수 있도록 이미지를 첨부했다.

전체적인 흐름을 보자면 다음과 같은 형태로 정렬 한다.

![](https://blog.kakaocdn.net/dn/bb5JKi/btq5aAjJZnz/lkkjHB9nXbzXkFG0cDUEK0/img.gif)

https://upload.wikimedia.org/wikipedia/commons/6/6a/Sorting_quicksort_anim.gif

![](https://blog.kakaocdn.net/dn/bQLJ6H/btq5fDzsCwm/z0vrS38HhAKKyjaU56ipM1/img.gif)

https://upload.wikimedia.org/wikipedia/en/f/f7/Quick_sort_animation.gif

## 구현

왼쪽 피벗 선택 방식으로 퀵 정렬을 구현한 것.

```java
	public class QuickSort {

	public static void sort(int[] a) {
		l_pivot_sort(a, 0, a.length - 1);
	}

	/**
	 *  왼쪽 피벗 선택 방식
	 * @param a		정렬할 배열
	 * @param lo	현재 부분배열의 왼쪽
	 * @param hi	현재 부분배열의 오른쪽
	 */
	private static void l_pivot_sort(int[] a, int lo, int hi) {

		/*
		 *  lo가 hi보다 크거나 같다면 정렬 할 원소가
		 *  1개 이하이므로 정렬하지 않고 return한다.
		 */
		if(lo >= hi) {
			return;
		}

		/*
		 * 피벗을 기준으로 요소들이 왼쪽과 오른쪽으로 약하게 정렬 된 상태로
		 * 만들어 준 뒤, 최종적으로 pivot의 위치를 얻는다.
		 *
		 * 그리고나서 해당 피벗을 기준으로 왼쪽 부분리스트와 오른쪽 부분리스트로 나누어
		 * 분할 정복을 해준다.
		 *
		 * [과정]
		 *
		 * Partitioning:
		 *
		 *   a[left]          left part              right part
		 * +---------------------------------------------------------+
		 * |  pivot  |    element <= pivot    |    element > pivot   |
		 * +---------------------------------------------------------+
		 *
		 *
		 *  result After Partitioning:
		 *
		 *         left part          a[lo]          right part
		 * +---------------------------------------------------------+
		 * |   element <= pivot    |  pivot  |    element > pivot    |
		 * +---------------------------------------------------------+
		 *
		 *
		 *  result : pivot = lo
		 *
		 *
		 *  Recursion:
		 *
		 * l_pivot_sort(a, lo, pivot - 1)     l_pivot_sort(a, pivot + 1, hi)
		 *
		 *         left part                           right part
		 * +-----------------------+             +-----------------------+
		 * |   element <= pivot    |    pivot    |    element > pivot    |
		 * +-----------------------+             +-----------------------+
		 * lo                pivot - 1        pivot + 1                 hi
		 *
		 */
		int pivot = partition(a, lo, hi);

		l_pivot_sort(a, lo, pivot - 1);
		l_pivot_sort(a, pivot + 1, hi);
	}



	/**
	 * pivot을 기준으로 파티션을 나누기 위한 약한 정렬 메소드
	 *
	 * @param a		정렬 할 배열
	 * @param left	현재 배열의 가장 왼쪽 부분
	 * @param right	현재 배열의 가장 오른쪽 부분
	 * @return		최종적으로 위치한 피벗의 위치(lo)를 반환
	 */
	private static int partition(int[] a, int left, int right) {

		int lo = left;
		int hi = right;
		int pivot = a[left];		// 부분리스트의 왼쪽 요소를 피벗으로 설정

		// lo가 hi보다 작을 때 까지만 반복한다.
		while(lo < hi) {

			/*
			 * hi가 lo보다 크면서, hi의 요소가 pivot보다 작거나 같은 원소를
			 * 찾을 떄 까지 hi를 감소시킨다.
			 */
			while(a[hi] > pivot && lo < hi) {
				hi--;
			}

			/*
			 * hi가 lo보다 크면서, lo의 요소가 pivot보다 큰 원소를
			 * 찾을 떄 까지 lo를 증가시킨다.
			 */
			while(a[lo] <= pivot && lo < hi) {
				lo++;
			}

			// 교환 될 두 요소를 찾았으면 두 요소를 바꾼다.
			swap(a, lo, hi);
		}


		/*
		 *  마지막으로 맨 처음 pivot으로 설정했던 위치(a[left])의 원소와
		 *  lo가 가리키는 원소를 바꾼다.
		 */
		swap(a, left, lo);

		// 두 요소가 교환되었다면 피벗이었던 요소는 lo에 위치하므로 lo를 반환한다.
		return lo;
	}

	private static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
}
```
