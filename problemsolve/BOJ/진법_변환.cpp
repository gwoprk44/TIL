#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int ans = 0;
    int b;
    string n;

    cin >> n >> b;

    for (int i = 0; i < n.length(); i++) {
        if(n[i] >= '0' && n[i] <= '9') ans = ans * b + n[i] - '0';
        else ans = ans * b + n[i] - 'A' + 10;
    }
    cout << ans;
}