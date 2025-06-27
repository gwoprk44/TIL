#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string isbn;
    cin >> isbn;
    int sum = 0;
    int star_pos = isbn.find('*');

    for (int i = 0; i < 13; i++) {
        if (i == star_pos) continue;
        int num = isbn[i] - '0';
        if (i % 2 == 0) sum += num * 1;
        else sum += num * 3;
    }

    for (int x = 0; x <= 9; x++) {
        int temp_sum = sum;
        if (star_pos % 2 == 0) temp_sum += x * 1;
        else temp_sum += x * 3;

        if (temp_sum % 10 == 0) {
            cout << x;
        }
    }
}