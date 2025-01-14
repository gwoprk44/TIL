package problemsolve.programmers;

class Solution {
    public int solution(String[] babbling) {

        String[] babbString = {"aya", "ye", "woo", "ma"};
        int answer = 0;

        for (int i = 0; i < babbling.length; i++) {
            for (int j = 0; j < babbString.length; j++) {
                babbling[i] = babbling[i].replace(babbString[j], " ");
            }

            if(babbling[i].trim().length() == 0) {
                answer++;
            }
        }

        return answer;
    }
}