#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    map<int, int> mp;

    int n; 
    cin >> n;

    for (int i = 0; i < n; i++) {
        int x;
        cin >> x;
        mp[x] = 1;
    }

    int m;
    cin >> m;

    for (int i = 0; i < m; i++) {
        int q;
        cin >> q;
        if (mp.find(q) != mp.end()) cout << 1;
        else cout << 0;

        if (i != m - 1) cout << " ";
    }


    return 0;
}