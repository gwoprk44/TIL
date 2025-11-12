#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    vector<int> num(5);
    int sum = 0;
    
    for (int i = 0; i < 5; i++) {
        cin >> num[i];
        sum += num[i];
    }

    sort(num.begin(), num.end());

    cout << sum / 5 << "\n" << num[2];

    return 0;
}