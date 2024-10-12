package BOJ.문자열;
import java.io.*;

public class P12871 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String s = br.readLine();
		String t = br.readLine();

		int num1 = s.length();
		int num2 = t.length();
		int result = 0;

		// 최소 공배수 구하기
		int min = (num1 * num2) / gcd(Math.max(num1, num2), Math.min(num1, num2));

		// 최소 공배수의 길이가 되도록 s 더하기
		String tmp = s;
		while (true) {
			if (s.length() == min)
				break;

			s += tmp;
		}

		// 최소 공배수의 길이가 되도록 t 더하기
		tmp = t;
		while (true) {
			if (t.length() == min)
				break;
			t += tmp;
		}
		
		if(s.equals(t)) result =1;
		System.out.println(result);
	}
	
	static int gcd(int max, int min) {	
		while(min!=0) {
			int remainder = max%min;
			max =min;
			min = remainder;
		}
		
		return max;
	}

}