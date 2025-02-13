package problemsolve.programmers;

class 치킨쿠폰 {
    public int solution(int chicken) {
        int answer = 0;
        int coupon1 = chicken;
        int coupon2 = 0;

        while (coupon1 + coupon2 >= 10) {
            int tmp1 = (coupon1 + coupon2) / 10;
            int tmp2 = (coupon1 + coupon2) % 10;

            coupon1 = tmp1;
            coupon2 = tmp2;
            answer += coupon1;
        }
        
        return answer;
    }
}