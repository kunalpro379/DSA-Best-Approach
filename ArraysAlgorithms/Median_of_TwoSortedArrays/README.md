# Median of Two Sorted Arrays

## Introduction

Finding the median of two sorted arrays is a classic algorithmic problem that involves determining the middle value(s) of the combined sorted array without necessarily merging the arrays. This problem is particularly interesting because it can be solved efficiently using binary search, achieving logarithmic time complexity. The task is to handle both cases where the arrays are of the same size and different sizes, and to account for whether the total number of elements is odd or even.

## Explanation of the Algorithm

The most efficient approach to find the median of two sorted arrays is using Binary Search, which avoids the need to merge the arrays explicitly and achieves a time complexity of O(log(min(n, m))), where n and m are the lengths of the two arrays. The key idea is to partition the arrays into two halves such that the left half contains elements smaller than or equal to the right half, and the partition ensures the median can be calculated from the boundary elements.

### Binary Search Approach
- **Concept**: Partition the smaller array (to optimize time complexity) into two parts using binary search, and calculate the corresponding partition in the larger array such that the left parts of both arrays are less than or equal to the right parts. The median is then derived from the boundary elements of these partitions.
- **Steps**:
  1. Ensure the first array is the smaller one to optimize binary search. If not, swap the arrays.
  2. Use binary search to find a partition point `mid1` in the smaller array (range from 0 to length of smaller array).
  3. Calculate the corresponding partition point `mid2` in the larger array as `(total_length + 1) / 2 - mid1` to balance the left half.
  4. Determine the left and right boundary values for both partitions:
     - For the smaller array: `l1` (left of partition) and `r1` (right of partition).
     - For the larger array: `l2` (left of partition) and `r2` (right of partition).
     - Handle edge cases by using `INT_MIN` or `INT_MAX` if partitions are at the start or end.
  5. Check if the partition is valid: `l1 <= r2` and `l2 <= r1`.
     - If valid, calculate the median:
       - If total length is odd, median is `max(l1, l2)`.
       - If total length is even, median is `(max(l1, l2) + min(r1, r2)) / 2`.
     - If not valid, adjust the binary search range:
       - If `l1 > r2`, move left in the smaller array (`high = mid1 - 1`).
       - If `l2 > r1`, move right in the smaller array (`low = mid1 + 1`).
- **Pros**: Achieves logarithmic time complexity, very efficient for large arrays.
- **Cons**: Complex to implement due to edge cases and partition logic.

## Problems and Solutions

### Problem 1: Median of Two Sorted Arrays of Different Sizes (Odd Total Length)
- **Description**: Given two sorted arrays of different sizes, find the median of the merged array. The total number of elements is odd.
- **Example**:
  - Input: `nums1 = [1, 3]`, `nums2 = [2, 4, 5]`
  - Output: `3`
- **Solution using Binary Search**:
  ```java
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
              if ((n + m) % 2 == 1) return Math.max(l1, l2);
              else return (Math.max(l1, l2) + Math.min(r1, r2)) / 2.0;
          } else if (l1 > r2) high = mid1 - 1;
          else low = mid1 + 1;
      }
      return 0.0; // Should not reach here with valid input
  }
  ```
- **Explanation**: The binary search partitions `nums1` and `nums2` to find the correct split where the left half elements are less than or equal to the right half. For the input arrays, the total length is 5 (odd), so the median is the middle element, which is 3 from `nums1`.

### Problem 2: Median of Two Sorted Arrays of Different Sizes (Even Total Length)
- **Description**: Given two sorted arrays of different sizes, find the median of the merged array. The total number of elements is even.
- **Example**:
  - Input: `nums1 = [1, 2]`, `nums2 = [3, 4]`
  - Output: `2.5`
- **Solution using Binary Search**:
  ```java
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
              if ((n + m) % 2 == 1) return Math.max(l1, l2);
              else return (Math.max(l1, l2) + Math.min(r1, r2)) / 2.0;
          } else if (l1 > r2) high = mid1 - 1;
          else low = mid1 + 1;
      }
      return 0.0; // Should not reach here with valid input
  }
  ```
- **Explanation**: The binary search finds the partition where the left half ends with 2 from `nums1` and 3 from `nums2` as the middle elements of the merged array. Since the total length is 4 (even), the median is the average of 2 and 3, which is 2.5.

## Time and Space Complexity Analysis

### Binary Search Approach
- **Time Complexity**: O(log(min(n, m))), where n and m are the lengths of the two arrays. Binary search is performed on the smaller array, halving the search space in each iteration.
- **Space Complexity**: O(1), as the algorithm uses only a constant amount of extra space regardless of input size.

### Alternative Approaches (for comparison)
1. **Naive Merging and Sorting**:
   - **Time Complexity**: O((n + m) log (n + m)) due to sorting the merged array.
   - **Space Complexity**: O(n + m) for storing the merged array.
2. **Merge without Sorting (using Merge Sort's merge process)**:
   - **Time Complexity**: O(n + m) to merge the arrays up to the median point.
   - **Space Complexity**: O(1) if we only track the median elements without storing the merged array.

## Optimizations

1. **Choosing Smaller Array for Binary Search**: Always perform binary search on the smaller array to minimize the number of iterations, ensuring the time complexity is O(log(min(n, m))).
2. **Handling Edge Cases**: Use `Integer.MIN_VALUE` and `Integer.MAX_VALUE` to handle cases where partitions are at the start or end of arrays, avoiding index out of bounds errors.
3. **Early Termination**: If the arrays are of significantly different sizes, the binary search quickly narrows down to the correct partition, making the algorithm very efficient in practice.
4. **Avoiding Full Merge**: By focusing on finding the partition rather than merging the entire arrays, we save significant computational resources, especially for large inputs.

## Conclusion

Finding the median of two sorted arrays is a problem that elegantly demonstrates the power of binary search in achieving efficient solutions. The binary search approach not only reduces the time complexity to logarithmic but also handles various edge cases like different array sizes and odd/even total lengths. This problem is a great example of how understanding the properties of sorted data can lead to optimized algorithms, avoiding unnecessary operations like full merging or sorting. Mastery of this technique is valuable for tackling similar problems involving order statistics in sorted datasets.
