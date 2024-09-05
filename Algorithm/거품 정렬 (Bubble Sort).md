---
aliases:
  - 정렬
tags:
  - Resource
  - CS
  - Algorithm
---
  
거품 정렬은 아마 정렬 방식 중 가장 쉽게 생각할 수 있는 알고리즘 중 하나일 것이다.

두 개의 인접한 원소를 비교하여 정렬하는 방식이다.

왜 Bubble 이라는 이름이 붙었는지 찾아보니 정렬 과정에서 원소의 이동이 마치 거품이 수면위로 올라오는 것 같다고 해서 거품(Bubble) 이라는 이름이 붙었다고 한다...

정렬 방식 중 가장 쉬우니 일단 거품 정렬에 대한 특징만 짚고 넘어가보자.

거품 정렬은 데이터를 '비교'하면서 찾기 때문에 **'비교 정렬'**이며 정렬의 대상이 되는 데이터 외에 추가적인 공간을 필요로 하지 않기 때문에 **'제자리 정렬(in-place sort)'**이기도 하다. 정확히는 데이터를 서로 교환하는 과정(swap)에서 임시 변수를 필요로 하나, 이는 충분히 무시할 만큼 적은 양이기 때문에 제자리 정렬로 보는 것이다. 이는 선택정렬과도 같은 부분이다.

그리고 이전에 다뤘던 선택 정렬과는 달리 거품 정렬은 앞에서부터 차례대로 비교하기 때문에 **'안정 정렬'**이기도 하다.

---

  
  
  

## 정렬 방법

  
  

거품 정렬의 전체적인 과정은 이렇다. (오름차순을 기준으로 설명)

**1. 앞에서부터 현재 원소와 바로 다음의 원소를 비교한다.**

**2. 현재 원소가 다음 원소보다 크면 원소를 교환한다.**

**3. 다음 원소로 이동하여 해당 원소와 그 다음원소를 비교한다.**

즉, 그림으로 보면 다음과 같은 과정을 거친다.

![](https://blog.kakaocdn.net/dn/dfQeBw/btqT1848T64/H5D9U4z8dhELfRTkfk2k30/img.png)

이 때, 각 라운드를 진행 할 때마다 뒤에서부터 한 개씩 정렬되기 때문에, 라운드가 진행 될 때마다 한 번씩 줄면서 비교하게 된다.

한마디로 정리하자면 이렇다.

총 라운드는 **배열 크기 - 1 번 진행**되고,

각 라운드별 비교 횟수는 **배열 크기 - 라운드 횟수 만큼 비교**한다.

전체적인 흐름을 보자면 다음과 같은 형태로 정렬 한다.

![](https://blog.kakaocdn.net/dn/camFmC/btqT18jLl9k/eewEO8cGnwQ0mopwu18r91/img.gif)

https://en.wikipedia.org/wiki/Bubble_sort

![](https://blog.kakaocdn.net/dn/co7iVc/btqT0tIGHz5/CuHPEWlkJrc8GpnOM1fkp0/img.gif)

https://ko.wikipedia.org/wiki/거품_정렬

## 구현

```java
public class Bubble_Sort {
 
	public static void bubble_sort(int[] a) {
		bubble_sort(a, a.length);
	}
	
	private static void bubble_sort(int[] a, int size) {
		
		// round는 배열 크기 - 1 만큼 진행됨 
		for(int i = 1; i < size; i++) {
			
			// 각 라운드별 비교횟수는 배열 크기의 현재 라운드를 뺀 만큼 비교함
			for(int j = 0; j < size - i; j++) {
				
				/*
				 *  현재 원소가 다음 원소보다 클 경우
				 *  서로 원소의 위치를 교환한다. 
				 */
				if(a[j] > a [j + 1]) {
					swap(a, j, j + 1);
				}
			}
		}
	}
	
	private static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
}
```