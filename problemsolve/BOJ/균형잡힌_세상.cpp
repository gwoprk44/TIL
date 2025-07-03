#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    while (true) {
        string input;
        getline(cin, input);

        if (input == ".") break;

        stack<char> st;
        bool is_balanced = true; 

        for (char c : input) { 
            if (c == '(' || c == '[') {
                // 1. 여는 괄호는 스택에 push
                st.push(c);
            } 
            else if (c == ')') {
                // 2. 닫는 소괄호 ')'를 만났을 때
                if (st.empty() || st.top() != '(') {
                    // 스택이 비어있거나, 짝이 맞지 않으면 실패
                    is_balanced = false;
                    break;
                }
                // 짝이 맞으면 스택에서 pop
                st.pop();
            } 
            else if (c == ']') {
                // 3. 닫는 대괄호 ']'를 만났을 때
                if (st.empty() || st.top() != '[') {
                    // 스택이 비어있거나, 짝이 맞지 않으면 실패
                    is_balanced = false;
                    break;
                }
                // 짝이 맞으면 스택에서 pop
                st.pop();
            }
            // 괄호가 아닌 다른 문자는 무시하고 그냥 넘어감
        }

        // 최종적으로 문자열 순회가 끝난 후
        // 1. 순회 중에 짝이 안 맞는 경우가 없었고 (is_balanced == true)
        // 2. 스택에 남아있는 여는 괄호가 없어야 함 (st.empty())
        if (is_balanced && st.empty()) {
            cout << "yes" << "\n";
        } else {
            cout << "no" << "\n";
        }
    }
}