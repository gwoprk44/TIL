import java.io.*;
import java.util.*;

public class P1620 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();

        HashMap<Integer, String> map1 = new HashMap<>(); // 번호 이름
        HashMap<String, Integer> map2 = new HashMap<>(); // 이름 번호

        for (int i = 1; i <= N; i++) {
            String S = br.readLine();
            map1.put(i, S);
            map2.put(S, i);
        }

        for (int i = 0; i < M; i++) {
            String S = br.readLine();
            if (S.charAt(0) >= 49 && S.charAt(0) <= 57 ) {
                sb.append(map1.get(Integer.parseInt(S))).append("\n");
            } else {
                sb.append(map2.get(S)).append("\n");
            }
        }

        System.out.println(sb);
    }
}
