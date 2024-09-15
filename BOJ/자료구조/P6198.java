package 자료구조;

import java.io.*;
import java.util.*;

public class P6198 {
    public static long ans;
    public static Stack<Integer> stack = new Stack<>();
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        for (int i = 1 ; i <= N; i++) {
            int height = Integer.parseInt(br.readLine());

            while (!stack.isEmpty()) {
                if (stack.peek() <= height) { // i 번째 빌딩보다 낮거나 같은 애들은 스택에서 제외
                    stack.pop();
                }
                else break;
            }
            ans += stack.size();
            stack.push(height);
        }

        System.out.println(ans);
    }
}
