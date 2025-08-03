#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m, k;

    vector<long long> dp(n+1, 0);

    cin >> n >> m;

    for (int i = 1; i <= n; i++) {
        cin >> k;
        dp[i] = dp[i-1] + k;
    }

    while(m--) {
        int i, j;
        cin >> i >> j;

        cout << dp[j] - dp[i-1] << "\n";
    }


}