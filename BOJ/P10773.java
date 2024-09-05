import java.io.*;
import java.util.*;

public class P10773 {
    static int K;

    public static void main(String[] args) throws IOException {
        Stack<Integer> st = new Stack<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        K = Integer.parseInt(br.readLine());

        for (int i = 0; i < K; i++) {
            int num = Integer.parseInt(br.readLine());

            if (num == 0) {
                st.pop();
            }
            else {
                st.push(num);
            }
        }
        
        int sum = 0;
        for (int n : st) {
            sum += n;            
        }
        
        System.out.println(sum);
    }
}
