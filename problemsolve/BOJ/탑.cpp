#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    stack<pair<int,int>> st;

    cin >> n;
    st.push({100000005, 0});    // 높이, 인덱스

    for (int i = 1; i <= n; i++) {
        int tower;
        cin >> tower;

        while(st.top().first <= tower) { //수신 못함
            st.pop();
        }
        cout << st.top().second << " ";
        st.push({tower, i});
    }
}