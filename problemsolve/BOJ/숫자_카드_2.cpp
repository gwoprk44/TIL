#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n;
    cin >> n;

    map<int, int> num_card;

    for (int i = 0; i < n; i++) {
        int card;
        cin >> card;
        num_card[card]++;
    }

    int m;
    cin >> m;

    for (int i = 0; i < m; i++) {
        int target;
        cin >> target;
        if (num_card.find(target) != num_card.end()) {
            cout << num_card[target] << " ";
        } else {
            cout << 0 << " ";
        }
    }
    return 0;
}