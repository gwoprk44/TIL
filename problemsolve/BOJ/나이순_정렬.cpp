#include <bits/stdc++.h>
using namespace std;

bool compare(pair<int, string> a, pair<int, string> b) {
    return a.first < b.first;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    vector<pair<int, string>> member(n);
    
    for (int i = 0; i < n; i++) {
        cin >> member[i].first >> member[i].second;
    }

    stable_sort(member.begin(), member.end(), compare);

    for (int i = 0; i < n; i++) {
        cout << member[i].first << ' ' << member[i].second << "\n";
    }
}
