#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    vector<pair<int,int>> people(n);

    for (int i = 0; i < n; i++) {
        cin >> people[i].first >> people[i].second;
    }

    for (int i = 0; i < n; i++) {
        int rank = 1;
        for (int j = 0; j < n; j++) {
            if (people[j].first > people[i].first && people[j].second > people[i].second)
            rank++; 
        }
        cout << rank << " ";
    }

}