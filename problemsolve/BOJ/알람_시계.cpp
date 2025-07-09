#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int h, m;
    cin >> h >> m;

    if (m < 45) {
        h = h - 1;
        m = m + 60 - 45;
    } else {
        m = m - 45;
    }
    if (h < 0) {
        h = 23;
    }

    cout << h << " " << m;
}