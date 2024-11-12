import java.io.*;
import java.util.StringTokenizer;

public class P2563 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int paper = Integer.parseInt(br.readLine());
        boolean[][] arr = new boolean[101][101];
        int x = 0;
        int y = 0;
        int answer = 0;

        StringTokenizer st;

        for (int i = 0; i < paper; i++) {
            st = new StringTokenizer(br.readLine());
            x = Integer.parseInt(st.nextToken());
            y = Integer.parseInt(st.nextToken());
            for (int j = x; j < x + 10; j++) {
                for (int k = y; k < y + 10; k++) {
                    if(!arr[j][k]) {
                        arr[j][k] = true;
                        answer++;
                    }
                }
            }
        }
        System.out.println(answer);
    }    
}
