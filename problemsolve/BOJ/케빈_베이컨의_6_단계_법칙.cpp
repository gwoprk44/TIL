#include <bits/stdc++.h>
using namespace std;

int n, m;
vector<int> adj[101];

int bfs(int start_node) {
    queue<int> q;
    vector<int> distance(n + 1, -1); // 거리 및 방문 여부 체크 (-1은 미방문)

    q.push(start_node);
    distance[start_node] = 0;

    int total_distance = 0;

    while (!q.empty()) {
        int current = q.front();
        q.pop();

        total_distance += distance[current];

        for (int neighbor : adj[current]) {
            if (distance[neighbor] == -1) { // 아직 방문하지 않은 이웃이라면
                distance[neighbor] = distance[current] + 1;
                q.push(neighbor);
            }
        }
    }
    return total_distance;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n >> m;

    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;

        adj[u].push_back(v);
        adj[v].push_back(u);
    }

    int min_kevin = 1e9;
    int ans = -1;

    for (int i = 1; i <= n; i++) {
        int cur_kevin = bfs(i);
        if (cur_kevin < min_kevin) {
            min_kevin = cur_kevin;
            ans = i;
        }
    }
    cout << ans;
}