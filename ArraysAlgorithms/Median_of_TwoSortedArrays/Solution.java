package Median_of_TwoSortedArrays;

public class Solution {
     public double findMedianSortedArrays(int[] nums1, int[] nums2) {
          if (nums1.length > nums2.length) {
               int[] temp = nums1;
               nums1 = nums2;
               nums2 = temp;
          }
          int n = nums1.length, m = nums2.length;
          int low = 0, high = n;
          while (low <= high) {
               int mid1 = (low + high) / 2;
               int mid2 = (n + m + 1) / 2 - mid1;
               int l1 = (mid1 == 0) ? Integer.MIN_VALUE : nums1[mid1 - 1];
               int r1 = (mid1 == n) ? Integer.MAX_VALUE : nums1[mid1];
               int l2 = (mid2 == 0) ? Integer.MIN_VALUE : nums2[mid2 - 1];
               int r2 = (mid2 == m) ? Integer.MAX_VALUE : nums2[mid2];
               if (l1 <= r2 && l2 <= r1) {
                    if ((n + m) % 2 == 1)
                         return Math.max(l1, l2);
                    else
                         return (Math.max(l1, l2) + Math.min(r1, r2)) / 2.0;
               } else if (l1 > r2)
                    high = mid1 - 1;
               else
                    low = mid1 + 1;
          }
          return 0.0; // Should not reach here with valid input
     }

     public static void main(String[] args) {
          Solution solution = new Solution();
          // Test Problem 1
          int[] nums1_1 = { 1, 3 };
          int[] nums1_2 = { 2, 4, 5 };
          System.out.println(
                    "Median of arrays [1, 3] and [2, 4, 5]: " + solution.findMedianSortedArrays(nums1_1, nums1_2));
          // Test Problem 2
          int[] nums2_1 = { 1, 2 };
          int[] nums2_2 = { 3, 4 };
          System.out.println(
                    "Median of arrays [1, 2] and [3, 4]: " + solution.findMedianSortedArrays(nums2_1, nums2_2));
     }
}
