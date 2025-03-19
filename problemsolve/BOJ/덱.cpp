#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    deque<int> DQ;
    int n;
    cin >> n;

    while(n--) {
        string dq;
        cin >> dq;

        if (dq == "push_front") {
            int val;
            cin >> val;
            DQ.push_front(val);
        }
        else if (dq == "push_back") {
            int val;
            cin >> val;
            DQ.push_back(val);
        }
        else if (dq == "pop_front") {
            if (DQ.empty()) {
                cout << -1 << "\n";
            }
            else {
                cout << DQ.front() << "\n";
                DQ.pop_front();
            }
        }
        else if (dq == "pop_back") {
            if (DQ.empty()) {
                cout << -1 << "\n";
            }
            else {
                cout << DQ.back() << "\n";
                DQ.pop_back();
            }
        }
        else if (dq == "size") {
            cout << DQ.size() << "\n";
        }
        else if (dq == "empty") {
            cout << (int)DQ.empty() << "\n";
        }
        else if (dq == "front") {
            if (DQ.empty()) {
                cout << -1 << "\n";
            }
            else {
                cout << DQ.front() << "\n";
            }
        }
        else {
            if (DQ.empty()) {
                cout << -1 << "\n";
            }
            else {
                cout << DQ.back() << "\n";
            }
        }
    }
}