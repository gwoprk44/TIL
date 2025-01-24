package problemsolve.programmers;

import java.util.Arrays;

class 문자열정리하기 {
    public int[] solution(String my_string) {

        String[] arr = my_string.replaceAll("[a-z]", "").split("");

        Arrays.sort(arr);
        
        int[] answer = new int[arr.length];

        for (int i = 0; i < answer.length; i++) {
            answer[i] = Integer.parseInt(arr[i]);
        }

        return answer;

    }
}