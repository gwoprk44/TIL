#include <bits/stdc++.h>
using namespace std;

int n, m;
vector<int> adj[101];
bool visited[101];
int count_infected = 0;

void dfs(int node) {
    visited[node] = true;
    count_infected++;

    for (int next_node : adj[node]) {
        if (!visited[next_node]) {
            dfs(next_node);
        }
    }
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

    dfs(1);

    cout << count_infected - 1;

    return 0;
}