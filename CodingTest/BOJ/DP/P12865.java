package CodingTest.BOJ.DP;

import java.io.*;
import java.util.*;

public class P12865 {

    static int[] W;
    static int[] V;
    static Integer[][] dp;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        W = new int[N+1];
        V = new int[N+1];

        dp = new Integer[N][K+1];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            W[i] = Integer.parseInt(st.nextToken());
            V[i] = Integer.parseInt(st.nextToken());
        }

        System.out.println(knapsack(N-1, K));

    }

    static int knapsack(int i, int k) {
        if (i < 0) {
            return 0;
        }

        if (dp[i][k] == null) {
            if(W[i] > k) {
                dp[i][k] = knapsack(i - 1, k);
            }
            else {
                dp[i][k] = Math.max(knapsack(i - 1, k), knapsack(i - 1, k - W[i]) + V[i]);
            }
        }
        return dp[i][k];
    }
}
