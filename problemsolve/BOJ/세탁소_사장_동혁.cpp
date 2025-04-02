#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int coins[4] = {25, 10, 5, 1};
    int t, c;

    cin >> t;
    while(t--) {
        cin >> c;
        for (int i = 0; i < 4; i++) {
            cout << c / coins[i] << " ";
            c %= coins[i];
        }
        cout << "\n";
    }
}