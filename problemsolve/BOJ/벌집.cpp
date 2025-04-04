#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    int layer = 1;
    int max = 1;
    int increment = 6;

    while (n > max) {
        max += layer * increment;
        layer++;
    }
    cout << layer;
}