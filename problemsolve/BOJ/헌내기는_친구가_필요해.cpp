#include <bits/stdc++.h>
using namespace std;

int N, M;
vector<string> campus;
vector<vector<bool>> visited;
int person_count = 0;

// 상, 하, 좌, 우
int dx[] = {-1, 1, 0, 0};
int dy[] = {0, 0, -1, 1};

void dfs(int x, int y) {
    visited[x][y] = true;

    if (campus[x][y] == 'P') {
        person_count++;
    }

    for (int i = 0; i < 4; ++i) {
        int nx = x + dx[i];
        int ny = y + dy[i];

        if (nx < 0 || nx >= N || ny < 0 || ny >= M) continue;
        if (campus[nx][ny] != 'X' && !visited[nx][ny]) {
            dfs(nx, ny);
        }
    }
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    cin >> N >> M;
    campus.resize(N);
    visited.resize(N, vector<bool>(M, false));
    int startX = -1, startY = -1;

    for (int i = 0; i < N; ++i) {
        cin >> campus[i];
        for (int j = 0; j < M; ++j) {
            if (campus[i][j] == 'I') {
                startX = i;
                startY = j;
            }
        }
    }

    dfs(startX, startY);

    if (person_count == 0) {
        cout << "TT" << endl;
    } else {
        cout << person_count << endl;
    }

    return 0;
}