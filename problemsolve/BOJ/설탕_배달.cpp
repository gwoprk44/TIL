#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;

    cin >> n;

    int ans = 0;

    while (true)
    {
        if (n % 5 == 0) {
            ans += n / 5;
            cout << ans;
            break;
        }
        n -= 3;
        ans++;

        if (n < 0) {
            cout << -1;
            break;
        }
    } 
    
}