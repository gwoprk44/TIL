package problemsolve.programmers;


class 문자열뒤집기 {
    public String solution(String my_string) {
        String answer = "";
        StringBuilder sb = new StringBuilder(my_string);

        answer = sb.reverse().toString();

        return answer;
    }
}