#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n, v;
    int ans = 0;
    int arr[101];

    cin >> n; 
    
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }
    cin >> v;

    for (int i = 0; i < n; i++) {
        if (arr[i] == v) {
            ans++;
        }
    }

    cout << ans;
    
    

    return 0;
}