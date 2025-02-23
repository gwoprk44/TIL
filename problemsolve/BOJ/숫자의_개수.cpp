#include <bits/stdc++.h>
using namespace std;

int main(void) {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int a,b,c;
    cin >> a >> b >> c;

    int t = a*b*c;
    int ans[10] = {};

    while (t>0) {
        ans[t%10]++;
        t/=10;
    }

    for (int i = 0; i < 10; i++) {
        cout << ans[i] << '\n';
    }
}