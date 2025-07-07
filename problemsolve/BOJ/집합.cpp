#include <iostream>
#include <string>

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout.tie(NULL);

    int M;
    std::cin >> M;

    int S = 0; // 집합을 나타낼 비트마스크 변수, 초기값은 공집합(0)

    for (int i = 0; i < M; ++i) {
        std::string command;
        std::cin >> command;

        if (command == "add") {
            int x;
            std::cin >> x;
            S |= (1 << (x - 1));
        } else if (command == "remove") {
            int x;
            std::cin >> x;
            S &= ~(1 << (x - 1));
        } else if (command == "check") {
            int x;
            std::cin >> x;
            if (S & (1 << (x - 1))) {
                std::cout << 1 << "\n";
            } else {
                std::cout << 0 << "\n";
            }
        } else if (command == "toggle") {
            int x;
            std::cin >> x;
            S ^= (1 << (x - 1));
        } else if (command == "all") {
            S = (1 << 20) - 1;
        } else if (command == "empty") {
            S = 0;
        }
    }

    return 0;
}