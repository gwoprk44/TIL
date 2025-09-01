#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int a, b;
    cin >> a >> b;

    int b_1 = b % 10;
    int b_2 = (b / 10) % 10;
    int b_3 = b / 100;

    int ans_1 = b_1 * a;
    int ans_2 = b_2 * a;
    int ans_3 = b_3 * a;
    int ans_4 = b * a;

    cout << ans_1 << "\n";
    cout << ans_2 << "\n";
    cout << ans_3 << "\n";
    cout << ans_4 << "\n";
}