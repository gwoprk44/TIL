import java.io.*;
import java.util.StringTokenizer;

public class P16931 {
    static StringBuilder sb = new StringBuilder();
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static int ans;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        int[][] cube = new int[N][M];

        // 초기화
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                cube[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        ans += N * M * 2;
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                for (int k = 0; k < 4; k++) { //사방탐색
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if (nx < 0 || ny < 0 || nx > N - 1 || ny > M - 1) {
                        ans += cube[i][j];
                        continue;
                    }
                    if (cube[nx][ny] < cube[i][j]) {
                        ans += cube[i][j] - cube[nx][ny];
                    }
                }
            }
        }
        sb.append(ans).append("\n");
        System.out.println(sb);
    }
}
