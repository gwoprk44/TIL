#include <bits/stdc++.h>
using namespace std;

string s1, s2;
int ans, str[2][26];

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> s1 >> s2;

    for (auto c : s1) str[0][c-'a']++;
    for (auto c : s2) str[1][c-'a']++;

    for (int i = 0; i < 26; i++) {
        ans += abs(str[0][i]-str[1][i]);
    }
    cout << ans;
}