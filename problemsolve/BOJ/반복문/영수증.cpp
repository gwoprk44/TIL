#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    long x;
    int n, a, b;
    int total = 0;

    cin >> x;
    cin >> n;

    for (int i = 1; i <= n; i++) {
        cin >> a >> b;
        total += a * b;
    }
    if (x == total) {
            cout << "Yes";
        } else {
            cout << "No";
        }

    return 0;
}