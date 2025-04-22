#include <bits/stdc++.h>
using namespace std;

int main() {
    int N, M;
    cin >> N >> M;
    vector<string> board(N);
    for (int i = 0; i < N; ++i) {
        cin >> board[i];
    }

    int answer = 64; 

    for (int i = 0; i <= N - 8; ++i) {
        for (int j = 0; j <= M - 8; ++j) {
            int cnt1 = 0, cnt2 = 0;
            for (int x = 0; x < 8; ++x) {
                for (int y = 0; y < 8; ++y) {
                    // (x+y)가 짝수면 시작색, 홀수면 반대색
                    if ((x + y) % 2 == 0) {
                        if (board[i + x][j + y] != 'W') cnt1++; // W로 시작
                        if (board[i + x][j + y] != 'B') cnt2++; // B로 시작
                    } else {
                        if (board[i + x][j + y] != 'B') cnt1++;
                        if (board[i + x][j + y] != 'W') cnt2++;
                    }
                }
            }
            answer = min(answer, min(cnt1, cnt2));
        }
    }
    cout << answer;
    return 0;
}