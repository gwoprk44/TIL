#include <bits/stdc++.h>
using namespace std;

int n;
vector<vector<int>> graph;
vector<vector<int>> result;

void bfs(int startnode) {
    queue<int> q;
    vector<bool> visited(n, false);
    q.push(startnode);

    while(!q.empty()) {
        int cur = q.front();
        q.pop();

        for (int next = 0; next < n; next++) {
            if (graph[cur][next] == 1 && !visited[next]) {
                visited[next] = true;
                result[startnode][next] = 1;
                q.push(next);
            }
        }
    }
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n;

    graph.assign(n, vector<int>(n, 0));
    result.assign(n, vector<int>(n, 0));

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cin >> graph[i][j];
        }
    }

    for (int i = 0; i < n; i++) {
        bfs(i);
    }

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cout << result[i][j] << " ";
        }
        cout << "\n";
    }
}