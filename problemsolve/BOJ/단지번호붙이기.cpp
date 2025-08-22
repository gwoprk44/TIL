#include <bits/stdc++.h>
using namespace std;

int n;
vector<string> apt_map;
vector<vector<bool>> visited;

int dx[] = {0, 0, -1, 1};
int dy[] = {-1, 1, 0, 0};

int house_count = 0;

void dfs(int x, int y) {
    visited[x][y] = true;
    house_count++;

    for (int i = 0; i < 4; i++) {
        int nx = x + dx[i];
        int ny = y + dy[i];

        if (nx < 0 || nx >= n || ny < 0 || ny >= n) {
            continue;
        }

        if (apt_map[nx][ny] == '1' && !visited[nx][ny]) {
            dfs(nx, ny);
        }
    }
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n;
    apt_map.resize(n);
    visited.resize(n, vector<bool>(n, false));

    for (int i = 0; i < n; i++) {
        cin >> apt_map[i];
    }

    vector<int> complex_size;

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (apt_map[i][j] == '1' && !visited[i][j]) {
                house_count = 0;
                dfs(i, j);
                complex_size.push_back(house_count);
            }
        }
    }

    cout << complex_size.size() << "\n";
    sort(complex_size.begin(), complex_size.end());
    for (int size : complex_size) {
        cout << size << "\n";
    }

    return 0; 
}