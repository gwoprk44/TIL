---
aliases:
  - 탐색
tags:
  - Resource
  - CS
  - Algorithm
---
# 깊이 우선 탐색과 너비 우선 탐색 (DFS & BFS)

## 탐색

많은 양의 데이터 중에서 원하는 데이터를 찾는 과정을 의미한다. 프로그래밍에서는 그래프, 트리 등의 자료구조 안에서 탐색을 하는 문제를 자주 다룬다. 대표적인 탐색 알고리즘으로 DFS와 BFS가 있다. 두 알고리즘을 이해하기 위해서는 자료구조인 스택과 큐에 대한 이해가 전제되어야 한다.

![](https://blog.kakaocdn.net/dn/cyaNIZ/btsk28iYxaH/QeN8Na7i9kB1KwMcaImhp0/img.png)

- 깊이 우선 탐색(DFS, Depth First Search):
    - 동작 원리: 스택
    - 구현 방법: 재귀 함수 사용
    - 탐색 순서: 1 → 2 → 7 → 6 → 8 → 3 → 4 → 5 (1부터 시작하고 작은 순서부터 탐색한다고 가정)
    - 수행 과정
        1. 탐색 시작 노드를 스택에 삽입하고 방문 처리를 한다.
        2. 스택의 최상단 노드에 방문하지 않은 인접한 노드가 하나라도 있으면 그 노드를 스택에 넣고 방문 처리하고 방문하지 않은 인접 노드가 없으면 스택에서 최상단 노드를 꺼낸다.
        3. 2번의 과정을 더 이상 수행할 수 없을 때까지 반복한다.
- 너비 우선 탐색 (BFS, Breadth First Search): 가까운 노드부터 탐색하는 알고리즘 
    - 동작 원리: 큐
    - 구현 방법: 큐 자료 구조 사용
    - 탐색 순서: 1 → 2 → 3 → 8 → 7 → 4 → 5 → 6 (1부터 시작하고 작은 순서부터 탐색한다고 가정)
    - 수행 과정
        1. 탐색 시작 노드를 큐에 삽입하고 방문 처리를 한다.
        2. 큐에서 노드를 꺼낸 뒤에 해당 노드의 인접 노드 중에서 방문하지 않은 노드를 모두 큐에 삽입하고 방문 처리한다
        3. 2번의 과정을 더 이상 수행할 수 없을 때까지 반복한다

## 자료 구조

데이터를 표현하고 관리하고 처리하기 위한 구조를 의미하고 그 중 스택과 큐는 자료구조의 기초 개념으로 삽입과 삭제 두 가지 핵심적인 함수로 구성된다. 스택은 선입후출 구조를 큐는 선입선출 구조를 가지고 있다. 

# 깊이 우선 탐색(DFS)

자바로 작성한 깊이 우선 탐색의 소스 코드는 아래와 같다.

```java
import java.util.*;

public class Main
{
    public final static int GRAPH_LIST_SIZE = 9;
    public static boolean[] visitedFlag = new boolean[GRAPH_LIST_SIZE];
    public static ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
    
    public static void main(String[] args) {
        // 리스트 초기화
        for (int i = 0; i < GRAPH_LIST_SIZE; i++) {
            graph.add(new ArrayList<Integer>());
        }
        
        // 노드 1에 연결된 노드 정보 저장 
        graph.get(1).add(2);
        graph.get(1).add(3);
        graph.get(1).add(8);
        
        // 노드 2에 연결된 노드 정보 저장 
        graph.get(2).add(1);
        graph.get(2).add(7);
        
        // 노드 3에 연결된 노드 정보 저장 
        graph.get(3).add(1);
        graph.get(3).add(4);
        graph.get(3).add(5);
        
        // 노드 4에 연결된 노드 정보 저장 
        graph.get(4).add(3);
        graph.get(4).add(5);
        
        // 노드 5에 연결된 노드 정보 저장 
        graph.get(5).add(3);
        graph.get(5).add(4);
        
        // 노드 6에 연결된 노드 정보 저장 
        graph.get(6).add(7);
        
        // 노드 7에 연결된 노드 정보 저장 
        graph.get(7).add(2);
        graph.get(7).add(6);
        graph.get(7).add(8);
        
        // 노드 8에 연결된 노드 정보 저장 
        graph.get(8).add(1);
        graph.get(8).add(7);

        dfs(1);
    }
	
    // DFS 탐색을 위한 재귀함수
    public static void dfs(int point){
        // 현재 노드 방문 처리 
        visitedFlag[point] = true;
        System.out.print(point + " ");
	    
        // 인접 노드 방문
        for(int node : graph.get(point)){
            if(!visitedFlag[node]){
                 dfs(node);
            }
        }
    }
}
```

# 너비 우선 탐색(BFS)

자바로 작성한 너비 우선 탐색의 소스 코드는 아래와 같다.

```java
import java.util.*;

public class Main
{
    public final static int GRAPH_LIST_SIZE = 9;
    public static boolean[] visitedFlag = new boolean[GRAPH_LIST_SIZE];
    public static ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
    
	public static void main(String[] args) {
		// 리스트 초기화
        for (int i = 0; i < GRAPH_LIST_SIZE; i++) {
            graph.add(new ArrayList<Integer>());
        }
        
        // 노드 1에 연결된 노드 정보 저장 
        graph.get(1).add(2);
        graph.get(1).add(3);
        graph.get(1).add(8);
        
        // 노드 2에 연결된 노드 정보 저장 
        graph.get(2).add(1);
        graph.get(2).add(7);
        
        // 노드 3에 연결된 노드 정보 저장 
        graph.get(3).add(1);
        graph.get(3).add(4);
        graph.get(3).add(5);
        
        // 노드 4에 연결된 노드 정보 저장 
        graph.get(4).add(3);
        graph.get(4).add(5);
        
        // 노드 5에 연결된 노드 정보 저장 
        graph.get(5).add(3);
        graph.get(5).add(4);
        
        // 노드 6에 연결된 노드 정보 저장 
        graph.get(6).add(7);
        
        // 노드 7에 연결된 노드 정보 저장 
        graph.get(7).add(2);
        graph.get(7).add(6);
        graph.get(7).add(8);
        
        // 노드 8에 연결된 노드 정보 저장 
        graph.get(8).add(1);
        graph.get(8).add(7);

        bfs(1);
	}
	
	// BFS 탐색을 위한 재귀함수
	public static void bfs(int point){
	    Queue<Integer> queue = new LinkedList<Integer>();
	    
	    // 현재 노드 방문 처리 
	    queue.offer(point);
	    visitedFlag[point] = true;
	    
	    while(!queue.isEmpty()){
	        int target = queue.poll();
	        System.out.print(target + " ");
	        
	        // 인접 노드 방문
	        for(int node : graph.get(target)){
	            if(!visitedFlag[node]){
	                queue.offer(node);
	                visitedFlag[node] = true;
	            }
	        }
	    }
	    
	}
}
```

# DFS, BFS를 활용하면 좋은 경우

DFS와 BFS를 활용하면 좋은 상황으로는 아래와 같은 상황들이 있다.

(1) 그래프의 모든 정점을 방문하는 것이 주요한 문제: DFS, BFS 모두 무방하다.

(2) 경로의 특징을 저장해둬야 하는 문제: 각 장점에 숫자가 있고 a 부터 b까지 가는 경로를 구하는데 경로에 같은 숫자가 있으면 안된다는 문제 등, 각각의 경로마다 특징을 저장해둬야 하는 경우는 DFS를 사용해야 한다. BFS는 경로의 특징을 저장하지 못한다.

(3) 최단거리를 구하는 문제: BFS가 유리하다. DFS의 경우 처음으로 발견되는 해답이 최단거리가 아닐 수 있지만 BFS의 경우 먼저 찾아지는 해답이 곧 최단거리이기 때문이다.