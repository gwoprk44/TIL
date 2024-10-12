package BOJ.문자열;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

// 놀라운 문자열 문제
// 문자열 처리
// ==> HashSet을 사용해서 중복 체크하기 -> 중복된 문자열은 하나만 가지므로 문자 하나씩 담을 때 마다 counting해서 hashSet의 size와 count 비교하면 됨.
public class P1972 {
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String inputStr;	// 입력받는 줄
		StringBuilder sb = new StringBuilder();
		while(!(inputStr = br.readLine()).equals("*")) {
			ArrayList<String> list = new ArrayList<String>();	// 각 쌍 별 나올 수 있는 문자를 담는 ArrayList
			int len = inputStr.length();	// 입력받은 문자열의 길이
			boolean isSurprise = true;	// 놀라운 문자열인지 아닌지 판별하는 boolean
			
			for(int i = 1; i < len; i++) {	// 0 ~ n - 2쌍 을 나타내는 값
				list.clear(); // 초기화해야지 다음 쌍 진행 가능
				for(int j = 0; j < len - i; j++) {	// 시작 인덱스
					String str = Character.toString(inputStr.charAt(j)) + Character.toString(inputStr.charAt(j + i));
//					String str = inputStr.charAt(j) + "" + inputStr.charAt(j + i);
					list.add(str);
				}
				// 같은 문자가 있는지 확인하기 위해 정렬 후 해당하는 인덱스의 문자와 바로 다음 인덱스의 문자를 비교한다
				Collections.sort(list);	
				for(int k = 0; k < list.size() - 1; k++) {
					if(list.get(k).equals(list.get(k + 1))) {
						isSurprise = false;
						break;
					}
				}
				if(!isSurprise) 
					break;	
			}
			
			if(isSurprise) {
				sb.append(inputStr).append(" is surprising.");
			}
			else {
				sb.append(inputStr).append(" is NOT surprising.");
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}
}