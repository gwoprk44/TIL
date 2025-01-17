package problemsolve.programmers;

import java.util.*;

class 배열자르기 {
    public int[] solution(int[] numbers, int num1, int num2) {
        return Arrays.copyOfRange(numbers, num1, num2 + 1);
    }
}