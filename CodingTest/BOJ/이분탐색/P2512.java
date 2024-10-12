package BOJ.이분탐색;

import java.io.*;
import java.util.StringTokenizer;

public class P2512 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int left = 0;
        int right = -1;
        int[] arr = new int[n];
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            right = Math.max(right, arr[i]);
        }
        
        int m = Integer.parseInt(br.readLine());
        while (left <= right) {
            int mid = (left + right) / 2;
            long budget = 0; // 예산
            
            for (int i = 0; i < n; i++) {
                if(arr[i] > mid) {
                    budget += mid; // 예산 상한액
                } else {
                    budget += arr[i]; // 요청 예산
                }
            }

            if (budget <= m) {    // 예산 총량
                left = mid + 1;   
            } else {              // 예산 초과
                right = mid - 1;
            }
        }
        System.out.println(right);
    }
}
