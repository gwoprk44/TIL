/*
 * @lc app=leetcode id=9 lang=cpp
 *
 * [9] Palindrome Number
 */

// @lc code=start
class Solution {
public:
    bool isPalindrome(int x) {
        if (x < 0 || (x != 0 && x % 10 == 0)) {
            return false;
        }
        
        int ori = x;
        long rev = 0;

        while (x > 0) {
            int digit = x % 10;
            rev = rev * 10 + digit;
            x /= 10;
        }

        return ori == rev;
    }
};
// @lc code=end

