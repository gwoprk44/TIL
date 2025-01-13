import java.io.*;
import java.util.StringTokenizer;

public class b_2798 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        int [] numberArray = new int[N];

        st = new StringTokenizer(br.readLine());
        br.close();

        for(int i = 0; i < N; i++) {
            numberArray[i] = Integer.parseInt(st.nextToken());
        }

        int sum = 0; //3수의 합을 구하는 변수
        int temp = 0; //근사치를 저장하여 저장하는 변수 (이전 for문의 3개의 합을 더해서 계속 비교해준다.

        for(int i = 0; i < N-2; i++) { //숫자가 겹치면 안되기 때문에 i는 0부터 시작하고 N-2까지
            for(int j = i+1; j < N-1; j++) { // 다음 j는 i+1 부터 시작하고 N-1까지
                for(int k = j+1; k < N; k++) { //다음 k는 j+1부터 시작 N까지 반복
                    sum = numberArray[i] + numberArray[j] + numberArray[k]; //3수를 합쳐서
                    if(sum > temp && sum <= M){ //이전 근사치보다 큰데 M보다 작거나 같다는 것은 이전 근사치보다 M에 가깝다는 뜻
                        temp = sum;
                    }
                }
            }
        }
        bw.write(temp + "\n");
        bw.flush();
        bw.close();

    }
}