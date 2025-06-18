#include <bits/stdc++.h>
using namespace std;

bool compare(string a, string b) {
    if (a.length() == b.length()) {
        return a < b;
    }
    return a.length() < b.length();
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    string words[20001];

    for (int i = 0; i < n; i++) {
        cin >> words[i];
    }

    sort(words, words + n, compare);

    for (int i = 0; i < n; i++) {
        if (i > 0 && words[i] == words[i-1]) {
            continue;
        }
        cout << words[i] << "\n";
    }
}