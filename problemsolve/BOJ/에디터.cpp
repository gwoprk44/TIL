#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string init;
    cin >> init;

    list<char> edit;
    for (auto c : init) {
        edit.push_back(c);
    }
    auto cursor = edit.end();

    int m;
    cin >> m;
    while (m--) {
        char op;
        cin >> op;

        if (op == 'L') {
            if (cursor != edit.begin()) cursor--;
        }
        else if (op == 'D') {
            if (cursor != edit.end()) cursor++;
        }
        else if (op == 'B') {
            if (cursor != edit.begin()) {
                cursor--;
                cursor = edit.erase(cursor);
            } 
        }
        else {
            char add;
            cin >> add;
            edit.insert(cursor, add);
        }
    }
    for (auto c : edit) cout << c;
}