#include <bits/stdc++.h>
using namespace std;

bool compare(pair<int, string> a, pair<int, string> b) {
    return a.first < b.first;
} 


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n;
    cin >> n;

    vector<pair<int, string>> member(n);

    for (int i = 0; i < n; i++) {
        cin >> member[i].first >> member[i].second;
    }

    stable_sort(member.begin(), member.end(), compare);

    for (int i = 0; i < n; i++) {
        cout << member[i].first << " " << member[i].second << "\n";
    }

    return 0;
}