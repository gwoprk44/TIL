#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int k, n;
    cin >> k >> n;

    vector<int> cables(k);
    long long high = 0;

    for (int i = 0; i < k; i++) {
        cin >> cables[i];
        if (cables[i] > high) {
            high = cables[i];
        }
    }

    long long low = 1;
    long long ans = 0;

    while (low <= high) {
        long long count = 0;
        long long mid = (low + high) / 2;

        for (int i = 0; i < k; i++) {
            count += cables[i] / mid;
        }

        if (count >= n) {
            ans = mid;
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }

    cout << ans << "\n";

    return 0;
}