#include <bits/stdc++.h>
using namespace std;
    
    int n, m;
    int world[1001][1001];
    int dist[1001][1001];

    int dr[] = {-1, 1, 0, 0};
    int dc[] = {0, 0, -1, 1};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n >> m;
    int start_r, start_c;

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            cin >> world[i][j];
            dist[i][j] = -1;

            if (world[i][j] == 2) {
                start_c = i;
                start_r = j;
            }
        }
    }

    queue<pair<int, int>> q;
    
    q.push({start_r, start_c});
    dist[start_r][start_c] = 0;

    while (!q.empty()) {
        int r = q.front().first;
        int c = q.front().second;
        q.pop();

        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];

            if (nr < 0 || nr >= n || nc < 0 || nc >= m) {
                continue;
            }

            if (world[nr][nc] == 1 && dist[nr][nc] == -1) {
                dist[nr][nc] = dist[r][c] + 1;
                q.push({nr, nc});
            }
        }
    }

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (world[i][j] == 0) {
                cout << 0 << " ";
            }
            else {
            cout << dist[i][j] << " ";
            }
        }
        cout << "\n";
    }


}