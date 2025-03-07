#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, v, arr[201] = {};

    cin >> n;

    while(n--) {
        int t;
        cin >> t;
        arr[t+100]++;
    }
    cin >> v;
    cout << arr[v+100];
} 