package problemsolve.programmers;

import java.util.Arrays;

class 문자열정렬2 {
    public String solution(String my_string) {
        String answer = "";
        String[] abc = my_string.toLowerCase().split("");

        Arrays.sort(abc);

        for (int i = 0; i < abc.length; i++) {
            answer += abc[i];
        }

        return answer;
    }
}