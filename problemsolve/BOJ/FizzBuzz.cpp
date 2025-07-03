#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string s1, s2, s3;
    cin >> s1 >> s2 >> s3;

    int target = 0;

    if (isdigit(s1[0])) {
        int num = stoi(s1);
        target = num + 3;
    } else if (isdigit(s2[0])) {
        int num = stoi(s2);
        target = num + 2;
    } else {
        int num = stoi(s3);
        target = num + 1;
    }

    if (target % 3 == 0 && target % 5 == 0) {
        cout << "FizzBuzz" << "\n";
    } else if (target % 3 == 0) {
        cout << "Fizz" << "\n";
    } else if (target % 5 == 0) {
        cout << "Buzz" << "\n";
    } else {
        cout << target << "\n";
    }
}