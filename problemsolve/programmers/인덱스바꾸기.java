package problemsolve.programmers;


class Solution {
    public String solution(String my_string, int num1, int num2) {
        String[] string = my_string.split("");

        String temp = string[num1];
        string[num1] = string[num2];
        string[num2] = temp;
        
        return String.join("", string);

    }
}