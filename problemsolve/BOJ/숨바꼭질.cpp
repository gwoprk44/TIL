#include <bits/stdc++.h>
using namespace std;

#define MAX 100001 

using namespace std;

int N, K;
int time_arr[MAX]; 

void bfs() {
    queue<int> q;

    q.push(N);
    time_arr[N] = 0;

    while (!q.empty()) {
        int current_pos = q.front();
        q.pop();

        
        if (current_pos == K) {
            cout << time_arr[K] << endl;
            return;
        }

    
        int next_positions[3] = {current_pos - 1, current_pos + 1, current_pos * 2};

        for (int next_pos : next_positions) {
           
            if (next_pos >= 0 && next_pos < MAX && time_arr[next_pos] == -1) {
                time_arr[next_pos] = time_arr[current_pos] + 1; 
                q.push(next_pos); 
        }
    }
}
}


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    cin >> N >> K;
    
    
    if (N >= K) {
        cout << N - K << endl;
        return 0;
    }

    
    fill(time_arr, time_arr + MAX, -1);

    bfs();

    return 0;
}