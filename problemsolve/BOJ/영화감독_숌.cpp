#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    int count = 0;
    int num = 666;

    while (true) {
        if (to_string(num).find("666") != string::npos) {
            count++;
            if (count == n) {
                cout << num;
                break;
            }
        }
        num++;
    }


}