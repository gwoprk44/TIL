package programmers.힙;

import java.util.*;

class Solution {
    public int solution(int[] scoville, int K) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int answer = 0;
        
        for (int i = 0; i < scoville.length; i++) {
            pq.add(scoville[i]);
        }
        
        while (pq.peek() < K) {
            if (pq.size() == 1) {
                return -1;
            } else {
                pq.add(pq.poll() + pq.poll() * 2);
                answer++;
            }
        }
        
        return answer;
    }
}