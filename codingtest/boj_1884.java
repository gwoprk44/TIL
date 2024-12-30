package codingtest;

import java.io.*;
import java.util.*;

public class boj_1884 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int H = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        if (M < 45) {
            H--;
            M = 60 - (45 - M);

            if (H < 0) {
                H = 23;
            } 
            System.out.println(H + " " + M);
        } else {
            System.out.println(H + " " + (M-45));
        }
    }
}
