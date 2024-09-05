---
aliases:
  - Algorithm
tags:
  - Resource
  - CS
  - Algorithm
---

### **LIS란?**

LIS는 **L**ongest **I**ncreasing **S**ubsequence의 약자이다. 말 그대로 **최장 증가 부분 수열**, 또는 **가장 긴 증가하는 부분 수열**이라고 불린다. LIS는 주어진 수열에서 **오름차순으로 정렬된 가장 긴 부분** 수열을 찾는 알고리즘이다. 다만, 오름차순으로 정렬된 부분 수열이 **연속적**이거나, **유일할** 필요는 없다.

풀이 방법으로는 DP를 활용한 방법과 이진 탐색을 활용한 방법이 있다. DP를 활용하면 조금 더 단순하지만 시간 복잡도가 **O(n^2)**이고 이진 탐색을 활용하면 조금 더 복잡하지만 시간 복잡도는 **O(n logn)**이다.

이번 글에서는 DP를 활용한 LIS만 살펴보도록 하겠다.

### **LIS 접근 방법 & 예시**

예시를 한번 보겠다. 수열 **arr = {10, 20, 10, 30, 20, 50}이라고** 가정해 보겠다. 

**dp [i] = i번째 수를 마지막 원소로 가지는 LIS의 길이**라고 하겠다.

LIS 길이를 구하는 방법은 i번째 원소를 i-1번째 원소와 비교했을 때 0~i-1번째 원소들 중에서 i번째 원소보다 작은 원소들의 dp값들 중 가장 큰 값 + 1을 dp [i]로 기록하는 것이다.

이제 예를 한번 보겠다.

![](https://blog.kakaocdn.net/dn/KDrtK/btrx6Z2cgrU/IrZMspNB27eIV7YnzGkTG0/img.png)

처음 dp 값은 1로 시작한다.

![](https://blog.kakaocdn.net/dn/KyeoY/btrx6IsRfWu/3aPKtVfk5L6cFRf43snKf0/img.png)

현재 값은 20이고 20보다 작거나 같은 이전 원소들 중 가장 큰 dp값이 1이므로 (Arr이 10일 때) 1+1 = 2를 현재 dp값으로 저장한다.

![](https://blog.kakaocdn.net/dn/oUqeJ/btrx8MVmBuR/6Ju9GEMMStBb06iLi2jni0/img.png)

현재 값이 10이고 10보다 작거나 같은 이전 원소들 중 가장 큰 dp값이 1이므로 (Arr이 10일 때) 1 + 1 = 2를 현재 dp값으로 저장한다.

![](https://blog.kakaocdn.net/dn/yxL4C/btrx30usZ0m/DIFaRaAvkqd0ogyYe1bMH0/img.png)

현재 값이 30이고 30보다 작거나 같은 이전 원소들 중 가장 큰 dp 값이 2이므로 (Arr이 20일 때, 혹은 10일 때) 2 + 1 = 3을 현재 dp값으로 저장한다.

![](https://blog.kakaocdn.net/dn/bqcdV1/btrx5zpDWVr/WKlKzvbcyl3ktnaIlfiK10/img.png)

현재 값이 20이고 20보다 작거나 같은 이전 원소들 중 가장 큰 dp 값이 2이므로 2 + 1 = 3을 현재 dp값으로 저장한다.

![](https://blog.kakaocdn.net/dn/O6DW4/btrx5AvlwPY/rKAuceuJm1Xhpcy8WpyMJ1/img.png)

마지막 값이 50이고 50보다 작거나 같은 이전 원소들 중 가장 큰 dp 값이 3이므로 3 + 1 = 4를 현재 dp값으로 저장한다. 따라서 수열 Arr의 LIS 길이는 4이다.


### 코드
```java
import java.io.*;
import java.util.*;
public class Main{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine()); //수열 arr의 길이
        int[] arr = new int[n+1];  //수열 배열 초기화
        int[] dp = new int[n+1];  //dp 테이블 초기화
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());  //arr에 수열 입력 받기
            dp[i] = 1;  //모든 dp값들은 최소 1
        }
        int max = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                if (arr[i] > arr[j]) dp[i] = Math.max(dp[i], dp[j]+1);
                // 이전 원소들 중 가장 큰 dp값 + 1
            }
            max = Math.max(max, dp[i]);  //LIS 길이 구하기
        }
        System.out.print(max);
    }
}
```