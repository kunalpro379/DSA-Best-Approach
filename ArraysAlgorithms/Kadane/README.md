# Kadane's Algorithm

## Introduction

Kadane's Algorithm is an efficient algorithm for solving the Maximum Subarray Sum problem. It finds the contiguous subarray within a one-dimensional array of numbers that has the largest sum. This algorithm is named after Jay Kadane, who published it as a simple yet powerful solution to this problem. Kadane's Algorithm is a prime example of dynamic programming due to its use of optimal substructures, solving the problem in linear time with constant space.

## What is Kadane's Algorithm?

Kadane's Algorithm works by iterating through the array and at each position, deciding whether to start a new subarray from the current element or extend the existing subarray sum from the previous position. The core idea is to maintain a running sum (`maxEndingHere`) that represents the maximum sum of the subarray ending at the current index. If this running sum becomes negative, it is reset to zero (or the current element, depending on the implementation), as including a negative sum would decrease the potential maximum sum of future subarrays.

The algorithm also keeps track of the overall maximum sum found so far (`maxSumSoFar`). At each step, it updates this value if the current running sum is larger.

### Algorithm Steps
1. Initialize two variables:
   - `maxSumSoFar` to store the maximum sum found so far (initialize with the first element).
   - `maxEndingHere` to store the maximum sum of the subarray ending at the current position (initialize with the first element).
2. Iterate through the array starting from the second element:
   - Update `maxEndingHere` by choosing the maximum of the current element alone or the sum of the current element and the previous `maxEndingHere`.
   - Update `maxSumSoFar` if `maxEndingHere` is greater than the current `maxSumSoFar`.
3. Return `maxSumSoFar` as the result.

### Pseudocode
```cpp
int findMaxSubarraySum(int arr[], int n) {
    int maxSumSoFar = arr[0];
    int maxEndingHere = arr[0];
    for (int i = 1; i < n; i++) {
        maxEndingHere = max(maxEndingHere + arr[i], arr[i]);
        if (maxSumSoFar < maxEndingHere) {
            maxSumSoFar = maxEndingHere;
        }
    }
    return maxSumSoFar;
}
```

## Problems and Solutions

### Problem 1: Maximum Subarray Sum
**Problem Statement:** Given an array of integers, find the subarray (containing at least one element) with the maximum possible sum, and return that sum.

**Example:**
- Input: `arr[] = [2, 3, -8, 7, -1, 2, 3]`
- Output: `11`
- Explanation: The subarray `[7, -1, 2, 3]` has the largest sum of 11.

**Solution (Java):**
```java
public class MaxSubarraySum {
    public static int maxSubarraySum(int[] arr) {
        int maxSumSoFar = arr[0];
        int maxEndingHere = arr[0];
        
        for (int i = 1; i < arr.length; i++) {
            maxEndingHere = Math.max(maxEndingHere + arr[i], arr[i]);
            maxSumSoFar = Math.max(maxSumSoFar, maxEndingHere);
        }
        return maxSumSoFar;
    }

    public static void main(String[] args) {
        int[] arr = {2, 3, -8, 7, -1, 2, 3};
        System.out.println("Maximum Subarray Sum: " + maxSubarraySum(arr));
    }
}
```
**Output:** `Maximum Subarray Sum: 11`

### Problem 2: Maximum Circular Subarray Sum
**Problem Statement:** Given a circular array of integers, find the maximum sum of any non-empty subarray. A circular array allows wrapping from the end back to the beginning.

**Example:**
- Input: `arr[] = [8, -8, 9, -9, 10, -11, 12]`
- Output: `22`
- Explanation: The subarray `[12, 8, -8, 9, -9, 10]` (wrapping around) has the largest sum of 22.

**Solution (Java):**
```java
public class MaxCircularSubarraySum {
    // Helper function to find max subarray sum using Kadane's Algorithm
    public static int kadane(int[] arr) {
        int maxSumSoFar = arr[0];
        int maxEndingHere = arr[0];
        
        for (int i = 1; i < arr.length; i++) {
            maxEndingHere = Math.max(maxEndingHere + arr[i], arr[i]);
            maxSumSoFar = Math.max(maxSumSoFar, maxEndingHere);
        }
        return maxSumSoFar;
    }

    public static int maxCircularSubarraySum(int[] arr) {
        // Case 1: Get the maximum subarray sum using standard Kadane's algorithm
        int maxNormal = kadane(arr);
        
        // If all numbers are negative, return the maximum element
        if (maxNormal < 0) {
            return maxNormal;
        }
        
        // Case 2: Get the maximum circular subarray sum
        // Step 1: Invert the array (change sign of each element)
        int[] invertedArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            invertedArr[i] = -arr[i];
        }
        
        // Step 2: Find the maximum subarray sum of inverted array
        int maxInverted = kadane(invertedArr);
        
        // Step 3: Add the total sum of original array to get circular sum
        int totalSum = 0;
        for (int num : arr) {
            totalSum += num;
        }
        int maxCircular = totalSum + maxInverted;
        
        // Return the maximum of normal and circular sum
        return Math.max(maxNormal, maxCircular);
    }

    public static void main(String[] args) {
        int[] arr = {8, -8, 9, -9, 10, -11, 12};
        System.out.println("Maximum Circular Subarray Sum: " + maxCircularSubarraySum(arr));
    }
}
```
**Output:** `Maximum Circular Subarray Sum: 22`

## Detailed Analysis

### Time Complexity
- **Kadane's Algorithm (Standard):** O(n), where n is the number of elements in the array. The algorithm performs a single pass through the array, making it highly efficient compared to brute force approaches (O(n²) or O(n³)).
- **Circular Subarray Sum:** O(n), as it involves two applications of Kadane's Algorithm (one on the original array and one on the inverted array) plus a single pass to compute the total sum, all of which are linear operations.

### Space Complexity
- **Kadane's Algorithm (Standard):** O(1), as it uses only a constant amount of extra space (two variables: `maxSumSoFar` and `maxEndingHere`), regardless of input size.
- **Circular Subarray Sum:** O(n), due to the creation of a temporary inverted array. However, this can be optimized to O(1) by computing the total sum and minimum subarray sum without storing the inverted array explicitly.

### Optimizations
1. **Handling All Negative Numbers:**
   - Kadane's Algorithm naturally handles arrays with all negative numbers by returning the largest (least negative) element, as `maxEndingHere` will reset to the current element if the sum becomes negative.
   - No additional checks are needed in the standard implementation, but for circular sum, a check is added to return the maximum element if all sums are negative.

2. **Finding Subarray Indices:**
   - To return the start and end indices of the maximum subarray, additional variables can track the start index of the current subarray and update the actual start and end indices whenever `maxSumSoFar` is updated.
   - Example modification:
     ```cpp
     int start = 0, end = 0, tempStart = 0;
     for (int i = 1; i < n; i++) {
         if (maxEndingHere + arr[i] < arr[i]) {
             maxEndingHere = arr[i];
             tempStart = i;
         } else {
             maxEndingHere += arr[i];
         }
         if (maxSumSoFar < maxEndingHere) {
             maxSumSoFar = maxEndingHere;
             start = tempStart;
             end = i;
         }
     }
     ```

3. **Space Optimization for Circular Sum:**
   - Instead of creating an inverted array, compute the minimum subarray sum directly using a modified Kadane's Algorithm (inverting the logic to find the minimum instead of maximum). Then, subtract this minimum sum from the total sum to get the maximum circular sum.
   - This reduces space complexity to O(1).

4. **Edge Cases:**
   - **Empty Array:** Handle by returning an appropriate value (e.g., 0 or throw an exception).
   - **Single Element:** The algorithm works correctly as both variables are initialized with the first element.
   - **All Zeros:** Returns 0, which is correct as it's the maximum possible sum.

### Comparison with Other Approaches
- **Brute Force (Three Nested Loops):** O(n³) time, O(1) space. Infeasible for large arrays.
- **Optimized Brute Force (Two Nested Loops):** O(n²) time, O(1) space. Still inefficient for large inputs.
- **Divide and Conquer:** O(n log n) time, O(log n) space. More complex and less efficient than Kadane's for this problem.
- **Dynamic Programming with Array:** O(n) time, O(n) space. Less space-efficient than Kadane's.

Kadane's Algorithm stands out due to its simplicity and efficiency, making it the preferred choice for the Maximum Subarray Sum problem.

## Conclusion
Kadane's Algorithm is a brilliant example of dynamic programming, offering an optimal solution to the Maximum Subarray Sum problem with O(n) time and O(1) space complexity. Its adaptability to variations like circular subarrays and ability to handle edge cases make it a versatile tool in algorithmic problem-solving. By understanding and implementing this algorithm, one can efficiently tackle a wide range of problems involving subarray sums.

For further practice, consider problems like finding the maximum product subarray or handling constraints like minimum subarray length, which build upon the principles of Kadane's Algorithm. 