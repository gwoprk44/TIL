#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, score;
    float ans;
    cin >> n;

    int max = -1;
    int sum = 0;

    for (int i = 0; i < n; i++) {
        cin >> score;
        sum += score;
        if (score > max) {
            max = score;
        }
    }

    ans = sum * 100.0 / max / n;
    cout << ans;

}