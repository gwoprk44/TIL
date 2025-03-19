#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    deque<int> DQ;
    int n, m;
    int ans = 0;

    cin >> n >> m;

    for (int i = 1; i <= n; i++) {
        DQ.push_back(i);
    }

    while (m--) {
        int t;
        cin >> t;
        int idx = find(DQ.begin(), DQ.end(), t) - DQ.begin();

        while (t != DQ.front()) {
            if (idx < DQ.size() - idx) {
                DQ.push_back(DQ.front());
                DQ.pop_front();
            }
            else {
                DQ.push_front(DQ.back());
                DQ.pop_back();
            }
            ans++;
        }
        DQ.pop_front();
    }
    cout << ans;
} 