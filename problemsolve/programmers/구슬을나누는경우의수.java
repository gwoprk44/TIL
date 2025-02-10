package problemsolve.programmers;

class Solution {
    public int solution(int balls, int share) {
        return factorial(balls, share);
    }

    public static int factorial(int balls, int share) {
        if (balls == share || share == 0) return 1;
        return factorial(balls - 1, share - 1) + factorial(balls - 1, share);
    }
}