import java.io.*;

public class P1193 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int x = Integer.parseInt(br.readLine());

        int count = 1;
        int sum = 0;

        while (true) {
            if (x <= count + sum) {
                if (count % 2 == 1) {
                    System.out.println((count - (x - sum - 1)) + "/" + (x - sum));
                    break;
                } else {
                    System.out.println((x - sum) + "/" + (count - (x - sum - 1)));
                    break;
                }
            } else {
                sum += count;
                count++;
            }
        }
    }
}
