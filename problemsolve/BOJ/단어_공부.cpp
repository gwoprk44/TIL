#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    string s;
    cin >> s;

    int alpha[26] = {0}; // 배열 초기화
    int cnt = 0;

    for (int i = 0; i < s.length(); i++) {
        if (s[i] >= 'A' && s[i] <= 'Z') {
            alpha[s[i] - 'A']++;  
        }
        else if (s[i] >= 'a' && s[i] <= 'z') {
            alpha[s[i] - 'a']++;
        }
    }

    int max = 0, max_index = 0;

    for (int i = 0; i < 26; i++) {
        if(max < alpha[i]) {
            max = alpha[i];
            max_index = i;
            cnt = 1; // 최댓값이 바뀌면 카운트 초기화
        }
        else if(max == alpha[i]) {
            cnt++;
        }
    }

    if(cnt > 1) cout << "?";
    else cout << (char)(max_index + 'A');
}
