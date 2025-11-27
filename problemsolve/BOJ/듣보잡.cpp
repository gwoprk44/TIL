#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    
    int n, m;
    cin >> n >> m;

    map<string, int> mp;
    vector<string> v;

    for (int i = 0; i < n+m; i++) {
        string name;
        cin >> name;
        mp[name]++;
        if (mp[name] > 1) {
            v.push_back(name);
        }
    }

    sort(v.begin(), v.end());
    cout << v.size() << "\n";

    for (auto a : v) {
        cout << a << "\n";
    }

    return 0;
}