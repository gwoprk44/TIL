#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int sides[3];

    cin >> sides[0] >> sides[1] >> sides[2];

    sort(sides, sides+3);

    if (sides[0] + sides[1] <= sides[2]) {
        sides[2] = sides[0] + sides[1] - 1;
    }
    cout << sides[0] + sides[1] + sides[2];
}