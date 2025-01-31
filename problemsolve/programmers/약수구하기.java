package problemsolve.programmers;

import java.util.ArrayList;

class 약수구하기 {
    public int[] solution(int n) {
        
        ArrayList<Integer> divisor = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                divisor.add(i);
            }
        }

        int[] answer = new int[divisor.size()];

        for (int i = 0; i < divisor.size(); i++) {
            answer[i] = divisor.get(i);
        }

        return answer;
    }
}