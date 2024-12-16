package CodingTest.BOJ.DP;

import java.io.*;
import java.util.*;

public class P11054{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int[] arr = new int[N+1];
        int[] dpLR = new int[N+1];
        int[] dpRL = new int[N+1];

        StringTokenizer st = new StringTokenizer(br.readLine()," ");

        for (int i = 1; i <= N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            dpLR[i] = 1;
            dpRL[i] = 1;
        }

        // 왼쪽에서 오른쪽으로 증가
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j < i; j++) {
                if (arr[i] > arr[j]) {
                    dpLR[i] = Math.max(dpLR[i], dpLR[j]+1);
                }
            }
        }

        // 오른쪽에서 왼쪽으로 감소
        for (int i = N; i >= 1; i--) {
            for (int j = N; j > i; j--) {
                if (arr[i] > arr[j]) {
                    dpRL[i] = Math.max(dpRL[i], dpRL[j]+1);
                }
            }
        }

        int max = 1;
        for (int i = 1; i <= N; i++) {
            max = Math.max(max, dpLR[i] + dpRL[i]);
        }


        System.out.println(max-1);
    }
}