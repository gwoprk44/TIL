package codingtest;

import java.io.*;
import java.util.*;

public class b_2501 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int count = 0;
        int answer = 0;

        for (int i = 1; i <= n; i ++) {
            if (n % i == 0) {
                count++;
            }
            if (count == k) {
                answer = i;
                break;
            }
        }
        System.out.println(answer);
    }
}
