import java.util.*;

class Solution {
    public int solution(int bridge_length, int weight, int[] truck_weights) {
        int answer = 0; // 걸린 총 시간
        int sum = 0;    // 트럭 무게의 합

        Queue<Integer> q = new LinkedList<>();
        
        for (int i = 0; i < truck_weights.length; i++) {
            int truck = truck_weights[i];

            while (true) {
                /* 큐가 비어있을 경우 큐에 트럭 삽입
                 * 트럭 무게의 합에 트럭의 무게를 더하고
                 * 걸린 총 시간 증가
                */
                if (q.isEmpty()) {
                    q.add(truck);
                    sum += truck;
                    answer++;
                    break;
                /* 큐가 꽉 찼을 경우 (다리에 트럭이 꽉 찬 경우)
                 * 다리에서 트럭을 내림 (q.poll)
                 * 그 후 트럭 무게의 합에서 내린 트럭의 무게만큼 감소
                 */ 
                } else if (q.size() == bridge_length) {
                    sum -= q.poll();
                } else {
                    if (sum + truck <= weight) {
                        q.add(truck);
                        sum += truck;
                        answer++;
                        break;
                    } else {
                        q.add(0);
                        answer++;
                    }
                }
            }
        }

        return answer + bridge_length;

    }
}
