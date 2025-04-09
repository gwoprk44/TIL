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

    int n, m;
    cin >> m >> n;

    vector<int> primes;

    for (int i = m; i <= n; i++) {
        if(isPrime(i)) {
            primes.push_back(i);
        }
    }

    if (primes.empty()) {
        cout << -1;
    } else {
        int sum = accumulate(primes.begin(), primes.end(), 0);
        int minPrime = primes[0];

        cout << sum << "\n";
        cout << minPrime << "\n";
    }


}