#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int t;
    cin >> t;

    int h, w, n;

    for (int i = 0; i < t; i++) {
        cin >> h >> w >> n;
        int floor = n % h;
        int room = n / h + 1;
        if (floor == 0) {
            floor = h;
            room = n / h;
        }
        cout << floor * 100 + room << "\n";
    }
    return 0;
}
