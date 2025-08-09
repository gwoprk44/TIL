#include <iostream>
#include <string>
#include <vector>

using namespace std;

int main() {
    
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    string expression;
    cin >> expression;

    int result = 0;
    string current_num_str = "";
    bool is_minus_section = false; 

    // 문자열의 끝까지 순회 (마지막 숫자를 처리하기 위해 길이까지 포함)
    for (int i = 0; i <= expression.length(); ++i) {
        
        // 연산자이거나 문자열의 끝에 도달했을 경우
        if (expression[i] == '+' || expression[i] == '-' || i == expression.length()) {
            // 지금까지 쌓아온 숫자 문자열을 정수로 변환
            int num = stoi(current_num_str);

            // is_minus_section 플래그에 따라 더하거나 뺀다
            if (is_minus_section) {
                result -= num;
            } else {
                result += num;
            }
            
            // 다음 숫자를 위해 초기화
            current_num_str = "";

            // 만약 현재 문자가 '-'라면, 플래그를 true로 설정
            // 이 시점 이후의 모든 숫자는 빼게 된다.
            if (expression[i] == '-') {
                is_minus_section = true;
            }
        } 
        // 숫자인 경우, 문자열에 추가
        else {
            current_num_str += expression[i];
        }
    }

    cout << result << endl;

    return 0;
}