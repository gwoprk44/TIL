import java.io.*;
import java.util.*;

public class P15664 {
    static int n, m;
    static int[] arr;
    static int[] ans;
    static boolean[] visited;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        arr = new int[n];
        ans = new int[n];
        visited = new boolean[n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(arr);
        dfs(0, 0);
        System.out.println(sb);
    }

    public static void dfs(int start, int depth) {
        if (depth == m) {
            for (int i = 0; i < m; i++) {
                sb.append(ans[i]).append(" ");
            }
            sb.append("\n");
            return;
        }
        else {
            int before = 0;
            for (int i = start; i < n; i++) { // 비내림차순
                if (visited[i]) continue;

                if (before != arr[i]) {
                    visited[i] = true;
                    ans[depth] = arr[i];
                    before = arr[i];
                    dfs(i + 1, depth + 1);
                    visited[i] = false;
                }
            }
        } 
        
    }
}
