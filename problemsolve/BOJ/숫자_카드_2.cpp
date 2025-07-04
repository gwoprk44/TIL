#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    unordered_map<int, int> cards_counts;

    int n;
    cin >> n;

    for (int i = 0; i < n; i++) {
        int card;
        cin >> card;
        cards_counts[card]++;
    }

    int m;
    cin >> m;

    for (int i = 0; i < m; i++) {
        int target;
        cin >> target;

        if (cards_counts.find(target) != cards_counts.end()) {
            cout << cards_counts[target] << " ";
        } else {
            cout << 0 << " ";
        }
    }
}