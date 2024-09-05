import java.io.*;
import java.util.StringTokenizer;

public class P11724 {
    static int[][] arr;
    static int N, M;
    static boolean[] visited;
    static int ans;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        arr = new int[N+1][N+1];
        visited = new boolean[N+1];

        for (int i = 0; i < M; i++){
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            // 무방향 그래프
            arr[u][v] = 1;
            arr[v][u] = 1;
        }

        ans = 0;
        for (int i = 1; i <= N; i++) {
            if (!visited[i]) {
                dfs(i);
                ans++;
            }
        }
        System.out.println(ans);
    }

    public static void dfs(int value) {
        
        if (visited[value] == true) {
            return;
        }

        visited[value] = true;
        for (int i = 1; i <= N; i++) {
            if(arr[value][i] == 1) {
                dfs(i);
            }
        }
    }

}
