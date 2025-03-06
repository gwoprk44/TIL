#include <bits/stdc++.h>
using namespace std;

    int a[1000001] = {};
    bool occur[2000001];
    int n, x;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n;

    for(int i = 0; i < n; i++) {
        cin >> a[i];
    }
    cin >> x;

    int ans = 0;

    for (int i = 0; i < n; i++) {
        if (x-a[i] > 0 && occur[x-a[i]]) {
            ans++;
        }
        occur[a[i]] = true;
    }

    cout << ans;

}