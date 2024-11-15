package CodingTest.BOJ.정렬;

import java.io.*;
import java.util.Arrays;

public class P2578 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int arr[] = new int[5];
        int sum = 0;
        int avg = 0;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(br.readLine());
            sum += arr[i];
        }

        avg = sum / 5;
        Arrays.sort(arr);
        
        System.out.print(avg +"\n");
        System.out.print(arr[2]);
    }
}
