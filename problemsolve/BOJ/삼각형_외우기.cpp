#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int a, b, c; 
    cin >> a >> b >> c;

    if (a+b+c != 180) {
        cout << "Error";
    } else if (a == 60 && b == 60 && c == 60) {
        cout << "Equilateral";
    } else if (a == b || a == c || b == c) {
        cout << "Isosceles";
    } else {
        cout << "Scalene";
    }
}