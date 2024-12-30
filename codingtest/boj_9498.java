package codingtest;

import java.io.*;

public class boj_9498 {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        if (n <= 100 && n >= 90) System.out.println('A');
        else if (n <= 89 && n >= 80) System.out.println('B');
        else if (n <= 79 && n >= 70) System.out.println('C');
        else if (n <= 69 && n >= 60) System.out.println('D');
        else System.out.println('F');
    }
}
