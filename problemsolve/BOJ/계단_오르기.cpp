#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    vector<int> dp(n+1, 0);
    vector<int> score(n+1, 0);

    for (int i = 1; i <= n; i++) {
        cin >> score[i];
    }

    dp[1] = score[1];

    if (n >= 2) {
        dp[2] = score[1] + score[2];
    }

    for (int i = 3; i <= n; i++) {
        int case1 = dp[i-2] + score[i];
        int case2 = dp[i-3] + score[i-1] + score[i];

        dp[i] = max(case1, case2);
    }

    cout << dp[n];

    return 0;
}