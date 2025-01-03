package codingtest;

import java.io.*;
import java.util.*;

public class b_2480 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int ans = 0;

        if (a == b && b == c) {
            ans = 10000 +  a * 1000;
        } else if (a == b || a == c) {
            ans = 1000 + a * 100;
        } else  if (b  == c)  {
            ans = 1000 + b * 100;
        } else {
            int max = Math.max(a, Math.max(b, c));
            ans =  max * 100;
        }
        System.out.println(ans);
    }
}
