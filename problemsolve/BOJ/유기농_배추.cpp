#include <bits/stdc++.h> 
using namespace std;

int n, m, k;
int field[51][51];
bool visited[51][51];

int dx[] = {0, 0, -1, 1};
int dy[] = {-1, 1, 0, 0};

void dfs(int y, int x) {
    visited[y][x] = true;

    for (int i = 0; i < 4; i++) {
        int nx = x + dx[i];
        int ny = y + dy[i];

        if (nx < 0 || ny < 0 || nx >= m || ny >= n) {
            continue;
        }

        if (field[ny][nx] == 1 && !visited[ny][nx]) { 
            dfs(ny, nx);
        }
    }
}

void solve() {
    
    memset(field, 0, sizeof(field));
    memset(visited, false, sizeof(visited)); 

    cin >> m >> n >> k;

    for (int i = 0; i < k; i++) {
        int x, y;
        cin >> x >> y;
        field[y][x] = 1;
    }

    int worm_count = 0;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (field[i][j] == 1 && !visited[i][j]) {
                worm_count++;
                dfs(i, j);
            }
        }
    }
 
    cout << worm_count << "\n";
}


int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int t;
   
    cin >> t; 

    while (t--) {
        solve();
    }

    return 0; 
}