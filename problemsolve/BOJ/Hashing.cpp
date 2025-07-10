#include <iostream>
#include <string>
#include <vector>

// 모듈러 연산에 사용할 상수 정의
#define M 1234567891
#define r 31

int main() {
    // C++ 입출력 속도 향상
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);

    int L; // 문자열 길이
    std::cin >> L;

    std::string s; // 입력 문자열
    std::cin >> s;

    long long hash_value = 0; // 최종 해시 값 (오버플로우 방지를 위해 long long)
    long long r_power = 1;    // r의 거듭제곱 (r^0, r^1, r^2, ...)

    for (int i = 0; i < L; ++i) {
        // 1. a_i 값 계산 ('a'는 1, 'b'는 2, ...)
        long long a_i = s[i] - 'a' + 1;

        // 2. 현재 항 (a_i * r^i) 계산
        //    (a_i * r_power)를 먼저 계산한 후 모듈러 연산을 적용
        long long term = (a_i * r_power) % M;

        // 3. 해시 값에 현재 항 더하기
        //    (hash_value + term)을 계산한 후 모듈러 연산을 적용
        hash_value = (hash_value + term) % M;

        // 4. 다음 항을 위해 r_power 갱신
        //    r_power = r_power * r
        //    마찬가지로 곱셈 후 모듈러 연산 적용
        r_power = (r_power * r) % M;
    }

    std::cout << hash_value << std::endl;

    return 0;
}