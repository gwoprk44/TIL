#include <iostream>
#include <vector>
#include <queue>
#include <string>

using namespace std;

int n, m;

vector<string> maze;
int dist[101][101];

int dx[] = {-1, 1, 0, 0}; // 상하좌우 행 이동
int dy[] = {0, 0, -1, 1}; // 상하좌우 열 이동

void bfs(int start_x, int start_y) {
    queue<pair<int, int>> q;
    
    q.push({start_x, start_y});
    dist[start_x][start_y] = 1;

    while (!q.empty()) {
        int cur_x = q.front().first;
        int cur_y = q.front().second;
        q.pop();

        for (int i = 0; i < 4; i++) {
            int next_x = cur_x + dx[i];
            int next_y = cur_y + dy[i];

            // 미로 범위 체크
            if (next_x < 0 || next_x >= n || next_y < 0 || next_y >= m) {
                continue;
            }

            if (maze[next_x][next_y] == '0' || dist[next_x][next_y] > 0) {
                continue;
            }

            q.push({next_x, next_y});
            dist[next_x][next_y] = dist[cur_x][cur_y] + 1;
        }
    }
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n >> m;

    maze.resize(n);
    for (int i = 0; i < n; i++) {
        cin >> maze[i];
    }

    bfs(0, 0);

    cout << dist[n - 1][m - 1] << "\n";

    return 0;
}