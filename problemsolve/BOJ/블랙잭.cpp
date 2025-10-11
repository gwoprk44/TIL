#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    cin >> n >> m;

    int card[100];

    for (int i = 0; i < n; i++) {
        cin >> card[i];
    }

    int ans = 0;
    
    for (int i = 0; i < n - 2; i ++) {
        for (int j = i + 1; j < n - 1; j++) {
            for (int k = j + 1; k < n; k++) {
                int sum = card[i] + card[j] + card[k];
                if (sum > ans && sum <= m) {
                    ans = sum;
                }
            }
        }
    }

    cout << ans;
}