package BOJ.문자열;
import java.io.*;
import java.util.*;

public class P11656 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        String[] strArr = new String[s.length()];

        for (int i = 0; i < s.length(); i++) {
            strArr[i] = s.substring(i, s.length());
        }
        Arrays.sort(strArr);

        for (String str : strArr) {
            System.out.println(str);
        }
    }
}
