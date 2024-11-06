import java.io.*;

public class P1259 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String s = br.readLine();
            StringBuilder sb = new StringBuilder(s);

            String reveString = sb.reverse().toString();

            if (s.equals("0")) break;

            if (s.equals(reveString)) {
                System.out.println("yes");
            } else{
                System.out.println("no");
            }
        }
    }
}
