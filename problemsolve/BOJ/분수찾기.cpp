#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int line = 1;
    int x;
    cin >> x;

    while (x - line > 0) {
        x -= line;
        line++;
    }

    if (line % 2 == 1) {
        cout << line + 1 - x << "/" << x;
    } else {
        cout << x << "/" << line + 1 - x;
    }
}