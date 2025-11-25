#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n;
    cin >> n;

    map<string, bool> mp;
    string name, log;

    for (int i = 0; i < n; i++) {
        cin >> name >> log;
        if (log == "enter") {
            mp[name] = true;
        } else { 
            mp.erase(name);
        }
    }

    for (auto it = mp.rbegin(); it != mp.rend(); ++it) {
        cout << it->first << '\n';
    }

    return 0;
}