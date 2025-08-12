#include <bits/stdc++.h>
using namespace std;

vector<int> graph[1001];
bool visited[1001];

void dfs(int node) {
    visited[node] = true;

    for (int i = 0; i < graph[node].size(); i++) {
        int next_node = graph[node][i];
        if (!visited[next_node]) {
            dfs(next_node);
        }
    }
}


int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    cin >> n >> m;

    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;

        graph[u].push_back(v);
        graph[v].push_back(u);
    }

    int ans = 0;

    for (int i = 1; i <= n; i++) {
        if (!visited[i]) {
            dfs(i);
            ans++;
        }
    }

    cout << ans;

    return 0;
}