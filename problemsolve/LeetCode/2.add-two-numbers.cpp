/*
 * @lc app=leetcode id=2 lang=cpp
 *
 * [2] Add Two Numbers
 */

// @lc code=start
/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     ListNode *next;
 *     ListNode() : val(0), next(nullptr) {}
 *     ListNode(int x) : val(x), next(nullptr) {}
 *     ListNode(int x, ListNode *next) : val(x), next(next) {}
 * };
 */
class Solution {
    public:
        ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
            int carry = 0;
            ListNode* head = new ListNode();
            ListNode* tail = head;
    
            while (l1 != NULL || l2 != NULL || carry) {
                int sum = 0;
                if (l1 != NULL) {
                    sum += l1->val;
                    l1 = l1->next;
                }
                if (l2 != NULL) {
                    sum += l2->val;
                    l2 = l2->next;
                }
                sum += carry;
                carry = sum / 10;
    
                ListNode* newNode = new ListNode(sum % 10);
                tail->next = newNode;
                tail = tail->next;
            }
            return head -> next;
        }
    };
// @lc code=end

