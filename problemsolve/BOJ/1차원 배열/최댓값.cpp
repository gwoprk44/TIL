#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    
    int maxv = -1;
    int maxvidx;
    int num;

    for (int i = 0; i < 9; i++) {
        cin >> num;
        if (num > maxv) {
            maxv = num;
            maxvidx = i;
        }
    }

    cout << maxv << "\n";
    cout << maxvidx + 1;

    return 0;
}