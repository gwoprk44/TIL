#include <bits/stdc++.h>
using namespace std;

int paper[129][129];
int white = 0;
int blue = 0;

void solve(int row, int col, int k) {
    bool cut = false;
    int first_color = paper[row][col];

    for (int i = row; i < row + k; i++) {
        for (int j = col; j < col + k; j++) {
            if (paper[i][j] != first_color) {
                cut = true;
                break;
            }
        }
    }

    if (cut) {
        solve(row, col, k/2);
        solve(row, col + k/2, k/2);
        solve(row + k/2, col, k/2);
        solve(row + k/2, col + k/2, k/2);
    } else {
        if (first_color == 1) {
            blue++;
        }else {
            white++;
        }
    }
}



int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cin >> paper[i][j];
        }
    }

    solve(0, 0, n);
    cout << white << "\n";
    cout << blue;
}