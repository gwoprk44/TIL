#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int x, n, a, b;
    int res = 0;

    cin >> x >> n;

    for (int i = 1; i <= n; i++) {
        cin >> a >> b;
        res += a * b;
    }

    if (res == x) {
        cout << "Yes";
    } else {
        cout << "No";
    }

}