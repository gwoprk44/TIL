import java.io.*;
import java.util.*;

public class P13414 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int K = Integer.parseInt(st.nextToken()); // 수강인원 수
        int L = Integer.parseInt(st.nextToken()); // 대기열 길이
        int count = 0; // 대기번호 

        LinkedHashSet<String> set = new LinkedHashSet<>();

        for (int i = 0; i < L; i++) {
            String student = br.readLine();
            if (set.contains(student))  // 중복 클릭한 경우 
                set.remove(student); // 제거하고 대기열 맨 후순위
                set.add(student);
        }
    
        for (String s : set) {
            count++;
            System.out.println(s);
            if (count == K) break;
        }
    }
}