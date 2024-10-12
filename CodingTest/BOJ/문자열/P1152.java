package BOJ.문자열;
import java.io.*;
import java.util.*;

public class P1152 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        System.out.println(st.countTokens());
    }
}
