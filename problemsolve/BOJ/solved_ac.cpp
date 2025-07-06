#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    if (n == 0) {
        cout << "0\n";
        return 0;
    }

    vector<int> opinions(n);

    for (int i = 0; i < n; i++) {
        cin >> opinions[i];
    }

    sort(opinions.begin(), opinions.end());

    int trim_count = round(n * 0.15);

    double sum = 0;

    for (int i = trim_count; i < n - trim_count; i++) {
        sum += opinions[i];
    }

    int avg_count = n - 2 * trim_count;

    int ans = round(sum / avg_count);

    cout << ans;

    return 0;
    
}