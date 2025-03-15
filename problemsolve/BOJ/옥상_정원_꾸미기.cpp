#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    stack<int> st;
    long long ans = 0;

    cin >> n;

    while(n--) {
        long long h;
        cin >> h;
        while (!st.empty() && st.top() <= h) {
            st.pop();
        }
        ans += st.size();
        st.push(h);
    }
    cout << ans;
}