#include <bits/stdc++.h>
using namespace std;

int arr[1000001];
int ans[1000001];

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    int n;
    cin >> n;

    stack<int> st;

    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }

    for (int i = n-1; i >= 0; i-- ) {
        while(!st.empty() && st.top() <= arr[i]) {
            st.pop();
        }
        if(st.empty()) {
            ans[i] = -1;
        } else {
            ans[i] = st.top();
        }
        st.push(arr[i]);
    }
    for (int i = 0; i < n; i++) {
        cout << ans[i] << " ";
    }
}