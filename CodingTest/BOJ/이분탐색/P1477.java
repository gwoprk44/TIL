
import java.io.*;
import java.util.*;

public class P1477 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken()); // 현재 휴게소 개수
        int m = Integer.parseInt(st.nextToken()); // 더 지으려는 휴게소 개수
        int l = Integer.parseInt(st.nextToken()); // 고속도로 길이

        int[] arr = new int[n+2];

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        arr[n+1] = l;
        arr[0] = 0;
        int left = 1;
        int right = l - 1;
        
        Arrays.sort(arr);

        while (left <= right) {
            int mid = (left + right) / 2;
            int ans = 0;

            for (int i = 1; i < arr.length; i++) {
                ans += (arr[i] - arr[i - 1] - 1) / mid;
            }

            if (ans > m) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        System.out.println(left);
    }
}
