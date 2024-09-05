import java.io.*;
import java.util.*;

public class P15658 {
    static int N, MAX = -1_000_000_001, Min = 1_000_000_001;
    static int[] numArr;
    static int[] opArr;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        numArr = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numArr[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        opArr = new int[4];
        for (int i = 0; i < 4; i++) {
            opArr[i] = Integer.parseInt(st.nextToken());
        }

        dfs(1, numArr[0]);
        System.out.print(MAX + "\n" + Min);
    }

    public static void dfs(int depth, int sum) {
        if (depth == N) {
            MAX = Math.max(MAX, sum);
            Min = Math.min(Min, sum);
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (opArr[i] > 0) {
                opArr[i]--;
                if (i == 0) dfs(depth + 1, sum + numArr[depth]);
                else if (i == 1) dfs(depth + 1, sum - numArr[depth]);
                else if (i == 2) dfs(depth + 1, sum * numArr[depth]);
                else dfs(depth + 1, sum / numArr[depth]);
                opArr[i]++;
            }
        }
    }
}

