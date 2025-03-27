#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    vector<string> croatian = {"c=", "c-", "dz=", "d-", "lj", "nj", "s=", "z="};

    string s;
    cin >> s;

    for (int i = 0; i < croatian.size(); i++) {
        while(1) {
            int idx = s.find(croatian[i]);
            if (idx == string::npos) break;
            s.replace(idx,croatian[i].length(), "!"); 
        }
    }
    cout << s.length();
}