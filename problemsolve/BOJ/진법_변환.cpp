#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string s;
    int b, sum = 0;
    cin >> s >> b;

    for (int i = 0; i < s.size(); i++) {
        if (s[i] > '0' && s[i] <= '9') {
            sum = sum * b + s[i] - 48;
        }
        else {
            sum = sum * b + (s[i] - 65 + 10);
        }
    }
    cout << sum;


}