package BOJ.문자열;
import java.io.*;

public class P10988 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder(br.readLine());

        System.out.println(sb.toString().equals(sb.reverse().toString()) ? 1 : 0);
    }
}
