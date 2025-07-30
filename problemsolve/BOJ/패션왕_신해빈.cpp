#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int t;
    cin >> t;

    while (t--) {
        int n;
        cin >> n;

        map<string, int> clothes;

        for (int i = 0; i < n; i++) {
            string s1, s2;
            cin >> s1 >> s2;
            clothes[s2]++;
        }
        int ans = 1;

        for (auto c : clothes) {
            ans *= c.second + 1;
        }

        cout << ans - 1 << "\n";
    }
}