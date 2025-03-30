#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int paper[100][100] = {0};
    int n, x, y;
    int ans = 0;

    cin >> n;

    for (int i = 0; i < n; i++) {
        cin >> x >> y;
        for (int j = x; j < x + 10; j++) {
            for (int k = y; k < y + 10; k++) {
                if (paper[j][k] == 0) {
                    paper[j][k] = 1;
                    ans++;
                }
            }
        }
    }
    cout << ans;
}