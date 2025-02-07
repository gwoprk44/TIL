package problemsolve.programmers;

import java.util.HashMap;

class 최빈값구하기 {
    public int solution(int[] array) {
        int answer = 0;
        int maxcnt = 0;

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int number : array) {
            int cnt = map.getOrDefault(number, 0) + 1;
            if (cnt > maxcnt) {
                maxcnt = cnt;
                answer = number;
            } else if (cnt == maxcnt) {
                answer = -1;
            }
            map.put(number, cnt);
        }


        return answer;
    }
}