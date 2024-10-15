package BOJ.수학;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P2720 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int t = Integer.parseInt(br.readLine()); // 테스트케이스

        for (int i = 0; i < t; i++) {
            int c = Integer.parseInt(br.readLine()); // 거스름돈
            
            int quarter = c / 25;
            c %= 25;

            int dime = c / 10;
            c %= 10;

            int nickel = c / 5;
            c %= 5;

            int penny = c;

            System.out.println(quarter + " " + dime + " " + nickel + " " + penny);
            
        }

    }
}
