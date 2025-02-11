package problemsolve.programmers;

class 삼각형의완성조건 {
    public int solution(int[] sides) {
       int bigValue = Math.max(sides[0], sides[1]);
       int minValue = Math.min(sides[0], sides[1]);

       int lowLimit = bigValue - minValue;
       int highLimit = bigValue + minValue;

       return highLimit - lowLimit - 1;

    }
}