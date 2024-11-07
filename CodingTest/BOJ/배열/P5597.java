import java.io.*;

public class P5597 {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      boolean[] arr = new boolean[30];

      for (int i = 0; i < 28; i++) {
        int student = Integer.parseInt(br.readLine());
        arr[student - 1] = true;
      }

      for (int i = 0; i < arr.length; i++) {
        if(!arr[i]) {
            System.out.println(i + 1);
        }
      }
    }
}
