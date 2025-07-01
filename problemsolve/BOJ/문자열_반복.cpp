#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int t, r;
    cin >> t;

    string s;
    for (int i = 0; i < t; i++) {
        cin >> r >> s;

        for (char c : s) {
            for (int i = 0; i < r; i++) {
                cout << c;
            }
        }
        cout << "\n";
    }
}