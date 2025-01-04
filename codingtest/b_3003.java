package codingtest;

import java.io.*;
import java.util.*;


public class b_3003 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int[] chess = {1, 1, 2, 2, 2, 8};

        for (int i = 0; i < 6; i++) {
            int input = Integer.parseInt(st.nextToken());
            System.out.print(chess[i] - input + " ");
        }

    }
}
