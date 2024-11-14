package CodingTest.BOJ.브루트포스;

import java.io.*;
import java.util.StringTokenizer;

public class P1018 {

    public static int getAnswer(int startRow, int startCol, String[] board) {
        String[] orgBoard = {"WBWBWBWB", "BWBWBWBW"};
        int whiteAns = 0;
        for (int i = 0; i < 8; i++) {
            int row = startRow + i;
            for (int j = 0; j < 8; j++) {
                int col = startCol + j;
                if (board[row].charAt(col) != orgBoard[row % 2].charAt(j)) {
                    whiteAns++;
                }
            }
        } 
        return Math.min(whiteAns, 64 - whiteAns);
    }
    public static void main(String[] args) throws IOException {
        // 1. 입력값 받기
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        int row = Integer.parseInt(st.nextToken());
        int col = Integer.parseInt(st.nextToken());

        String[] board = new String[row];
        for (int i = 0; i < row; i++) {
            board[i] = br.readLine();
        }

        // 2. 체스판 자르기
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i <= row - 8; i++) {
            for (int j = 0; j <= col - 8; j++) {
                // 3. 체스판 최소비용 구하기
                int curAns = getAnswer(i, j, board);
                // 4. 최소비용 갱신
                if (ans > curAns) {
                    ans = curAns;
                }
            }
        }

        System.out.println(ans);
    }
}
