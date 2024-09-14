import java.io.*;

public class P9996 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        
        String pattern = br.readLine();
        String[] arr = pattern.split("\\*");

        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            if (arr[0].length() + arr[1].length() > str.length()) {
                System.out.println("NE");
                continue;
            }

            String front = str.substring(0, arr[0].length()); // 앞부분
            String back = str.substring(str.length() - arr[1].length(), str.length()); // 뒷부분

            if (front.equals(arr[0]) && back.equals(arr[1])) {
                System.out.println("DA");
            } else {
                System.out.println("NE");
            }
        }
    }
}