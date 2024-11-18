import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P2485 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        int[] tree = new int[N];

        // 입력값 받기
        for (int i = 0; i < N; i++) {
            tree[i] = Integer.parseInt(br.readLine());
        }

        // 각 가로수 사이의 거리 계산
        int[] distances = new int[N - 1];
        for (int i = 0; i < N - 1; i++) {
            distances[i] = tree[i + 1] - tree[i];
        }

        // 모든 거리의 최대 공약수 구하기
        int gcd = distances[0];
        for (int i = 1; i < distances.length; i++) {
            gcd = findGcd(gcd, distances[i]);
        }

        // 첫 번째 가로수와 마지막 가로수 사이의 전체 거리를 gcd로 나눈 값에서 현재 나무 개수를 뺀 값이 추가해야 할 나무의 수
        int totalTreesToPlant = (tree[N - 1] - tree[0]) / gcd + 1 - N;
        
        System.out.println(totalTreesToPlant);
    }

    // 유클리드 호제법을 사용한 GCD 계산
    public static int findGcd(int x, int y) {
        while (y != 0) {
            int temp = y;
            y = x % y;
            x = temp;
        }
        return x;
    }
}