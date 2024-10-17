import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean solution(String[] phone_book) {
        Map<String, Integer> map = new HashMap<>();

        // 모든 phone 번호 hashMap에 담기
        for (int i = 0; i < phone_book.length; i++) {
            map.put(phone_book[i], i);
        }

        for (int i = 0; i < phone_book.length; i++) {
            // 전화번호의 길이만큼 순회
            for (int j = 0; j < phone_book[i].length(); j++) {
                // 해당 전화번호의 접두어가 key로 가진 경우
                if (map.containsKey(phone_book[i].substring(0,j))) {
                    return false;
                }
            }
        }

        return true;
    }
}
