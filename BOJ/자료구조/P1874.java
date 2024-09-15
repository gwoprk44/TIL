package 자료구조;

import java.io.*;
import java.util.*;

public class P1874 {

    public static void main(String[] args) throws IOException {
    
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringBuilder sb = new StringBuilder();

    int N = Integer.parseInt(br.readLine());
    int start = 0;
    Stack<Integer> st = new Stack<>();

    while (N --> 0) {
        int value = Integer.parseInt(br.readLine());
        
        if (value > start) {
            for (int i = start + 1; i <= value; i++) {
                st.push(i);
                sb.append("+").append("\n");
            }
            start = value;
        }
        else if (st.peek() != value) {
            System.out.println("NO");
            return;
        }
        st.pop();
        sb.append("-").append("\n");        
    }

    System.out.println(sb);
    }

}
