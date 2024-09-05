import java.io.*;
import java.util.StringTokenizer;

public class P1107 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringTokenizer st;

        for (int i = 0; i < T; i++) {
            boolean check = false;
            st = new StringTokenizer(br.readLine());
            
            int M = Integer.parseInt(st.nextToken());
            int N = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            
            for (int j = x; j < (N * M); j += M) {
                if (j % N == y) {
                    System.out.println(j + 1);
                    check = true;
                    break;
                }
            }
         
            if (!check) {
                System.out.println(-1);
            }
        }

    }
}
