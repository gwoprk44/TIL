#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    cin >> n >> m;

    unordered_map<string, string> sitePassWord;

    for (int i = 0; i < n; i++) {
        string site, password;
        cin >> site >> password;
        sitePassWord[site] = password;
    }

    for (int i = 0; i < m; i++) {
        string querySite;
        cin >> querySite;

        cout << sitePassWord[querySite] << "\n";
    }

    return 0;

}