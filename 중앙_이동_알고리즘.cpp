#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    int cnt = 2;

    for (int i = 0; i < n; i++) {
        cnt += (cnt-1);
    }
    cout << cnt * cnt;
}