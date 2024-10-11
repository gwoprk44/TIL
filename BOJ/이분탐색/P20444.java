package 이분탐색;

import java.io.*;
import java.util.*;

public class P20444 {
    static long N, K;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        N = Long.parseLong(st.nextToken());
        K = Long.parseLong(st.nextToken());
        System.out.println(biSearch());
    }

    public static String biSearch() {
        long left = 0, right = N / 2;
        while (left <= right) {
            long row = (left + right) / 2;
            long col = N - row;
            long total = (row + 1) * (col + 1); // 색종이 조각의 개수
            if (total == K) { // k개의 색종이 조각을 만들 수 있는 경우
                return "YES";
            } else if (total < K) { // 색종이 조각이 부족한 경우, 더 잘게 잘라야 함
                left = row + 1; // 자르는 횟수 증가
            } else { // 색종이 조각이 넘치는 경우, 더 크게 잘라야 함
                right = row - 1; // 자르는 횟수 감소
            }
        }
        return "NO";
    }
}