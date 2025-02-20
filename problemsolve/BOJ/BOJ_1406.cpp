#include <bits/stdc++.h>
using namespace std;

int main(void) {
	ios::sync_with_stdio;
	cin.tie(0);

	string init;
	cin >> init;

	list<char> edit;
	for (auto c : init) edit.push_back(c);
	auto cursor = edit.end();

	int m;
	cin >> m;
	while (m--) {
		char op;
		cin >> op;
		if (op == 'P') {
			char add;
			cin >> add;
			edit.insert(cursor, add);
		}
		else if (op == 'L') {
			if (cursor != edit.begin()) cursor--;
		}
		else if (op == 'D') {
			if (cursor != edit.end()) cursor++;
		}
		else {
			if (cursor != edit.begin()) {
				cursor--;
				cursor = edit.erase(cursor);
			}
		}
	}
	for (auto c : edit) cout << c;
}