import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class P6603 {
    static int k;
    static int[] S;
    static int[] arr;
    static boolean[] visit;

    public static void dfs(int start, int depth) {
        if (depth == 6) {
            for (int val : arr) {
                System.out.println(val + " ");
            }
            System.out.println();
            return;
        }

        for (int i = start; i < k; i++) {
            if (!visit[i]) {
                visit[i] = true;
                arr[depth] = S[i];
                dfs(i, depth + 1);
                visit[i] = false;
            }
        }
        return;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        while (true) {
            st = new StringTokenizer(br.readLine());
            k = Integer.parseInt(st.nextToken());
            if (k == 0) break;
            S = new int[k];
            
            for (int i = 0; i < k; i++) {
                S[i] = Integer.parseInt(st.nextToken());
            }

            visit = new boolean[k];
            arr = new int[6];

            dfs(0,0);
            System.out.println();
        }
    }
}
