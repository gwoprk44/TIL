#include <iostream>
using namespace std;

int main() {
    int N, T, P;
    int size[6];
    
    // 입력 받기
    cin >> N;
    for (int i = 0; i < 6; i++) {
        cin >> size[i];
    }
    cin >> T >> P;
    
    // 티셔츠 묶음 수 계산
    int tshirt_bundles = 0;
    for (int i = 0; i < 6; i++) {
        tshirt_bundles += (size[i] + T - 1) / T;  // 올림 처리
    }
    
    // 펜 묶음 수와 개별 주문 수 계산
    int pen_bundles = N / P;
    int pen_individual = N % P;
    
    // 출력
    cout << tshirt_bundles << '\n';
    cout << pen_bundles << ' ' << pen_individual << '\n';
    
    return 0;
}