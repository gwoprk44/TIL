import java.io.*;

public class P16171 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String S = br.readLine(); //입력
        String K = br.readLine(); //찾고자하는

        String target = S.replaceAll("[0-9]", ""); // 입력값에서 숫자 제거

        if (target.contains(K)) {
            System.out.println(1);
        }
        else {
            System.out.println(0);
        }
    }
}
