/*
 * @lc app=leetcode id=14 lang=cpp
 *
 * [14] Longest Common Prefix
 */

// @lc code=start
class Solution {
    public:
        string longestCommonPrefix(vector<string>& strs) {
            sort(strs.begin(), strs.end());
            string ans = "";
    
            string first = strs[0];
            string last = strs[strs.size() - 1];
    
            for (int i = 0; i < strs[0].size(); i++) {
                if (first[i] == last[i]) {
                    ans += first[i];
                }
                else break;
            }
            return ans;
        }
    };
// @lc code=end

