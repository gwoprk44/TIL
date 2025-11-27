#include <bits/stdc++.h>
using namespace std;


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    int a, b;
    cin >> a >> b;

    map<int,bool> mp;

    for (int i = 0; i < a+b; i++) {
        int num;
        cin >> num;
        
        if (mp[num] == true) {
            mp.erase(num);
        } else {
            mp[num] = true;
        }
    }

    cout << mp.size();

    return 0;
}