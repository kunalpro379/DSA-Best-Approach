# Search in a Rotated Sorted Array

## Introduction

Searching in a rotated sorted array is a classic algorithmic problem that involves finding a target element in an array that was originally sorted in ascending order but has been rotated at some unknown pivot point. A rotated sorted array can be thought of as two sorted subarrays placed end-to-end. For example, the array `[4, 5, 6, 7, 0, 1, 2]` is a rotation of the sorted array `[0, 1, 2, 4, 5, 6, 7]`. The challenge is to efficiently locate a target value in such an array, leveraging the partial sorted nature of the data to achieve better than linear time complexity.

## Algorithm Explanation

The most efficient approach to search in a rotated sorted array is to use a modified binary search algorithm. Since the array is rotated, a standard binary search cannot be directly applied. However, we can adapt binary search to handle the rotation by determining which half of the array is sorted at each step and deciding which half to search based on the target value. There are two primary methods to solve this problem:

1. **Using Binary Search Twice**: First, find the pivot (the smallest element or rotation point) using binary search. Then, perform a second binary search in the appropriate sorted subarray based on the target value.
2. **Using Single Binary Search**: In a single pass, determine which half of the array is sorted by comparing the middle element with the endpoints, and then decide which half could contain the target based on its value.

The second approach (Single Binary Search) is generally more efficient as it avoids the overhead of finding the pivot separately.

### Steps for Single Binary Search:
1. Initialize two pointers, `low` and `high`, to the start and end of the array.
2. While `low <= high`:
   - Calculate the middle index `mid`.
   - If the middle element is the target, return its index.
   - Check if the left half (`low` to `mid`) is sorted by comparing `arr[low]` with `arr[mid]`.
     - If sorted and the target lies within this range (`arr[low] <= target < arr[mid]`), search the left half.
     - Otherwise, search the right half.
   - If the left half is not sorted, the right half (`mid` to `high`) must be sorted.
     - If the target lies within this range (`arr[mid] < target <= arr[high]`), search the right half.
     - Otherwise, search the left half.
3. If the target is not found, return -1.

This approach ensures that we always narrow down the search space by half in each iteration, maintaining the efficiency of binary search.

## Problems and Solutions

### Problem 1: Search for a Target in Rotated Sorted Array
**Problem Statement**: Given a rotated sorted array `arr` of distinct integers and a target value `target`, return the index of `target` if it is in the array, else return -1. You must write an algorithm with O(log n) time complexity.

**Example**:
- Input: `arr = [4, 5, 6, 7, 0, 1, 2]`, `target = 0`
- Output: 4
- Explanation: The target 0 is at index 4.

**Solution** (Using Single Binary Search):
```java
public class Solution {
    public int search(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            
            if (arr[mid] == target) {
                return mid;
            }
            
            // Check if left half is sorted
            if (arr[low] <= arr[mid]) {
                // Check if target is in left half
                if (arr[low] <= target && target < arr[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            // Right half is sorted
            else {
                // Check if target is in right half
                if (arr[mid] < target && target <= arr[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -1;
    }
}
```

**Explanation**:
- We use a single binary search to find the target.
- At each step, we determine which half is sorted by comparing `arr[low]` with `arr[mid]`.
- If the left half is sorted, we check if the target lies within the range of the left half. If it does, we search there; otherwise, we search the right half.
- If the right half is sorted, we perform a similar check for the right half.
- This ensures we always reduce the search space by half, achieving O(log n) time complexity.

### Problem 2: Search for a Target with Duplicates in Rotated Sorted Array
**Problem Statement**: Given a rotated sorted array `arr` of integers which may contain duplicates, and a target value `target`, return `true` if `target` is in the array, else return `false`. You must minimize the number of operations with O(log n) time complexity in the average case.

**Example**:
- Input: `arr = [2, 5, 6, 0, 0, 1, 2]`, `target = 0`
- Output: true
- Explanation: The target 0 is present in the array.

**Solution** (Handling Duplicates with Modified Binary Search):
```java
public class Solution {
    public boolean search(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            
            if (arr[mid] == target) {
                return true;
            }
            
            // Handle duplicates by skipping them
            if (arr[low] == arr[mid] && arr[mid] == arr[high]) {
                low++;
                high--;
            }
            // Check if left half is sorted
            else if (arr[low] <= arr[mid]) {
                // Check if target is in left half
                if (arr[low] <= target && target < arr[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            // Right half is sorted
            else {
                // Check if target is in right half
                if (arr[mid] < target && target <= arr[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return false;
    }
}
```

**Explanation**:
- This problem is more complex due to duplicates, which can confuse the determination of the sorted half.
- We add a check to handle the case where `arr[low]`, `arr[mid]`, and `arr[high]` are equal. In such cases, we cannot determine which half is sorted, so we increment `low` and decrement `high` to skip duplicates.
- For other cases, we proceed similarly to the first problem, checking which half is sorted and whether the target lies in that half.
- In the worst case (many duplicates), the time complexity could degrade to O(n), but in the average case, it remains O(log n).

## Time and Space Complexity Analysis

### Time Complexity:
- **Single Binary Search (Problem 1)**: O(log n), where n is the length of the array. At each step, the search space is halved, similar to a standard binary search, ensuring logarithmic time complexity.
- **Modified Binary Search with Duplicates (Problem 2)**: O(log n) in the average case. However, in the worst case (when there are many duplicates), it can degrade to O(n) because we might need to skip duplicates one by one.

### Space Complexity:
- Both solutions have O(1) space complexity as they use only a constant amount of extra space regardless of the input size. The algorithm operates in-place using just a few variables (`low`, `high`, `mid`).

## Optimizations

1. **Avoiding Two-Pass Approach**: Instead of finding the pivot first and then searching in the appropriate subarray (which requires two binary searches), the single-pass binary search approach directly determines the sorted half and searches accordingly. This reduces the constant factor in the time complexity.
2. **Handling Duplicates Efficiently**: In the presence of duplicates, skipping duplicates at the boundaries (`low` and `high`) when they match `mid` helps maintain the logarithmic time complexity in most cases. However, care must be taken to avoid unnecessary skips that could lead to missing the target.
3. **Edge Cases**: Handle edge cases such as an empty array or an array with one element by returning appropriate results immediately. Also, ensure that the algorithm correctly handles cases where the array is not rotated (fully sorted) or fully rotated (pivot at the start).
4. **Mid Calculation**: Use `mid = low + (high - low) / 2` instead of `(low + high) / 2` to prevent integer overflow in languages like Java for very large arrays.

## Conclusion

Searching in a rotated sorted array is an excellent problem to understand the power of binary search and how it can be adapted to non-standard scenarios. By leveraging the property that at least one half of the array is always sorted, we can achieve an efficient O(log n) solution. The presence of duplicates adds complexity, but with careful handling, the average-case performance remains optimal. These techniques are widely applicable in real-world scenarios where data might be partially ordered or rotated due to operations like circular buffers or log rotations.
