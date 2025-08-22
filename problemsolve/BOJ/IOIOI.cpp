#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int N, M;
    string S;

    cin >> N >> M >> S;

    int result = 0;
    int ioi_count = 0; // 'IOI' 패턴이 연속으로 나온 횟수

    for (int i = 0; i < M - 2; ++i) {
        if (S[i] == 'I' && S[i+1] == 'O' && S[i+2] == 'I') {
            ioi_count++;

            if (ioi_count >= N) {
                result++;
            }

            i++; 
        } else {
            ioi_count = 0;
        }
    }

    cout << result << "\n";

    return 0;
}