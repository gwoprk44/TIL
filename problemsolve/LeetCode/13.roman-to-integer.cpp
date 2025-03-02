/*
 * @lc app=leetcode id=13 lang=cpp
 *
 * [13] Roman to Integer
 */

// @lc code=start

class Solution {
    public:
        int romanToInt(string s) {
            int ans = 0;
            unordered_map<char, int> m;
            m['I'] = 1;
            m['V'] = 5;
            m['X'] = 10;
            m['L'] = 50;
            m['C'] = 100;
            m['D'] = 500;
            m['M'] = 1000;
    
            for (int i = 0; i < s.size() - 1; i++) {
                if (m[s[i]] >= m[s[i+1]]) {
                    ans += m[s[i]];
                }
                else {
                    ans -= m[s[i]];
                }
            }
            ans += m[s[s.size() - 1]];
    
            return ans;
        }
    };
// @lc code=end

