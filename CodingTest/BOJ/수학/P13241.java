import java.io.*;
import java.util.*;

public class P13241 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        long a = Long.parseLong(st.nextToken());
        long b = Long.parseLong(st.nextToken());


        if (a > b) {
            System.out.println(a*b / findGcd(a,b));
        } else if (b > a) {
            System.out.println(a * b / findGcd(b,a));
        } else {
            System.out.println(a);
        }
    }

    static long findGcd(long a, long b) {
        while (b != 0) {
            long r = a % b;
            a = b;
            b = r;
        }
        return a;
    }
}