#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n, m;
    cin >> n >> m;

    map<string,bool> mp;
    string s;

    for (int i = 0; i < n; i++) {
        cin >> s;
        mp[s] = true;
    }

    int ans = 0;
    for (int i = 0; i < m; i++) {
        cin >> s;
        if (mp.find(s) != mp.end()) ans++;
    }

    cout << ans;

    return 0;
}