#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int h,m;
    cin >> h >> m;

    if (m < 45) {
        m += 15;
        h -= 1;
        if (h < 0) {
            h = 23;
        }
    } else {
        m -= 45;
    }

    cout << h << " " << m;

    return 0;
}