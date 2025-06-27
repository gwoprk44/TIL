#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int T;
    cin >> T;

    string ps;

    while(T--) {
        stack<char> st;
        cin >> ps;
        bool isVPS = true;

        for (char c : ps) {
            if (c == '(') st.push(c);
            else {
                if (st.empty()) {
                    isVPS = false;
                    break;
                }
                st.pop();
            }
        }
        if (!st.empty()) isVPS = false;
        cout << (isVPS ? "YES" : "NO") << "\n";
    }
}