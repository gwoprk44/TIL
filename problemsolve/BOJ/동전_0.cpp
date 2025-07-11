#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, k;
    cin >> n >> k;

    vector<int> coins(n);

    int ans = 0;

    for (int i = 0; i < n; i++) {
        cin >> coins[i];
    }

    for (int i = n - 1; i >= 0; i--) {
        if (coins[i] <= k) {
            ans += k / coins[i];
            k %= coins[i];
        }
        if (k == 0) break;
    }

    cout << ans << "\n";
}