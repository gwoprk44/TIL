package problemsolve.programmers;

class 모음제거하기 {
    public String solution(String my_string) {
       return my_string.replaceAll("[aeiou]", "");
    }
}