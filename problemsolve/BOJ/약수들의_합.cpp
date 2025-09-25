#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n = 0;

    while (1) {
        cin >> n;
        if (n == -1) break;

        vector<int> divisor;
        int perfect = 0;

        for (int i = 1; i < n; i++) {
            if (n % i == 0) {
                divisor.push_back(i);
                perfect+=i;
            }
        }
        
        if (perfect == n) {
            cout << n << " = ";
            for (int i = 0; i < divisor.size() - 1; i++) {
                cout << divisor[i] << " + ";
            }
            cout << divisor[divisor.size() - 1] << "\n";
        } else {
            cout << n << " is NOT perfect." << "\n";
        }
    }
    
}