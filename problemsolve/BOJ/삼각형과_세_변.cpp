#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int a,b,c;

    while (true) {
        cin >> a >> b >> c;
        if (a == 0 && b == 0 && c == 0) break;

        int sides[3] = {a,b,c};
        sort(sides, sides+3);

        if (sides[2] >= sides[0] + sides[1]) {
            cout << "Invalid\n";
        } else if (a == b && a == c) {
            cout << "Equilateral\n";
        } else if (a == b || a == c || b == c) {
            cout << "Isosceles\n";
        } else {
            cout << "Scalene\n";
        }
    }
}