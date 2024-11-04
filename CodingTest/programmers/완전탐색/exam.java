package programmers.완전탐색;

import java.util.*;

class Solution {
    public int[] solution(int[] answers) {
        // 첫번째, 두번째, 세번째 정답 찍는 패턴
        int[] one = {1,2,3,4,5};
        int[] second = {2,1,2,3,2,4,2,5};
        int[] third = {3,3,1,1,2,2,4,4,5,5};

        // 정답을 맞춘 횟수 저장할 배열
        int[] score = new int[3];
        
        for (int i = 0; i < answers.length; i++) {
            if (answers[i] == one[i % 5]) score[0]++;
            if (answers[i] == second[i % 8]) score[1]++;
            if (answers[i] == third[i % 10]) score[2]++;
        }
        // 가장 정답을 많이 맞춘 사람 찾기
        int maxScore = Math.max(score[0], Math.max(score[1], score[2]));

        // 가장 정답을 많이 맞춘 사람을 저장할 배열 선언
        List<Integer> list = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            if (maxScore == score[i]) list.add(i+1);
        }

        int answer[] = new int[list.size()]; 

        for (int i = 0; i < list.size(); i++) {
            answer[i] = list.get(i);
        }

        return answer;
    }
}