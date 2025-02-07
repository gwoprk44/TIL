package problemsolve.programmers;

import java.util.*;

class 잘라서배열로저장하기 {
    public String[] solution(String my_str, int n) {
        
        List<String> list = new ArrayList<>();
        
        for (int i = 0; i < my_str.length(); i+=n) {
            list.add(my_str.substring(i, Math.min(i + n, my_str.length())));
        }
        return list.toArray(new String[list.size()]);
    }
}