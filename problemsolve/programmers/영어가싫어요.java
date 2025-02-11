package problemsolve.programmers;

class 영어가싫어요 {
    public long solution(String numbers) {

        String[] stringNumbers = {"zero", "one", "two", "three",
                                "four", "five", "six", "seven",
                                "eight", "nine"};
        for (int i = 0; i < stringNumbers.length; i++) {
            numbers = numbers.replaceAll(stringNumbers[i], String.valueOf(i));
        }

        return Long.parseLong(numbers);
    }
}
