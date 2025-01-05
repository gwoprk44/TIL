package codingtest;

import java.io.*;
import java.util.*;

public class b_2869 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        long a = Long.parseLong(st.nextToken());
        long b = Long.parseLong(st.nextToken());
        long v = Long.parseLong(st.nextToken());

        long daydist = a - b;
        long goaldist = v - b;

        if (goaldist % daydist != 0) {
            System.out.println(goaldist / daydist + 1);
        } else {
            System.out.println(goaldist / daydist);
        }

    }
}
