package codingtest;

import java.io.*;


public class b_24265 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        long n = Integer.parseInt(br.readLine());

        System.out.println((n*(n-1))/2);
        System.out.println(2);

    }    
}