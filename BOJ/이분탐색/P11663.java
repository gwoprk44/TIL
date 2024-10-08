package 이분탐색;

import java.io.*;
import java.util.*;

public class P11663 {

    static int[] point;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        point = new int[n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            point[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(point);

        
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            int start_idx = bSearch(start, 0);
            int end_idx = bSearch(end, 1);
            
            System.out.println(end_idx - start_idx);
        }
    }

    public static int bSearch(int start, int check) {
        int left = 0;
        int right = point.length - 1;

        if (check == 0) {
            while (left <= right) {
                int mid = (left + right) / 2;
                if (point[mid] < start) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return left;
        }
        else {
            while (left <= right) {
                int mid = (left + right) / 2;
                if (point[mid] < start) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return right + 1;
        }
    }
}
