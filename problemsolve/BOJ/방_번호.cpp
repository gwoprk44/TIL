#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, num[10] = {}, ans = 0;
    
    cin >> n;

    while(n) {
        num[n%10]++;
        n /= 10;
    }

    for (int i = 0; i < 10; i++) {
        if (i == 6 || i == 9) {
            continue;
        }
        ans = max(ans, num[i]);
    }
    ans = max(ans, (num[6] + num[9] + 1)/2);

    cout << ans;
}