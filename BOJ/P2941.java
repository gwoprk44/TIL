import java.io.*;

public class P2941 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] str = {"c=", "c-", "dz=", "d-", "lj", "nj", "s=", "z="};

        String s = br.readLine();

        for (int i = 0; i < str.length; i++) {
            if (s.contains(str[i])) {
                s = s.replace(str[i], "@");
            }
        }
        System.out.println(s.length());
    }
}
