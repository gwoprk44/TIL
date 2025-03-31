#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, b;
    string ans;
    cin >> n >> b;

    while(n > 0) {
        int temp = n % b;
        if(temp >= 0 && temp <= 9) ans += temp + '0';
        else ans += temp-10 + 'A';
        n /= b;
    }
    for (int i = ans.length()-1; i >= 0; i--) {
        cout << ans[i];
    }
}