#include <bits/stdc++.h>
using namespace std;

stack<int> s;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    stack<int> s;
    int k, n, ans = 0;
    
    cin >> k;

    while(k--) {
        cin >> n;
        if (n == 0) s.pop();
        else s.push(n);
    }

    while(!s.empty()) {
        ans += s.top();
        s.pop();
    }
    cout << ans;
}