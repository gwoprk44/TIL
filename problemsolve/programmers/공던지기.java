package problemsolve.programmers;

class 공던지기 {
    public int solution(int[] numbers, int k) {
        return numbers[2*(k-1) % numbers.length];
    }
}