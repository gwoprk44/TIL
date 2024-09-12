import java.io.*;
import java.util.*;

public class P11478 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        HashSet<String> set = new HashSet<>();

        String S = br.readLine();

        for (int i = 0; i < S.length(); i++) {
            for (int j = i + 1; j <= S.length(); j++) {
                set.add(S.substring(i, j));
            }
        }
        System.out.println(set.size());
    }
}
