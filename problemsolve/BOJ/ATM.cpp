#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    vector<int> p(n);
    for (int i = 0; i < n; i++) {
        cin >> p[i];
    }

    int cur_time = 0; //현재까지의 대기시간
    int tot_time = 0; //총 대기시간

    sort(p.begin(), p.end());

    for (int i = 0; i < n; i++) {
        cur_time += p[i];
        tot_time += cur_time;
    }

    cout << tot_time;

    return 0;
}