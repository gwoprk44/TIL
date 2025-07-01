#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int t;
    cin >> t;

    while (t--) {
        string result;
        cin >> result;
        int tot = 0;
        int cur = 0;

        for (char c : result) {
            if (c == 'O') {
                cur++;
                tot += cur;
            } else {
                cur = 0;
            }
        }
        cout << tot << "\n";
    }
}