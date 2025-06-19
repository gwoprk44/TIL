#include <bits/stdc++.h>
using namespace std;

int getNum(int k, int n) {
    if (k == 0) return n;
    if (n == 1) return 1;
    return getNum(k-1, n) + getNum(k, n-1);
}


int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int t, k, n;
    cin >> t;

    for (int i = 0; i < t; i++) {
        cin >> k >> n;
        cout << getNum(k, n) << "\n";
    }
    
}