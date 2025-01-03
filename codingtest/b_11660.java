package codingtest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class b_11660 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); //행렬(NxN)
        int M = Integer.parseInt(st.nextToken()); //질의 수

        int[][] sum = new int[N+1][N+1]; //누적합 배열

        for(int i = 1; i <= N; i++){ //누적합 배열 만드는 로직
            st =  new StringTokenizer(br.readLine());
            for(int j = 1; j <= N; j++){
                sum[i][j] = sum[i][j-1] + sum[i-1][j] - sum[i-1][j-1] + Integer.parseInt(st.nextToken());
            }
        }

        int answer = 0; //답을 담는 변수

        //구간 합 구한 후 질의 계산
        for(int i = 0; i < M; i++){ //잘의 개수만큼
            st = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            answer = sum[x2][y2] - sum[x1-1][y2] - sum[x2][y1-1] + sum[x1-1][y1-1];
            System.out.println(answer);
        }
    }

}