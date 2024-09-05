import java.io.*;
import java.util.*;

public class P15649 {

    public static int[] arr;
    public static boolean[] visit;
    public static StringBuilder sb = new StringBuilder();
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); 
        int M = Integer.parseInt(st.nextToken());

        arr = new int[M]; // 값을 담을 배열
        visit = new boolean[N]; // 다음 노드를 탐색하기 위한 배열
        dfs(N, M, 0);
        System.out.println(sb);
    }

    public static void dfs(int N, int M, int depth) {
        // 재귀 깊이가 M과 같아지면 배열 출력
        if (depth == M) {
            for (int val : arr) {
                sb.append(val).append(" ");
            }
            sb.append('\n');
            return;
        }

        for (int i = 0; i < N; i++) {
            if (!visit[i]) {

                visit[i] = true;
                arr[depth] = i + 1;
                dfs(N, M, depth+1);

                visit[i] = false;
            }
        }
        return;
    }
}
