import java.io.*;
import java.util.*;

public class P3986 {
    static int N;
    static int ans;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ans = 0;
        N = Integer.parseInt(br.readLine());

        for (int i = 0; i < N; i++) {
            Stack<Character> stack = new Stack<>();
            String word = br.readLine();
            for (int j = 0; j < word.length(); j++) {
                if (!stack.isEmpty() && stack.peek() == word.charAt(j)) {
                    stack.pop();
                } else {
                    stack.push(word.charAt(j));
                }
            }
            if (stack.size() == 0) {
                ans++;
            }
        }
        System.out.println(ans);
    }
}
