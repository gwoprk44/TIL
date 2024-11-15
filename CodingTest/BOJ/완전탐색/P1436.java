import java.io.*;

public class P1436 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int N = Integer.parseInt(br.readLine());

        int cnt = 1;
        int num = 666;

        while (cnt != N) {
            num++;
            if (String.valueOf(num).contains("666")) cnt++;
        }
        System.out.println(num);
    }
}
