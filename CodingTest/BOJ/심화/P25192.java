package CodingTest.BOJ.심화;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class P25192 {
    public static void main(String[] args) throws IOException {
        HashSet memSet = new HashSet<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int ans = 0;

        for (int i = 0; i < N; i++) {
            String chat = br.readLine();
            
            if (chat.equals("ENTER")) {
                ans+= memSet.size();

                memSet = new HashSet<>();
            } else {
                memSet.add(chat);
            }
        }
        
        ans+= memSet.size();
        System.out.println(ans);


    }
}
