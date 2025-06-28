#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, max = 0, idx = 0;
    
    for (int i = 1; i <= 9; i++) {
        cin >> n;
        if (n > max) {
            max = n;
            idx = i;
        }
    }
    cout << max << "\n" << idx;
}