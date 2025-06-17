#include <bits/stdc++.h>
using namespace std;

int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, k;

    cin >> n >> k;

    int ans = factorial(n) / (factorial(k) * factorial(n-k));

    cout << ans;
}