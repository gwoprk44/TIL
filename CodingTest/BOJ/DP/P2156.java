package CodingTest.BOJ.DP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P2156 {

    static int[] dp;
    static int[] wine;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        dp = new int[n+1];
        wine = new int[n+1];

        for (int i = 1; i <= n; i++) {
            wine[i] = Integer.parseInt(br.readLine());
        }

        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            if (i == 1) dp[i] = wine[i];
            else if (i == 2) dp[i] = wine[i-1] + wine[i];
            else dp[i] = Math.max(dp[i-1], Math.max(dp[i-2] + wine[i], dp[i-3] + wine[i-1] + wine[i]));
        }
        System.out.println(dp[n]);
    }
}
