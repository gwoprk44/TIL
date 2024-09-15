package 문자열;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class P15312 {
    static int[] numOfStrokesArray = new int[]{3, 2, 1, 2, 3, 3, 2, 3, 3, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1, 2, 1, 1, 1, 2, 2, 1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String aName = br.readLine();
        String bName = br.readLine();
        int[] arrayOfStrkoes = new int[aName.length()+bName.length()];
        // 일자로 이름 번갈아가면서 세우기
        for (int i = 0; i < arrayOfStrkoes.length-1; i=i+2) {
            arrayOfStrkoes[i] = numOfStrokesArray[aName.charAt(i/2)-65];
            arrayOfStrkoes[i+1] = numOfStrokesArray[bName.charAt(i/2)-65];
        }
        
        int size = arrayOfStrkoes.length;
        while (size > 2) {
            for (int i = 0; i < size-1; i++) {
                arrayOfStrkoes[i] = (arrayOfStrkoes[i]+arrayOfStrkoes[i+1])%10;
            }
            arrayOfStrkoes[--size] = 0;
        }

        System.out.println(String.valueOf(arrayOfStrkoes[0])+String.valueOf(arrayOfStrkoes[1]));
    }
}