#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int arr[31] = { 0, };
    int n;
    

    for (int i = 1; i <= 28; i++) {
        cin >> n;
        arr[n] = 1; 
    }

    for (int i = 1; i <= 30; i++) {
        if (arr[i] != 1) 
        cout << i << "\n";
    }
}