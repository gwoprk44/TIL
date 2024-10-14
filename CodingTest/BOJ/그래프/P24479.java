package BOJ.그래프;

import java.io.*;
import java.util.*;

public class P24479 {
        static int cnt; // 방문순서
        static int[] visited; // 방문 정점 기록
        static ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());

        /*  방문한 정점이 최대 정점의 개수만큼 있기 때문에, 정점의 개수만큼의 크기로 배열 생성
         *  index 혼란을 방지하고자 0번 인덱스를 더미 데이터로 활용
         */
        visited = new int[n + 1];    

        // 그래프 초기화
        for (int i = 0; i < n + 1; i++) {
            graph.add(new ArrayList<Integer>());
        }

        // 리스트에 값 저장 
        // 무방향이니까 양쪽 정보 
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        // 그래프 오름차순 정렬
        for (int i = 1; i <= n; i++) {
            Collections.sort(graph.get(i));
        }
        // 시작 정점도 순서에 포함이므로 cnt 초기값 1 할당
        cnt = 1;
        dfs(r);

        for (int i = 1; i < visited.length; i++) {
            sb.append(visited[i]).append("\n");
        }
        System.out.println(sb);
    }

    public static void dfs(int node) {
        visited[node] = cnt; // 현재 방문하고있는 정점에 순서 저장
        
        // 현재 위치한 정점이 갈 수 있는 정점 리스트를 순회
        for (int i = 0; i < graph.get(node).size(); i++) {
            int newNode = graph.get(node).get(i);
            //다음 갈 정점을 방문했었는지 체크(0일 경우 방문 X)
            if (visited[newNode] == 0) {
                cnt++;
                dfs(newNode);
            }
        }
    }
}
