package problemsolve.programmers;

import java.util.Arrays;

class 최댓값만들기2 {
    public int solution(int[] numbers) {

        Arrays.sort(numbers);
        return Math.max(numbers[numbers.length-1] * numbers[numbers.length-2], numbers[0] * numbers[1]);
    }
}