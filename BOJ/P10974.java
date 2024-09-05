import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P10974 {
    public static int N;
    public static int[] arr;
    public static boolean[] visit;
    public static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        arr = new int[N];
        visit = new boolean[N];

        dfs(0);
        System.out.println(sb.toString());
    }

    public static void dfs(int depth) {
        if (depth == N) {
            for (int i = 0; i < N; i++) {
                sb.append(arr[i] + " ");
            }
            sb.append("\n");
            return;
        }

        for (int i = 0; i < N; i++) {
            if (visit[i]) 
                continue;
            
            arr[depth] = i + 1;
            visit[i] = true;
            dfs(depth + 1);
            visit[i] = false;
        }
    }
}
