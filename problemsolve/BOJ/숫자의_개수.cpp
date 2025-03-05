#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int a, b, c;
    cin >> a >> b >> c;

    int arr[10] = {};
    int t = a * b * c;
    

    while(t > 0) {
        arr[t % 10]++;
        t /= 10;
    }

    for (int i = 0; i < 10; i++) {
        cout << arr[i] << "\n";
    }
}