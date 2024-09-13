import java.io.*;

public class P1543 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String text = br.readLine();
        String word = br.readLine();
        int ans = 0;

        text = text.replaceAll(word, "1");

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '1') {
                ans++;
            }
        }

        System.out.println(ans);
    }    
}
