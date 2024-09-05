import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class P16967 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int H = Integer.parseInt(st.nextToken());
        int W = Integer.parseInt(st.nextToken());
        int X = Integer.parseInt(st.nextToken());
        int Y = Integer.parseInt(st.nextToken());

        int[][] arrA = new int[H][W];
        int[][] arrB = new int[H + X][W + Y];

        //초기화
        for (int i = 0; i < H + X; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0 ; j < W + Y; j++) {
                arrB[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < H + X; i++) {
            for (int j = 0; j < W + Y; j++) {
                if (i < X && j < W) { // 위 안겹치는 부분
                    arrA[i][j] = arrB[i][j];
                }
                else if (i < H && j < Y) {
                    arrA[i][j] = arrB[i][j];
                }
                else if (i >= X && j >= Y && i < H && j < W) {
                    arrA[i][j] = arrB[i][j] - arrA[i - X][j - Y];
                }
            }
        }

        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                System.out.print(arrA[i][j] + " ");
            }
            System.out.println();
        }
    }


}
