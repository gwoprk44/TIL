#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, k;
    cin >> n >> k;

    queue<int> q;
    vector<int> result;

    for (int i = 1; i <= n; i++) {
        q.push(i);
    }

    while(!q.empty()) {
        for (int i = 0; i < k - 1; i++) {
            q.push(q.front());
            q.pop();
        }
        result.push_back(q.front());
        q.pop();
    }

    cout << "<";

    for (int i = 0; i < result.size(); i++) {
        cout << result[i];
        if (i != result.size() - 1) {
            cout << ", ";
        }
    }
    cout << ">";
}