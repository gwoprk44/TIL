package codingtest;

import java.io.*;

public class boj_27866 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String s = br.readLine();
        int i = Integer.parseInt(br.readLine())-1;

        System.out.println(s.charAt(i));
    }
}
