
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class P2470 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        int[] arr = new int[n];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(arr);

        int check = Integer.MAX_VALUE;
        int left = 0;
        int right = n - 1;
        int ans1 = 0;
        int ans2 = 0;

        while (left < right) {
            int sum = arr[left] + arr[right];

            if (Math.abs(sum) < check) {
                check = Math.abs(sum);
                ans1 = arr[left];
                ans2 = arr[right];
            }

            if (sum < 0) {
                left++; //음수니까 왼쪽 포인터 증가
            } else {
                right--; //양수니까 오른쪽 포인터 감소
            }
        }
        System.out.println(ans1 + " " + ans2);
    }    
}
