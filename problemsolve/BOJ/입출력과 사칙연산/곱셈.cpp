#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int a, b;
    cin >> a >> b;

    int n1 = a * (b%10); 
    int n2 = a * ((b%100)/10); 
    int n3 = a * (b/100);

    cout << n1 << "\n";
    cout << n2 << "\n";
    cout << n3 << "\n";
    cout << a * b;


    return 0;
}