package problemsolve.programmers;

class 배열의평균값 {
    public double solution(int[] numbers) {
        double answer = 0;
        
        int total = 0;

        for (int i = 0; i < numbers.length; i++) {
            total += numbers[i];
        }
        
        answer = (double) total / numbers.length;
        return answer;      
    }
}