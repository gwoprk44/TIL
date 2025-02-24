#include <bits/stdc++.h>
using namespace std;

int main (void) {
    ios::sync_with_stdio(0);
    cin.tie(0);

    unordered_set<string> m;
    string s;
    cin >> s;

    int i,j,l = s.length();

    for (int i = 0; i < l; i++) {
        for (j = i; j < l; j++) {
            m.insert(s.substr(i, j-i+1));
        }
    }
    cout << m.size();
}