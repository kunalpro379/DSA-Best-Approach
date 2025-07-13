package RotateArrayByK;
public class RotateArrayByK {
     // TC :O(n)
     // SC: O(1)
     public static void rotateNaive(int[] arr, int k) {
          int n = arr.length;
          if (n == 0)
               return;
          k = k % n;// if k>n

          if (k < 0)
               k = k + n;

          // copy last k elements to the start of the temp array
          int[] temp = new int[k];
          for (int i = 0; i < k; i++)
               temp[i] = arr[n - k + i];
          for (int i = k; i < n; i++)
               temp[i] = arr[i - k];
          for (int i = 0; i < n; i++)
               arr[i] = temp[i];
     }

     public static void rotateOptimized(int[] arr, int k) {
          int n = arr.length;
          k = k % n;
          reverse(arr, 0, n - 1);// entrire array reverse;
          reverse(arr, 0, k - 1);// first k elements reverse;
          reverse(arr, k, n - 1);// last n-k elements reverse;
     }

     public static void reverse(int[] arr, int start, int end) {
          while (start < end) {
               int temp = arr[start];
               arr[start] = arr[end];
               arr[end] = temp;
               start++;
               end--;
          }
     }

     public static void main(String[] args) {
          int[] arr1 = { 1, 2, 3, 4, 5 };
          int k1 = 2;
          // System.out.println("Original Array (Naive): ");
          // printArray(arr1);
          // int[] arr1Copy = arr1.clone();
          // System.out.println("Copy of array for naive rotation: ");
          // printArray(arr1Copy);
          // rotateNaive(arr1Copy, k1);
          // System.out.println("Rotated Array by " + k1 + " (Naive): ");
          // printArray(arr1Copy);

          int[] arr2 = { 1, 2, 3, 4, 5 };
          int k2 = 2;
          System.out.println("\nOriginal Array (Optimized): ");
          printArray(arr2);
          rotateOptimized(arr2, k2);
          System.out.println("Rotated Array by " + k2 + " (Optimized): ");
          printArray(arr2);
     }

     private static void printArray(int[] arr) {
          for (int num : arr) {
               System.out.print(num + " ");
          }
          System.out.println();
     }
}
