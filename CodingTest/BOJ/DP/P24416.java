package CodingTest.BOJ.DP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P24416 {

    static int fibocnt, fibodpcnt;
    static int[] dp;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        
        dp = new int[n];

        fibocnt = 0;
        fibodpcnt = 0;

        fibo(n);
        fibodp(n);

        System.out.println(fibocnt + " " + fibodpcnt);
    }

    static int fibo(int n) {
        if (n == 1 || n == 2) {
            fibocnt++;
            return 1;
        } else {
            return fibo(n - 1) + fibo(n - 2);
        }
    }

    static int fibodp(int n) {
        dp[1] = 1;
        dp[2] = 1;
        for (int i = 2; i < n; i++) {
            fibodpcnt++;
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n - 1];
    }
}
