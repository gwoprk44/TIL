#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    vector<int> notes(8);
    bool is_ascending = true;
    bool is_descending = true;

    for (int i = 0; i < 8; i++) {
        cin >> notes[i];
    }

    for (int i = 0; i < 8; i++) {
        if (notes[i] != i + 1) {
            is_ascending = false;
        }
        if (notes[i] != 8 - i) {
            is_descending = false;
        }
    }

    if (is_ascending) {
        cout << "ascending";
    } else if (is_descending) {
        cout << "descending";
    } else {
        cout << "mixed";
    }
}