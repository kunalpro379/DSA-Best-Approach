package KthSmallestElement;

import java.util.PriorityQueue;

public class KthSmallestElement {
     // Solution for Problem 1: Kth Smallest Element in an Unsorted Array using
     // QuickSelect
     public int findKthSmallest(int[] arr, int k) {
          if (arr == null || k < 1 || k > arr.length) {
               throw new IllegalArgumentException("Invalid input");
          }
          return quickSelect(arr, 0, arr.length - 1, k - 1);
     }

     private int quickSelect(int[] arr, int left, int right, int k) {
          if (left == right) {
               return arr[left];
          }
          int pivotIndex = partition(arr, left, right);
          if (k == pivotIndex) {
               return arr[k];
          } else if (k < pivotIndex) {
               return quickSelect(arr, left, pivotIndex - 1, k);
          } else {
               return quickSelect(arr, pivotIndex + 1, right, k);
          }
     }

     private int partition(int[] arr, int left, int right) {
          int pivot = arr[right];
          int i = left - 1;
          for (int j = left; j < right; j++) {
               if (arr[j] <= pivot) {
                    i++;
                    swap(arr, i, j);
               }
          }
          swap(arr, i + 1, right);
          return i + 1;
     }

     private void swap(int[] arr, int i, int j) {
          int temp = arr[i];
          arr[i] = arr[j];
          arr[j] = temp;
     }

     // Solution for Problem 2: Kth Smallest Element in a Sorted Matrix using
     // Min-Heap
     public int kthSmallestInMatrix(int[][] matrix, int k) {
          int n = matrix.length;
          PriorityQueue<Integer> minHeap = new PriorityQueue<>();
          for (int i = 0; i < n; i++) {
               for (int j = 0; j < n; j++) {
                    minHeap.offer(matrix[i][j]);
               }
          }
          for (int i = 0; i < k - 1; i++) {
               minHeap.poll();
          }
          return minHeap.poll();
     }
}
