package CodingTest.BOJ.심화;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;

public class P26069 {
    public static void main(String[] args) throws IOException { 
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        HashSet<String> dance = new HashSet<>();
        dance.add("ChongChong");

        int N = Integer.parseInt(br.readLine());

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String A = st.nextToken();
            String B = st.nextToken();
            
            if (dance.contains(A) || dance.contains(B)) {
                dance.add(A);
                dance.add(B);
            }
        }       
        System.out.println(dance.size());
    }   
}
