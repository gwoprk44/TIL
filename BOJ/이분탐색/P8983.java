package 이분탐색;

import java.io.*;
import java.util.*;

public class P8983 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int m = Integer.parseInt(st.nextToken()); // 사대의 수
        int n = Integer.parseInt(st.nextToken()); // 동물의 수
        long l = Long.parseLong(st.nextToken());  // 사정거리

        int[] range = new int[m];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            range[i] = Integer.parseInt(st.nextToken());
        }

        int[][] animal = new int[n][2];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            animal[i][0] = Integer.parseInt(st.nextToken());
            animal[i][1] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(range);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int start = 0;
            int end = m - 1;
            while (start <= end) {
                int mid = (start + end) / 2;
                long dis = Math.abs(range[mid] - animal[i][0]) + animal[i][1];

                if (dis <= l) {
                    ans++;
                    break;
                }

                if (animal[i][0] < range[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            }
        }
        System.out.println(ans);
    }
}
