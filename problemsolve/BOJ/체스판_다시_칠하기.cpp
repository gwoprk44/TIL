#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    cin >> n >> m;
    
    vector<string> board(n);

    for (int i = 0; i < n; i++) {
        cin >> board[i];
    }

    int ans = 64;

    for (int i = 0; i <= n - 8; i++) {
        for (int j = 0; j <= m - 8; j++) {
            // cnt1 w시작 cnt2 b시작
            int cnt1 = 0, cnt2 = 0;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if ((x+y) % 2 == 0) {
                        if (board[i+x][j+y] != 'W') cnt1++;
                        if (board[i+x][j+y] != 'B') cnt2++;
                    } else {
                        if (board[i+x][j+y] != 'B') cnt1++;
                        if (board[i+x][j+y] != 'W') cnt2++;
                    }
                }
            }
            ans = min(ans, min(cnt1, cnt2));
        }
    }
    cout << ans;
}