
import java.io.*;
import java.util.*;

public class P19637 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        String[] title = new String[n]; //칭호
        int[] power = new int[n];       //전투력
        

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            title[i] = st.nextToken();
            power[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < m; i++) {
            int num = Integer.parseInt(br.readLine());
            
            int start = 0;
            int last = n - 1;

            while (start <= last) {
                int mid = (start + last) / 2;
                if (power[mid] < num) {
                    start = mid + 1;
                } else {
                    last = mid - 1;
                }
            }
            sb.append(title[start]).append("\n");
        }
        System.out.println(sb.toString());
    }
    
}