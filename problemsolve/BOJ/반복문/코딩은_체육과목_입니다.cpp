#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n;
    cin >> n;

    for (int i = 4; i <= n; i++) {
        if (i % 4 == 0) {
            cout << "long ";
        } 
    }
    cout << "int";

    return 0;
}