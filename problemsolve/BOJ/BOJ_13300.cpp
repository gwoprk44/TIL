#include <bits/stdc++.h>
using namespace std;

int N, K, ans = 0;
int stu[2][7] = {}; //성별, 학년별 학생 수

int main(void) {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N >> K;
    
    for (int i = 0; i < N; i++) {
       int s, y;
       cin >> s >> y;
       stu[s][y]++;
    }

    for (int i = 0; i < 2; i++) {
        for (int j = 1; j < 7; j++) {
            ans += stu[i][j] / K;
            if (stu[i][j] % K) ans++;  
        }
    }
    cout << ans;
}