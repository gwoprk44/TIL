package problemsolve.BOJ;

import java.io.*;
import java.util.*;


public class BOJ_10773 {

    public static void main(String[] args) throws IOException {
        Stack<Integer> stack = new Stack<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int K = Integer.parseInt(br.readLine());
        int ans = 0;

        while (K --> 0) {
            int N = Integer.parseInt(br.readLine());
            if (N == 0) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                stack.push(N);
            }
        }

        while (!stack.isEmpty()) {
            ans += stack.pop();
        }
        System.out.println(ans);
    }
}