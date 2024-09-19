package 문자열;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P3613 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String input = br.readLine();
        char[] arr = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean stop = false;

        if (input.contains("_")) { // C++인 경우
            for (int i = 0; i < arr.length; i++) {
                if (!(arr[i] >= 'a' && arr[i] <= 'z') && !(arr[i] == '_')) { //소문자 또는 _가 아닌 문자가 나오는 경우
                    System.out.println("Error!");
                    return;
                }
                if (arr[i] == '_') {
                    if (i == 0 || i == arr.length - 1 || stop) { // _ 가 첫 글자 / 마지막 글자 / 연속되어 나오는 경우
                        System.out.println("Error!");
                        return;
                    }
                    stop = true; // _ 등장 표시
                } else if (stop) {
                    char ch = (char) (arr[i] - ('a' - 'A')); //_ 바로 뒤 문자는 대문자로 변경
                    sb.append(ch);
                    stop = false;
                } else {
                    sb.append(arr[i]);
                }
            }

        } else { // Java인 경우
            for (int i = 0; i < arr.length; i++) {
                if (!(arr[i] >= 'a' && arr[i] <= 'z') && !(arr[i] >= 'A' && arr[i] <= 'Z')) {
                    System.out.println("Error!");
                    return;
                }
                if (arr[i] >= 'A' && arr[i] <= 'Z') { //대문자가 등장하는 경우
                    if (i == 0) { //첫 문자가 대문자인 경우
                        System.out.println("Error!");
                        return;
                    } else {
                        char ch = (char) (arr[i] + ('a' - 'A')); // _ 붙이고 소문자로 변경
                        sb.append('_').append(ch);
                    }
                } else {
                    sb.append(arr[i]);
                }
            }
        }
        System.out.println(sb.toString());
    }
}