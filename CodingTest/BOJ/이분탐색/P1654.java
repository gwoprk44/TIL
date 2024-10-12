package BOJ.이분탐색;

import java.io.*;
import java.util.*;

public class P1654 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int k = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());

        int[] arr = new int[k];

        long max = 0;

        for (int i = 0; i < k; i++) {
            arr[i] = Integer.parseInt(br.readLine());
            if (max < arr[i]) {
                max = arr[i];
            }
        }

        max++;

        long mid = 0;
        long min = 0;

        while (min < max) {
            mid = (min + max) / 2;
            
            long count = 0;
            for (int i = 0; i < arr.length; i++) {
                count += (arr[i] / mid);
            }

            /*
	        *  [upper bound 형식]
	        *  
	        *  mid길이로 잘랐을 때의 개수가 만들고자 하는 랜선의 개수보다 작다면
	        *  자르고자 하는 길이를 줄이기 위해 최대 길이를 줄인다.
	        *  그 외에는 자르고자 하는 길이를 늘려야 하므로 최소 길이를 늘린다.
	        */
            
            if (count < n) {
                max = mid;
            } else {
                min = mid + 1;
            }
        }

        System.out.println(min - 1);
    }    
}
