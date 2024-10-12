package BOJ.문자열;
import java.io.*;
import java.util.*;

public class P2204 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        HashMap<String, String> hashMap = new HashMap<>();

        while (true) {
            int n = Integer.parseInt(br.readLine());
            if (n == 0) break;
            String[] str = new String[n];
            
            for (int i = 0; i < n; i++) {
                str[i] = br.readLine();
            }

            for (int i = 0; i < n; i++ ) {
                String tmp = str[i].toLowerCase();
                hashMap.put(tmp, str[i]);
                str[i] = str[i].toLowerCase();
            }
            Arrays.sort(str);
            System.out.println(hashMap.get(str[0]));
        }        



    }
}
