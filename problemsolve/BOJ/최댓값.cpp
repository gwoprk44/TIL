#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, x, y;
    int max = -1;

    for (int i = 1; i <= 9; i++) {
        for (int j = 1; j <= 9; j++) {
            cin >> n;

            if (max <= n) {
                max = n;
                x = i;
                y = j;
            }
        }
    }

    cout << max << "\n";
    cout << x << " " << y;
}