#include <iostream>
#include <vector>
#include <numeric>     // std::accumulate (합계를 위해 사용 가능)
#include <algorithm>   // std::sort
#include <cmath>       // std::round

int main() {
    // C++ 입출력 속도 향상
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);

    int N;
    std::cin >> N;

    std::vector<int> nums(N);
    long long sum = 0;
    // 최빈값 계산을 위한 카운팅 배열
    // -4000 ~ 4000 범위의 숫자를 카운트. 
    // 인덱스: 숫자 + 4000 (0 ~ 8000)
    std::vector<int> count(8001, 0); 

    for (int i = 0; i < N; ++i) {
        std::cin >> nums[i];
        sum += nums[i];
        count[nums[i] + 4000]++;
    }

    // 중앙값과 범위를 구하기 위해 정렬
    std::sort(nums.begin(), nums.end());

    // 1. 산술평균 출력
    // round 함수는 double형을 인자로 받으므로 sum을 double로 캐스팅
    // round(-0.0)은 -0.0을 반환하지만, int로 출력하면 0이 되므로 문제 없음.
    int mean = round((double)sum / N);
    std::cout << mean << '\n';

    // 2. 중앙값 출력
    // N은 홀수이므로 N/2 인덱스가 중앙값
    int median = nums[N / 2];
    std::cout << median << '\n';

    // 3. 최빈값 출력
    int max_frequency = 0;
    for (int i = 0; i <= 8000; ++i) {
        if (count[i] > max_frequency) {
            max_frequency = count[i];
        }
    }

    std::vector<int> modes;
    for (int i = 0; i <= 8000; ++i) {
        if (count[i] == max_frequency) {
            // 인덱스를 원래 숫자로 변환 (i - 4000)
            modes.push_back(i - 4000);
        }
    }

    // modes 벡터는 이미 오름차순 정렬 상태
    int mode;
    if (modes.size() > 1) {
        // 최빈값이 여러 개면 두 번째로 작은 값 출력
        mode = modes[1];
    } else {
        mode = modes[0];
    }
    std::cout << mode << '\n';

    // 4. 범위 출력
    int range = nums.back() - nums.front(); // nums[N-1] - nums[0]
    std::cout << range << '\n';

    return 0;
}