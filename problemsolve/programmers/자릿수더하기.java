package problemsolve.programmers;

class 자릿수더하기 {
    public int 자릿수더하기(int n) {
        int answer = 0;

        while (n != 0) {
            answer += n % 10;
            n /= 10;
        }
        return answer;
    }
}