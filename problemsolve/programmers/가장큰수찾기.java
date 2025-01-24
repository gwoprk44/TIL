package problemsolve.programmers;


class 가장큰수찾기 {
    public int[] solution(int[] array) {
        int max = array[0];
        int index = 0;

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }
        return new int[] {max, index};
    }
}