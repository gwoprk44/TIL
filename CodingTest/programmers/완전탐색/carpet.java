package programmers.완전탐색;

class Solution {
    public int[] solution(int brown, int yellow) {
        int[] answer = new int[2]; 
        int carpet = brown + yellow;

        // 노란색 카펫이 존재하려면 가로, 세로가 3 이상이여야 함
        for (int i = 3; i <= carpet; i++) {
            int row = i;
            int col = carpet / row;

            if (row < 3) continue;

            if (row >= col) {
                if ((row - 2)*(col - 2) == yellow) {
                    answer[0] = row;
                    answer[1] = col;
                } 
            }
        }
        return answer;
    }
}