package CodingTest.BOJ.정렬;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class P18870 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        // 원본 배열
        int[] origin = new int[N];
        // 정렬한 배열
        int[] sorted = new int[N];

        // 해시맵 선언 (중복된 값을 제외하기위해)
        HashMap<Integer, Integer> rankMap = new HashMap<>();

        StringTokenizer st = new StringTokenizer(br.readLine());

        for (int i = 0; i < N; i++) {
            origin[i] = sorted[i] = Integer.parseInt(st.nextToken());
        }

        int rank = 0;
        // 배열 정렬
        Arrays.sort(sorted);

        
        for (int v : sorted) {
            if(!rankMap.containsKey(v)) {
                rankMap.put(v, rank);
                rank++;
            }
        }

        StringBuilder sb = new StringBuilder();

        for (int key : origin) {
            int ranking = rankMap.get(key);
            sb.append(ranking).append(" ");
        }

        System.out.println(sb);

    }
}

