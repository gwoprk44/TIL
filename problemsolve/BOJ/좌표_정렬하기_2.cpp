#include <bits/stdc++.h>
using namespace std;

bool compare(pair<int,int> a, pair<int, int> b) {
    if (a.second == b.second) {
        return a.first < b.first;
    } else {
        return a.second < b.second;
    }
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    vector<pair<int, int>> cords(n);

    for (int i = 0; i < n; i++) {
        cin >> cords[i].first >> cords[i].second;
    }

    sort(cords.begin(), cords.end(), compare);

    for (int i = 0; i < n; i++) {
        cout << cords[i].first << " " << cords[i].second << "\n";
    }
}