#include <iostream>
#include <queue>
#include <vector>

using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int t;
    cin >> t;

    while (t--) {
        int n, m;
        cin >> n >> m;

        queue<pair<int, int>> q; // {인덱스, 중요도}
        priority_queue<int> pq;   // 중요도만 저장

        for (int i = 0; i < n; i++) {
            int priority;
            cin >> priority;

            q.push({i, priority});
            pq.push(priority);
        }

        int count = 0;

        while (!q.empty()) { 
            int current_index = q.front().first;
            int current_priority = q.front().second;
            q.pop();

            // 현재 문서의 중요도가 가장 높은지 확인
            if (current_priority == pq.top()) {
                count++;
                pq.pop(); 

                if (current_index == m) {
                    cout << count << "\n";
                    break;
                }
            } else {
                // 중요도가 더 높은 문서가 있다면 다시 큐의 뒤로 보냄
                q.push({current_index, current_priority});
            }
        }
    }
    return 0;
}