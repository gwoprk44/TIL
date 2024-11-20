import java.io.*;

public class P13909 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int ans = 0;

        for (int i = 1; i * i <= N; i++) {
            ans++;
        }

        System.out.println(ans);
    }
}
