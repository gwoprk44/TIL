
package CodingTest.BOJ.심화;

import java.io.*;


public class P1157 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] arr = new int[26];
        
        String str = br.readLine().toUpperCase();

        for (int i = 0; i < str.length(); i++) {
            int num = str.charAt(i) - 'A';
            arr[num]++;
        }

        int max = 0;
        char answer = '?';

        for (int i = 0; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
                answer = (char)(i+'A');
            } else if (max == arr[i]) {
                answer = '?';
            }
        }
        System.out.println(answer);
    }
}