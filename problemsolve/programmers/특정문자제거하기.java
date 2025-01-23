package problemsolve.programmers;

class 특정문자제거하기 {
    public String solution(String my_string, String letter) {
        String answer = "";
 
        answer = my_string.replace(letter, "");

        return answer;
    }
}