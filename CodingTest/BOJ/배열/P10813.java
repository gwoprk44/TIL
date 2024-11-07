import java.util.Scanner;

public class P10813 {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int M = sc.nextInt();
		
		int[] arr = new int[N];
		
		// 바구니 번호와 배열에 들어있는 숫자 같음
		for(int i = 0 ; i < arr.length ; i++) {
			arr[i] = i+1;
		}
		
		// 공 바꾸는 횟수
		for(int j = 0 ; j < M ; j++) {
			
			// 바꿀 번호 입력
			int a = sc.nextInt();
			int b = sc.nextInt();
		
			int temp = arr[a-1];
			arr[a-1] = arr[b-1];
			arr[b-1] = temp;
			
			
		}
		
		// 출력
		for(int i = 0 ; i < arr.length ; i++) {
			System.out.print(arr[i] + " ");
		}
	}
}