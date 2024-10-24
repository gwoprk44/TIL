import java.util.*;

class Solution {
    public int[] solution(int []arr) {
        int[] answer;
        
        // 스택 선언
        Stack<Integer> st = new Stack<>();
        
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                st.push(arr[i]);
            } else if (st.peek() != arr[i]) {
                st.push(arr[i]);
            }
        }

        answer = new int[st.size()];

        for (int i = st.size() - 1; i >= 0; i--) {
            answer[i] = st.pop();
        }
        
        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("Hello Java");

        return answer;
    }
}