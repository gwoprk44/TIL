#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    vector<int> original(n);
    vector<int> sorted;

    for (int i = 0; i < n; i++) {
        cin >> original[i];
    }

    sorted = original;

    sort(sorted.begin(), sorted.end());
    sorted.erase(unique(sorted.begin(), sorted.end()), sorted.end());

    for (int i = 0; i < n; i++) {
        auto it = lower_bound(sorted.begin(), sorted.end(),  original[i]);

        cout << (it - sorted.begin()) << " ";
    }
}