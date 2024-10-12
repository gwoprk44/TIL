package BOJ.자료구조;

import java.io.*;
import java.util.*;

public class P2164 {
    public static void main(String[] args) throws IOException {
        
        LinkedList<Integer> q = new LinkedList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int N = Integer.parseInt(br.readLine());

        for (int i = 1; i <= N; i++) {
            q.offer(i);
        }
        
        while (q.size() > 1) {
            q.poll();
            q.offer(q.poll());
        }
        System.out.println(q.poll());
    }    
}