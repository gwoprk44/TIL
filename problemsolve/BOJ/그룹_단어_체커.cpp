#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    string s;
    int ans = 0;

    cin >> n;

    for (int i = 0; i < n; i++) {
        cin >> s;

        bool alpha[26] = {false, };
        bool isGroupWord = true;

        alpha[s[0] - 'a'] = true;

        for (int j = 1; j < s.size(); j++) {
            if (s[j] == s[j-1]) {
                continue;
            }
            else if (s[j] != s[j-1] && alpha[s[j] - 'a'] == true) {
                isGroupWord = false;
                break;
            }
            else {
                alpha[s[j] - 'a'] = true;
            }
        }
        
        if (isGroupWord) {
            ans++;
        }
    }
    cout << ans;
}
