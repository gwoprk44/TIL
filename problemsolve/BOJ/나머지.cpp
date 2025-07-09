#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    set<int> ans;

    for (int i = 0; i < 10; i++) {
        cin >> n;
        ans.insert(n % 42);
    }
    cout << ans.size();
}