#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    stack<int> st;
    int k,n,ans = 0;

    cin >> k;

    while(k--) {
        cin >> n;
        if (n == 0) st.pop();
        else st.push(n);
    }

    while(!st.empty()) {
        ans += st.top();
        st.pop();
    }

    cout << ans;
}