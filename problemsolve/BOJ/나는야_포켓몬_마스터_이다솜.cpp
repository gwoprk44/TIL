#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    cout.tie(NULL);

    int N, M;
    cin >> N >> M;

    vector<string> num_to_name(N + 1);

    unordered_map<string, int> name_to_num;

    // 포켓몬 도감 데이터 입력 및 저장
    for (int i = 1; i <= N; ++i) {
        string name;
        cin >> name;
        num_to_name[i] = name;
        name_to_num[name] = i;
    }

    // M개의 문제 처리
    for (int i = 0; i < M; ++i) {
        string query;
        cin >> query;

        // 입력된 query가 숫자인지 문자인지 판별
        if (isdigit(query[0])) {
            // query가 숫자일 경우
            int num = stoi(query); // 문자열을 정수로 변환
            cout << num_to_name[num] << '\n';
        } else {
            // query가 이름(문자열)일 경우
            cout << name_to_num[query] << '\n';
        }
    }

    return 0;
}