import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P4948 {

    static boolean[] primeNum = new boolean[246913]; // 배열 크기는 최대 2 * 123456 + 1
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        getPrime(); // 소수 미리 계산

        while (true) {
            int N = Integer.parseInt(br.readLine());

            if (N == 0) break; // 입력이 0이면 종료

            int count = 0;

            // N+1부터 2N까지의 소수 개수 세기
            for (int i = N + 1; i <= 2 * N; i++) {
                if (primeNum[i]) count++;
            }

            System.out.println(count); // 결과 출력
        }
    }

    static void getPrime() {
        // 모든 수를 소수로 가정하고 시작
        for (int i = 2; i < primeNum.length; i++) {
            primeNum[i] = true;
        }

        // 에라토스테네스의 체 알고리즘
        for (int i = 2; i <= Math.sqrt(primeNum.length); i++) {
            if (primeNum[i]) {
                for (int j = i * i; j < primeNum.length; j += i) { // i의 배수를 false로 설정
                    primeNum[j] = false;
                }
            }
        }
    }
}