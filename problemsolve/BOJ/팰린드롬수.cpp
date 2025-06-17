#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string N;

    while (true) {
        cin >> N;
        if (N == "0") break;

        string original = N;

        reverse(N.begin(), N.end());

        if (N == original) {
            cout << "yes\n";
        } else {
            cout << "no\n";
        }
    }
}