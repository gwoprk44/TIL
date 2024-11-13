package CodingTest.BOJ.기하;

import java.io.*;
import java.util.Arrays;

public class P14215 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] s = br.readLine().split(" ");

        int a = Integer.parseInt(s[0]);
        int b = Integer.parseInt(s[1]);
        int c = Integer.parseInt(s[2]);

        int[] arr = {a, b, c};

        Arrays.sort(arr);

        if (arr[0] + arr[1] > arr[2]) {
            System.out.println(a+b+c);
        } else {
            System.out.println(2*(arr[0]+arr[1]) - 1);
        }
    }
}
