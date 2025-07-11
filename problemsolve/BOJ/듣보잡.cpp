#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    cin >> n >> m;

    unordered_set<string> unheard_people;
    vector<string> result;

    for (int i = 0; i < n; i++) {
        string name;
        cin >> name;
        unheard_people.insert(name);
    }

    for (int i = 0; i < m; i++) {
        string name;
        cin >> name;
        if (unheard_people.count(name)) {
            result.push_back(name);
        }
    }

    sort(result.begin(), result.end());

    cout << result.size() << "\n";
    for (string s : result) {
        cout << s << "\n";
    }

}