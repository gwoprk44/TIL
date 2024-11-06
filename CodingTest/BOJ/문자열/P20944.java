import java.io.*;
import java.util.Arrays;

public class P20944 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(br.readLine());
        char[] arr = new char[n];

        Arrays.fill(arr, 'a');
        sb.append(arr);
        System.out.println(sb.toString());

    }    
}
