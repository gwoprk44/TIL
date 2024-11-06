
import java.io.*;

public class P2417 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(br.readLine());
        System.out.println(bSearch(n));
    }

    static long bSearch(long key) {
        long left = 0;
        long right = key;
        long ans = 0;
        
        while (left <= right) {
            long mid = (left + right) / 2;
            if (key <= (long)Math.pow(mid, 2)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
}
