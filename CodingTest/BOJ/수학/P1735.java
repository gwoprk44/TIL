import java.io.*;
import java.util.*;


public class P1735 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        // 입력값 받기
        st = new StringTokenizer(br.readLine());
        int a  = Integer.parseInt(st.nextToken());
        int b  = Integer.parseInt(st.nextToken());
        
        st = new StringTokenizer(br.readLine());
        int c = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());

        int num = a * d + b * c;
        int den = b * d;

        System.out.println(num / gcd(num, den) + " " + den / gcd(num, den));
    }

    public static int gcd(int a, int b) {
        if (a % b == 0) {
            return b;
        } else {
            return gcd(b, a % b);
        }
    }
}