import java.io.*;
import java.util.*;

public class P17298 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Stack<Integer> stack = new Stack<>();

        int N = Integer.parseInt(br.readLine());
        int[] seq = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        for (int i = 0; i < N; i++) {
            seq[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < N; i++) {
            /* 스택이 비어있지 않으면서
             * 현재 원소가 스택의 top 원소가 가리키는 원소보다 큰 경우
             * 조건을 만족할 때 까지 스택의 원소를 pop 하면서
             * 해당 인덱스의 값을 현재 원소로 바꾼다
             */

            while (!stack.isEmpty() && seq[stack.peek()] < seq[i]) {
                    seq[stack.pop()] = seq[i];
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            seq[stack.pop()] = -1;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(seq[i]).append(' ');
        }
        System.out.println(sb);
    }
}
