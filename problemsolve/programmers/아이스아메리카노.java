package problemsolve.programmers;

class 아이스아메리카노 {
    public int[] solution(int money) {
        int[] answer = new int[2];
        
        answer[0] = money / 5500;
        answer[1] = money % 5500;

        
        return answer;
    }
}