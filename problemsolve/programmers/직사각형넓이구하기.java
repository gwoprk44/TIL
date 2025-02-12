package problemsolve.programmers;

class Solution {
    public int solution(int[][] dots) {
        int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;

        for (int[] dot : dots) {
            xMin = Math.min(xMin, dot[0]);
            xMax = Math.max(xMax, dot[0]);
            yMin = Math.min(yMin, dot[1]);
            yMax = Math.max(yMax, dot[1]);
        }

        int w = xMax - xMin;
        int h = yMax - yMin;

        return w * h;
    }
}