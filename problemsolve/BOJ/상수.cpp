#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string a, b, aa, bb;
    cin >> a >> b;

    for (int i = 2; i >= 0; i--) {
        aa += a[i];
        bb += b[i];
    }

    cout << max(aa, bb);

}