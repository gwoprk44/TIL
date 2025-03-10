#include <bits/stdc++.h>
using namespace std;

int ans, n, k, stu[2][7] = {};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n >> k;

    for (int i = 0; i < n; i++) {
        int s, y;
        cin >> s >> y;
        stu[s][y]++;
    }

    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 7; j++) {
            ans+=stu[i][j]/k;
            if (stu[i][j]%k) ans++;
        }
    }
    cout << ans;
}