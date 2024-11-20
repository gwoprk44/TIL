import java.io.*;

public class P17103{
    static boolean[] isPrime = new boolean[1000001]; // 소수를 저장할 배열

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        

        // 에라토스테네스의 체로 소수 구하기
        findPrimes();

        int T = Integer.parseInt(br.readLine()); // 테스트 케이스 개수 입력

        for (int i = 0; i < T; i++) {
            int N = Integer.parseInt(br.readLine()); // 짝수 N 입력
            int count = 0;

            // 골드바흐 파티션 계산
            for (int j = 2; j <= N / 2; j++) {
                if (!isPrime[j] && !isPrime[N - j]) { // j와 N - j가 모두 소수일 때
                    count++;
                }
            }

            System.out.println(count);
        }
    }

    // 에라토스테네스의 체로 소수를 구하는 함수
    static void findPrimes() {
        // 배열 초기화: true는 소수가 아님을 의미함 (false가 소수)
        isPrime[0] = isPrime[1] = true; // 0과 1은 소수가 아님

        for (int i = 2; i * i < isPrime.length; i++) {
            if (!isPrime[i]) { // i가 소수일 때
                for (int j = i * i; j < isPrime.length; j += i) {
                    isPrime[j] = true; // i의 배수는 소수가 아님
                }
            }
        }
    }
}