#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n, x;
    cin >> n >> x;

    int arr[n];

    for (int i = 0; i < n; i++) {
        cin >> arr[i];
        if (arr[i] < x) {
            cout << arr[i] << " ";
        }
    }

    return 0;
}