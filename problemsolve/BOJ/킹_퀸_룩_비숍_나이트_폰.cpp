#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int chess[7] = {1,1,2,2,2,8};
    int n;

    for (int i = 0; i < 6; i++) {
        cin >> n;
        cout << chess[i] - n << " ";
    }
}