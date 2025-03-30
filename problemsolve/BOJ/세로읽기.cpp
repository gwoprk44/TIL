#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string words[5];
    
    for (int i = 0; i < 5; i++) {
        cin >> words[i];
    }

    for (int col = 0; col < 15; col++) {
        for (int row = 0; row < 5; row++) {
            if (col < words[row].length()) {
                cout << words[row][col];
            }
        }
    }
}