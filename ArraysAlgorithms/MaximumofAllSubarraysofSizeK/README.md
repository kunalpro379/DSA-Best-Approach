# Maximum of All Subarrays of Size K

## Introduction

Finding the maximum of all subarrays of size K is a common problem in algorithmic programming, often encountered in scenarios like signal processing, image processing, and data analysis. The task is to identify the largest element in each contiguous subarray of a fixed size K within a given array. This problem can be efficiently solved using the sliding window technique, with the most optimal approach utilizing a deque (double-ended queue) to maintain the indices of potential maximum elements in the current window.

## What is the Sliding Window Maximum Algorithm?

The Sliding Window Maximum algorithm aims to find the maximum element in each contiguous subarray of size K as the window slides over the array from left to right. The naive approach involves iterating through each subarray of size K and finding its maximum, but this is inefficient for large arrays. The optimal solution uses a deque to keep track of indices of elements in the current window, maintaining them in decreasing order of their values. This ensures that the front of the deque always holds the index of the maximum element in the current window.

### Algorithm Steps (Using Deque)
1. **Initialize a Deque:** Create a deque to store indices of elements in the current window. The deque will maintain elements in decreasing order of their values.
2. **Process First K Elements:** For the first K elements of the array:
   - Remove indices from the back of the deque if the corresponding elements are less than or equal to the current element (they cannot be the maximum).
   - Add the current index to the back of the deque.
3. **Slide the Window:** For the remaining elements:
   - The front of the deque holds the index of the maximum element of the previous window; record this maximum.
   - Remove the front index if it is out of the current window (i.e., index <= current index - K).
   - Remove indices from the back if the corresponding elements are less than or equal to the current element.
   - Add the current index to the back of the deque.
4. **Record Last Window's Maximum:** After the loop, record the maximum of the last window from the front of the deque.
5. **Return Results:** Return the list of maximum values for each window.

### Pseudocode
```cpp
vector<int> maxOfSubarrays(vector<int>& arr, int k) {
    vector<int> res;
    deque<int> dq;
    for (int i = 0; i < k; ++i) {
        while (!dq.empty() && arr[i] >= arr[dq.back()]) {
            dq.pop_back();
        }
        dq.push_back(i);
    }
    for (int i = k; i < arr.size(); ++i) {
        res.push_back(arr[dq.front()]);
        while (!dq.empty() && dq.front() <= i - k) {
            dq.pop_front();
        }
        while (!dq.empty() && arr[i] >= arr[dq.back()]) {
            dq.pop_back();
        }
        dq.push_back(i);
    }
    res.push_back(arr[dq.front()]);
    return res;
}
```

## Problems and Solutions

### Problem 1: Sliding Window Maximum
**Problem Statement:** Given an array of integers and an integer K, find the maximum value for each contiguous subarray of size K.

**Example:**
- Input: `arr[] = [1, 2, 3, 1, 4, 5, 2, 3, 6]`, K = 3
- Output: `[3, 3, 4, 5, 5, 5, 6]`
- Explanation:
  - 1st subarray `[1, 2, 3]` -> max = 3
  - 2nd subarray `[2, 3, 1]` -> max = 3
  - 3rd subarray `[3, 1, 4]` -> max = 4
  - 4th subarray `[1, 4, 5]` -> max = 5
  - 5th subarray `[4, 5, 2]` -> max = 5
  - 6th subarray `[5, 2, 3]` -> max = 5
  - 7th subarray `[2, 3, 6]` -> max = 6

**Solution (Java):**
```java
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public class SlidingWindowMaximum {
    public static ArrayList<Integer> maxOfSubarrays(int[] arr, int k) {
        int n = arr.length;
        ArrayList<Integer> res = new ArrayList<>();
        Deque<Integer> dq = new ArrayDeque<>();

        // Process first k elements
        for (int i = 0; i < k; ++i) {
            while (!dq.isEmpty() && arr[i] >= arr[dq.peekLast()]) {
                dq.pollLast();
            }
            dq.addLast(i);
        }

        // Process rest of the elements
        for (int i = k; i < n; ++i) {
            res.add(arr[dq.peekFirst()]);
            while (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }
            while (!dq.isEmpty() && arr[i] >= arr[dq.peekLast()]) {
                dq.pollLast();
            }
            dq.addLast(i);
        }

        // Add maximum of last window
        res.add(arr[dq.peekFirst()]);
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 4, 5, 2, 3, 6};
        int k = 3;
        ArrayList<Integer> res = maxOfSubarrays(arr, k);
        System.out.println("Maximum of each subarray of size " + k + ": " + res);
    }
}
```
**Output:** `Maximum of each subarray of size 3: [3, 3, 4, 5, 5, 5, 6]`

### Problem 2: Sum of Minimum and Maximum Elements of All Subarrays of Size K
**Problem Statement:** Given an array of integers and an integer K, compute the sum of minimum and maximum elements of all subarrays of size K.

**Example:**
- Input: `arr[] = {2, 5, -1, 7, -3, -1, -2}`, K = 4
- Output: `18`
- Explanation:
  - Subarray `{2, 5, -1, 7}` -> min + max = -1 + 7 = 6
  - Subarray `{5, -1, 7, -3}` -> min + max = -3 + 7 = 4
  - Subarray `{-1, 7, -3, -1}` -> min + max = -3 + 7 = 4
  - Subarray `{7, -3, -1, -2}` -> min + max = -3 + 7 = 4
  - Sum = 6 + 4 + 4 + 4 = 18

**Solution (Java):**
```java
import java.util.Deque;
import java.util.LinkedList;

public class SumOfMinMaxSubarrays {
    public static int sumOfKSubArray(int[] arr, int k) {
        int sum = 0;
        Deque<Integer> S = new LinkedList<>(); // for minimum
        Deque<Integer> G = new LinkedList<>(); // for maximum

        // Process first window of size K
        for (int i = 0; i < k; i++) {
            while (!S.isEmpty() && arr[S.peekLast()] >= arr[i]) {
                S.removeLast();
            }
            while (!G.isEmpty() && arr[G.peekLast()] <= arr[i]) {
                G.removeLast();
            }
            S.addLast(i);
            G.addLast(i);
        }

        // Process rest of the elements
        for (int i = k; i < arr.length; i++) {
            sum += arr[S.peekFirst()] + arr[G.peekFirst()];
            while (!S.isEmpty() && S.peekFirst() <= i - k) {
                S.removeFirst();
            }
            while (!G.isEmpty() && G.peekFirst() <= i - k) {
                G.removeFirst();
            }
            while (!S.isEmpty() && arr[S.peekLast()] >= arr[i]) {
                S.removeLast();
            }
            while (!G.isEmpty() && arr[G.peekLast()] <= arr[i]) {
                G.removeLast();
            }
            S.addLast(i);
            G.addLast(i);
        }

        // Sum of minimum and maximum of last window
        sum += arr[S.peekFirst()] + arr[G.peekFirst()];
        return sum;
    }

    public static void main(String[] args) {
        int[] arr = {2, 5, -1, 7, -3, -1, -2};
        int k = 4;
        System.out.println("Sum of minimum and maximum elements of subarrays of size " + k + ": " + sumOfKSubArray(arr, k));
    }
}
```
**Output:** `Sum of minimum and maximum elements of subarrays of size 4: 18`

## Detailed Analysis

### Time Complexity
- **Naive Approach (Nested Loops):** O(n * k), where n is the size of the array and k is the window size. For each of the (n-k+1) windows, it takes O(k) time to find the maximum.
- **Using Max-Heap (Priority Queue):** O(n * log n), as inserting and removing elements from a heap takes O(log n) time, and we perform these operations for n elements.
- **Optimal Approach (Deque):** O(n), where n is the size of the array. Each element is pushed and popped at most once from the deque, leading to amortized constant time per operation. This is the most efficient method.

### Space Complexity
- **Naive Approach (Nested Loops):** O(1), as it uses only a constant amount of extra space.
- **Using Max-Heap (Priority Queue):** O(n), due to the storage of elements in the heap.
- **Optimal Approach (Deque):** O(k), where k is the size of the window, as the deque stores at most k indices at any time.

### Optimizations
1. **Deque over Heap:** Using a deque is more efficient than a max-heap because it maintains a sorted order of potential maxima, allowing for O(1) amortized operations per element, compared to O(log n) for heap operations.
2. **Handling Edge Cases:**
   - **K = 1:** Each element is its own subarray, so the output is the array itself.
   - **K = n:** There is only one subarray, and the maximum is the largest element in the array.
   - **Empty Array or Invalid K:** Handle by returning an empty result or throwing an exception.
3. **Early Termination:** If the array contains only one unique value or is sorted in descending order, optimizations can be made to avoid unnecessary deque operations, though the general algorithm handles these cases efficiently.
4. **Memory Efficiency:** The deque approach minimizes space usage by only storing indices rather than values, and never more than K indices at a time.

### Comparison with Other Approaches
- **Nested Loops:** Simple to implement but inefficient for large arrays or large K, with O(n * k) time complexity.
- **Priority Queue (Max-Heap):** Better than nested loops with O(n * log n) time complexity, but still less efficient than deque and uses more space (O(n)).
- **Deque (Sliding Window):** The optimal solution with O(n) time complexity and O(k) space complexity, making it suitable for large datasets and real-time applications.

The deque-based sliding window approach is the preferred method due to its efficiency and scalability.

## Conclusion
Finding the maximum of all subarrays of size K is efficiently solved using the sliding window technique with a deque, achieving O(n) time complexity and O(k) space complexity. This approach is versatile and can be extended to solve related problems, such as finding sums of minimum and maximum elements in subarrays. Understanding and implementing this algorithm equips one to handle a variety of window-based problems in algorithmic programming.

For further practice, consider problems like finding the minimum of all subarrays of size K or applying sliding window techniques to other constraints, which build upon the principles discussed here.
