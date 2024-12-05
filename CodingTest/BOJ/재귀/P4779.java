package CodingTest.BOJ.재귀;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P4779 {
    static StringBuilder sb;
    static int N;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String s;

        while ((s = br.readLine()) != null) {
            N = Integer.parseInt(s);
            int size = (int) Math.pow(3, N);

            sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                sb.append('-');
            }
            divide(0, size);
            System.out.println(sb);
        }
    }

    static void divide(int start, int size) {
        if (size == 1) return;

        int temp = size / 3;

        for (int i = start + temp; i < start + temp * 2; i++) {
            sb.setCharAt(i, ' ');
        }

        divide(start, temp);
        divide(start + temp * 2, temp);
    }
}
