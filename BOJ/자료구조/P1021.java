package 자료구조;

import java.io.*;
import java.util.*;

public class P1021 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        LinkedList<Integer> dq = new LinkedList<>();

        int ans = 0;
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken()); // 큐의 크기
        int M = Integer.parseInt(st.nextToken()); // 뽑의려는 숫자의 수

        // 1부터 N까지 
        for (int i = 1; i <= N; i++) {
            dq.offer(i);
        }

        int[] seq = new int[M]; // 뽑아내려는 숫자 배열
        
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            seq[i] = Integer.parseInt(st.nextToken());
        } 

        for (int i = 0; i < M; i++) {
            int target = dq.indexOf(seq[i]); // 뽑으려는 인덱스
            int mid;

            if (dq.size() % 2 == 0) {
                mid = dq.size() / 2 - 1;
            }
            else {
                mid = dq.size() / 2;
            }

            if (target <= mid) { // 2번 연산
                for (int j = 0; j < target; j++ ) {
                    int tmp = dq.pollFirst();
                    dq.offerLast(tmp);
                    ans++;
                }
            }

            else { // 3번 연산
                for (int k = 0; k < dq.size() - target; k++) {
                    int tmp = dq.pollLast();
                    dq.offerFirst(tmp);
                    ans++;
                }
            }
            
            dq.pollFirst();
        }
        System.out.println(ans);
    }
}