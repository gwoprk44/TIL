
import java.util.*;
import java.io.*;

public class P4358 {
    static String str;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        HashMap<String ,Integer> map = new HashMap<>();
        int count = 0;
        
        while((str = br.readLine()) != null) {
            map.put(str, map.getOrDefault(str, 0) + 1);
            count++;
        }
        
        Object[] keyArr = map.keySet().toArray(); // 키 값을 배열로 받아옴
        Arrays.sort(keyArr); 
        
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < keyArr.length; i++) {
            int value = map.get(keyArr[i]);
            double percent = ((double)value /(double) count) * 100;
            sb.append(keyArr[i] + " " + String.format("%.4f", percent) + "\n");
        }
        System.out.println(sb.toString());
    }
}