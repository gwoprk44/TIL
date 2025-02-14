package problemsolve.programmers;

class 등수매기기 {
    public int[] solution(int[][] score) {
        float[] avg = new float[score.length];

        // 평균 점수 계산
        for (int i = 0; i < score.length; i++) {
            avg[i] = (float)((score[i][0] + score[i][1]) / 2.0);
        }

        int[] answer = new int[score.length];

        // 순위 계산
        for (int j = 0; j < score.length; j++) {
            int order = 1; // 순위는 1부터 시작
            for (int k = 0; k < score.length; k++) {
                if (avg[j] < avg[k]) order++; // 자신보다 높은 평균이 있을 경우 순위 증가
            }
            answer[j] = order;
        }

        return answer;
    }
}