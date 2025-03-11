#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    using namespace std;

    int n;

    cin >> n;

    for (int i = 0; i < n; i++) {
        list<char> l = {};
        auto cursor = l.begin();
        string s;
        
        cin >> s;

        for (auto c : s) {
            if (c == '<') {
                if (cursor != l.begin()) cursor--;
            }
            else if (c == '>') {
                if (cursor != l.end()) cursor++;
            }
            else if (c == '-') {
                if (cursor != l.begin()) {
                    cursor--;
                    cursor = l.erase(cursor);
                }
            }
            else {
                l.insert(cursor, c);
            }
        }
        for (auto c : l) cout << c;
        cout << '\n';
    }
}