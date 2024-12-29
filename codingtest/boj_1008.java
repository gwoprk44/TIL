
package codingtest;

import java.io.*;
import java.util.*;

public class boj_1008 {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer  st = new StringTokenizer(br.readLine());

        double n = Double.parseDouble(st.nextToken());
        double m = Double.parseDouble(st.nextToken());

        System.out.println(n / m);
    }
}
