#include <bits/stdc++.h>
using namespace std;

bool isPrime(int num) {
    if (num < 2) return false;
    for (int i = 2; i <= sqrt(num); i++) { 
        if (num % i == 0) return false;
    }
    return true;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, count = 0;
    cin >> n;

    for (int i = 0; i < n; i++) {
        int num;
        cin >> num;
        if (isPrime(num)) count++;
    }

    cout << count;
    return 0;
}
