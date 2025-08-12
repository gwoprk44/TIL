#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    long long b;
    cin >> n >> m >> b;

    int ground[501][501];

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            cin >> ground[i][j];
        }
    }

    int min_time = INT_MAX;
    int result_h = 0; 

    for (int h = 0; h <= 256; h++) {
        int block_add = 0;
        int block_remove = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int diff = ground[i][j] - h;

                if (diff > 0) {
                    block_remove += diff;
                } else {
                    block_add += -diff;
                }
            }
        }

        if (b + block_remove >= block_add) {
            int current_time = block_remove * 2 + block_add * 1;

            if (current_time <= min_time) {
                min_time = current_time;
                result_h = h;
            }
        }
    }
    
    cout << min_time << " " << result_h;

}