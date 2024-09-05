import java.io.*;
import java.util.*;

public class P1182 {
    static int n, s, ans;
    static int[] arr;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        s = Integer.parseInt(st.nextToken());

        arr = new int[n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        ans = 0;
        dfs(0, 0);
        
        if(s == 0) { // 양수인 부분수열만 뽑기위해
            System.out.println(ans - 1);
            return;
        }
        System.out.println(ans);
    }
    
    public static void dfs(int depth, int sum) {
        if (depth == n) {
            if (sum == s) {
                ans++;
            }
            return;
        }
        // 지금 숫자 뽑는다
        dfs(depth + 1, sum + arr[depth]);
        // 지금 숫자 안 뽑는다
        dfs(depth + 1, sum);
    }
}
