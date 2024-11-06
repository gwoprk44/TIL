package BOJ.반복문;

import java.io.*;

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        for (int i = 0; i < N / 4; i++) {
            System.out.print("long ");
        }
        System.out.println("int");
    }

}
