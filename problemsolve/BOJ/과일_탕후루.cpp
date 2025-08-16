#include <iostream>
#include <vector>
#include <algorithm>

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);

    int N;
    std::cin >> N;

    std::vector<int> fruits(N);
    for (int i = 0; i < N; ++i) {
        std::cin >> fruits[i];
    }

    int fruit_counts[10] = {0};

    int left = 0;           // 윈도우의 시작 포인터
    int max_len = 0;        // 최대 길이 저장 변수
    int distinct_fruits = 0; // 윈도우 내 과일 종류의 수

    // right 포인터가 0부터 N-1까지 이동하면서 윈도우를 확장
    for (int right = 0; right < N; ++right) {
        // 1. 윈도우에 새로운 과일(fruits[right]) 추가
        int current_fruit = fruits[right];
        
        // 이 과일이 윈도우에 처음 들어오는 종류라면, 종류의 수를 1 증가
        if (fruit_counts[current_fruit] == 0) {
            distinct_fruits++;
        }
        fruit_counts[current_fruit]++;

        // 2. 윈도우의 상태 확인 및 축소
        // 과일 종류가 2개를 초과하면, 조건을 만족할 때까지 left를 이동시켜 윈도우 축소
        while (distinct_fruits > 2) {
            int fruit_to_remove = fruits[left];
            fruit_counts[fruit_to_remove]--;
            
            // 만약 특정 과일의 개수가 0이 되면, 윈도우에서 해당 종류가 완전히 빠진 것이므로
            // 종류의 수를 1 감소
            if (fruit_counts[fruit_to_remove] == 0) {
                distinct_fruits--;
            }
            left++; // left 포인터 이동
        }

        // 3. 최대 길이 갱신
        // 현재 윈도우 [left, right]는 조건을 만족하므로, 길이를 계산하여 최대값 갱신
        max_len = std::max(max_len, right - left + 1);
    }

    std::cout << max_len << std::endl;

    return 0;
}