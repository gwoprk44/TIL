#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    
    int n, m, a, b;
    int arr[101] = {0,};

    cin >> n >> m;

    for (int i = 1; i <= n; i++) {
        arr[i] = i;
    }

    for (int i = 0; i < m; i++) {
        cin >> a >> b;
        swap(arr[a], arr[b]);
    }

    for (int i = 1; i <= n; i++) {
        cout << arr[i] << " ";
    }



    return 0;
}