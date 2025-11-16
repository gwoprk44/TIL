#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    string n;

    cin >> n;

    sort(n.begin(), n.end(), greater<char>());

    cout << n;

    return 0;
}