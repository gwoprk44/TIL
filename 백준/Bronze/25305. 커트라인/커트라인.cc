#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int n, k;
    cin >> n >> k;

    int score[1001];

    for (int i = 0; i < n; i++) {
        cin >> score[i];
    }

    sort(score, score+n);
    cout << score[n-k];

    return 0;
}