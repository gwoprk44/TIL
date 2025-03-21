#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int arr[101];
    int n, m;

    cin >> n >> m;

    for (int i = 1; i <= n; i++) {
        arr[i] = i;
    }

    for (int i = 1; i <= m; i++) {
        int a, b;
        cin >> a >> b;

        reverse(arr + a, arr + b + 1);
    }

    for (int i = 1; i <= n; i++) {
        cout << arr[i] << " ";
    }

}