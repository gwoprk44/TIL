package problemsolve.programmers;

import java.util.ArrayList;

class Solution {
    public int[] solution(int n) {

        ArrayList<Integer> arr = new ArrayList<>();

        for (int i = 2; i <= n; i++) {
            if (n % i == 0) {
                while (n % i == 0) {
                    n /= i;
                }
                arr.add(i);
            }
        }

        int[] answer = new int[arr.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = arr.get(i);
        }

        return answer;
    }
}