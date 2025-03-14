#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    stack<int> st;
    int n;
    string ans;
    int num = 1;

    cin >> n;

    while(n--) {
        int t;
        cin >> t;

        while(num <= t) {
            st.push(num++);
            ans += "+\n";
        }
        if (st.top() != t) {
            cout << "NO\n";
            return 0;
        }
        st.pop();
        ans+= "-\n";
    }
    cout << ans;
}