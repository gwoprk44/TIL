import java.io.*;
import java.util.*;

public class P9375 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb  = new StringBuilder();

        int T = Integer.parseInt(br.readLine());

        while (T --> 0) {
            HashMap<String, Integer> map = new HashMap<>(); // 옷 종류, 옷의 개수
            
            int ans = 1;
            int N = Integer.parseInt(br.readLine());

            while (N --> 0) { // 가진 의상수 만큼 반복
                StringTokenizer st = new StringTokenizer(br.readLine());
                st.nextToken(); // 옷 이름
                String cloth = st.nextToken(); // 옷 종류

                map.put(cloth, map.getOrDefault(cloth, 0) + 1);
            }

            for (int count : map.values()) {
                ans = ans *(count + 1);
            }

            sb.append(ans-1).append("\n");
        }
        
        System.out.println(sb);
    }
}
