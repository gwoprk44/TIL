#include <bits/stdc++.h>
using namespace std;

int N, M, V;
vector<int> adj[1001];
bool visited[1001];

void dfs(int current_node) {
    visited[current_node] = true;
    cout << current_node << " ";

    for (int next_node : adj[current_node]) {
        if(!visited[next_node]) {
            dfs(next_node);
        }
    }
}

void bfs(int start_node) {
    queue<int> q;

    q.push(start_node);
    visited[start_node] = true;

    while(!q.empty()) {
        int current_node = q.front();
        q.pop();

        cout << current_node << " ";

        for (int next_node : adj[current_node]) {
            if (!visited[next_node]) {
                q.push(next_node);
                visited[next_node] = true;
            }
        }
    }
}


int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N >> M >> V;

    for (int i = 0; i < M; i++) {
        int u, v;
        cin >> u >> v;
        
        adj[u].push_back(v);
        adj[v].push_back(u);
    }

    for (int i = 1; i <= N; i++) {
        sort(adj[i].begin(), adj[i].end());
    }

    dfs(V);
    cout << "\n";

    memset(visited, false, sizeof(visited));

    bfs(V);
    cout << "\n";

    return 0;
}