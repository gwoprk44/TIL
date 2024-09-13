import java.io.*;
import java.util.*;

public class P19583 {
    static String S, E, Q;
    static int ans = 0;
    static HashSet<String> set = new HashSet<>();
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());        
            
        S = st.nextToken();
        E = st.nextToken();
        Q = st.nextToken();

        String input;
        
        while ((input = br.readLine()) != null) {
            String[] str = input.split(" ");
            if (str[0].compareTo(S) <= 0) {
                set.add(str[1]);
            }
            else if (str[0].compareTo(E) >= 0 && str[0].compareTo(Q) <= 0) {
                if (set.contains(str[1])) {
                    ans++;
                    set.remove(str[1]);
                }
            }
        }
        System.out.println(ans);
    }
}
