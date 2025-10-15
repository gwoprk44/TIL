#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    int result = 665;
    string temp;

    for (int i = 0; i < n; i++) {
        while (true) {
            result++;
            temp = to_string(result);
            if (temp.find("666") != string::npos) break;
        }
    }

    cout << result;
}