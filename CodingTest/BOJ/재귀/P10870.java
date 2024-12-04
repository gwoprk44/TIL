package CodingTest.BOJ.재귀;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P10870 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        System.out.println(fibo(N));
    }   

    static int fibo(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        return fibo(n-1) + fibo(n-2);
    }
}
