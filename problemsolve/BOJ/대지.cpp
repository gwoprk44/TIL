#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, x, y;
    cin >> n;

    int min_x = 100001, max_x = -100001;
    int min_y = 100001, max_y = -100001;

    for (int i = 0; i < n; i++) {
        cin >> x >> y;

        min_x = min(min_x, x);
        max_x = max(max_x, x);
        min_y = min(min_y, y);
        max_y = max(max_y, y);
    }

    cout << (max_x - min_x) * (max_y - min_y);
}