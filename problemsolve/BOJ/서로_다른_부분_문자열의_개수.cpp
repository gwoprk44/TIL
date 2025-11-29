#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    set<string> v;
    string s;

    cin >> s;
    
    for (int i = 0; i < s.length(); i++) {
        for (int j = i; j < s.length(); j++) {
            v.insert(s.substr(i, j-i+1));
        }
    }

    cout << v.size();

    return 0;
}