#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int count = 0;
    int n, k;

    cin >> n >> k;

    for (int i = 1; i <= n; i++) {
        if (n % i == 0) {
            count++;
            if (count == k) {
                cout << i;
                return 0;
            }
        }
    }
    cout << 0;
}