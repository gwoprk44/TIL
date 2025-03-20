#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    int arr[101] = {0, };
    
    cin >> n >> m;


    for (int i = 1; i <= n; i++) {
        int a, b, c;
        cin >> a >> b >> c;
        for (int j = a; j <= b; j++) {
            arr[j] = c;
        }
    }
    for (int i = 1; i <= n; i++) {
        cout << arr[i] << " ";
    }
}