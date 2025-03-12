#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    stack<int> st;

    while(n--) {
        string s;
        cin >> s;

        if (s == "push") {
            int t;
            cin >> t;
            st.push(t);
        }
        else if (s == "pop") {
            if (st.empty()) cout << -1 << '\n';
            else {
                cout << st.top() << '\n';
                st.pop();
            }
        }
        else if (s == "size") {
            cout << st.size() << '\n';
        }
        else if (s == "empty") {
            if (st.empty()) cout << 1 << '\n';
            else cout << 0 << '\n';
        }
        else if (s == "top") {  
            if (st.empty()) cout << -1 << '\n';
            else {
                cout << st.top() << '\n';  
            }
        }
    }
}