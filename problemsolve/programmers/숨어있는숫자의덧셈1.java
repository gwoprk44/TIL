package problemsolve.programmers;


class 숨어있는숫자의덧셈 {
    public int solution(String my_string) {
        int answer = 0;
        
        my_string = my_string.replaceAll("[a-zA-Z]", "");
        String[] string = my_string.split("");
        
        for(String i : string) {
            answer += Integer.parseInt(i);
        }
        
        return answer;
    }
}