package CodingTest.BOJ.재귀;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class P11729 {
    static StringBuilder sb = new StringBuilder();
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        sb.append((int) (Math.pow(2, N) - 1)).append("\n");
        Hanoi(N, 1, 2, 3);
        System.out.println(sb);
    }

    static void Hanoi(int N, int start, int mid, int to) {
        if (N == 1) {
            sb.append(start + " " + to + "\n");
            return;
        }
        // N-1개의 원판을 시작지점에서 중간지점으로 이동
        Hanoi(N-1, start, to, mid);
        // N번째 원판을 시작지점에서 목표지점으로 이동
        sb.append(start + " " + to + "\n");
        // N-1개의 원판을 중간지점에서 목표지점으로 이동
        Hanoi(N-1, mid, start, to);
    }
}
