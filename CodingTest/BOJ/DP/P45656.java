package CodingTest.BOJ.DP;

import java.io.*;

public class P45656 {
    static int N;
    static final long MOD = 1_000_000_000L;
    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());

        long[][] dp = new long[N+1][10];

        // init
        for(int i=0; i<=9; i++) {
            dp[1][i] = 1L;
        }

        for(int i=2; i<=N; i++) {
            // 0으로 시작하는 수의 개수
            dp[i][0] = dp[i-1][1];
            for(int j=1; j<=9; j++) {
                // 9일경우는 이전 자릿수의 8로 시작하는 수의 개수와 동일하다!
                if(j == 9) dp[i][9] = dp[i-1][8] % MOD;
                // 나머지 수들은 이전 숫자의 j-1로 시작하는 수의 개수와 j+1로 시작하는 수의 개수를 더한 값이다.
                else dp[i][j] = (dp[i-1][j-1] % MOD + dp[i-1][j+1] % MOD) % MOD;
            }
        }

        long result = 0;
        for(int i=1; i<=9; i++) {
            result = (result + dp[N][i]) % MOD;   
        }
        System.out.print(result);
    }
}
