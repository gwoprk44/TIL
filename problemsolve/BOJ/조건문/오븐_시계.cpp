#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int a,b,c;
    cin >> a >> b >> c;

    int total = a * 60 + b + c;

    int h = (total / 60) % 24;
    int m = total % 60;

    cout << h << " " << m;
    

    return 0;
}