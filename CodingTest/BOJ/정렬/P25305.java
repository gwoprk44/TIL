package CodingTest.BOJ.정렬;

import java.io.*;
import java.util.*;

public class P25305 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken()); // 응시자의 수
        int k = Integer.parseInt(st.nextToken()); // 상을 받는 사람의 수

        int[] score = new int[n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            score[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(score);

        System.out.println(score[n-k]);


    }
}
