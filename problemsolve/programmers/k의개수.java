package problemsolve.programmers;


class k의개수 {
    public int solution(int i, int j, int k) {
        int answer = 0;
        
        String strK = String.valueOf(k);

        for (int l = i; l <= j; l++) {
            String value = String.valueOf(l);
            if (value.contains(strK)) {
                String[] arr = value.split("");
                for (String alpha : arr) {
                    if (alpha.equals(strK)) answer++;
                }
            }
        }


        return answer;
    }
}