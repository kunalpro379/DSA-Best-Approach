package Kadane;

/*
 * psudo code
 * int findMaxSubArraySum(int[] arr, int n){
 * int maxSumSoFar=arr[0];
 * int maxEndingHere=arr[0];
 * for(int i=0;i<n;i++){
 * maxEndingHere=max(
 * arr[i]+maxEndingHere, arr[i]
 * ); * 
 * }
 * if(maxSoFar<maxEndingHere)maxSumSoFar=maxEndingHere;
 * return maxSumSoFar;
 * }
 */
public class Kadane {
     /*
      * Maximum Subarray Sum
      */
     public static int maxSubArraySum(int[] arr, int n) {
          int maxSumSoFar = arr[0];
          int maxEndingHere = arr[0];
          for (int i = 1; i < n; i++) {
               maxEndingHere = Math.max(arr[i] + maxEndingHere, arr[i]);
               maxSumSoFar = Math.max(maxSumSoFar, maxEndingHere);
          }
          return maxSumSoFar;
     }

     public static int Print_that_Subarray_withmaxSubArraySum(int[] arr, int n) {
          int maxSumSoFar = arr[0];
          int maxEndingHere = arr[0];
          int start = 0;
          int end = 0;
          int tempStart = 0;
          for (int i = 1; i < n; i++) {
               if (arr[i] > arr[i] + maxEndingHere) {
                    maxEndingHere = arr[i];
                    tempStart = i;
               } else {
                    maxEndingHere = arr[i] + maxEndingHere;
               }
               if (maxSumSoFar < maxEndingHere) {
                    maxSumSoFar = maxEndingHere;
                    start = tempStart;
                    end = i;
               }

          }
          System.out.println("Subarray with maximum sum:");
          for (int i = start; i <= end; i++) {
               System.out.print(arr[i] + " ");
          }
          System.out.println();
          return maxSumSoFar;
     }

     public static int kadane(int[] arr) {
          int maxSumSoFar = arr[0];
          int maxEndingHere = arr[0];

          for (int i = 1; i < arr.length; i++) {
               maxEndingHere = Math.max(maxEndingHere + arr[i], arr[i]);
               maxSumSoFar = Math.max(maxSumSoFar, maxEndingHere);
          }
          return maxSumSoFar;
     }

     /*
      * 
      * Maximum Circular Subarray Sum
      */
     public static int maxCircularSubArraySum(int[] arr) {
          // max sum of normal subarray
          int maxSum = kadane(arr);
          // if all the numbers negetive then return maxSum
          if (maxSum < 0)
               return maxSum;
          // invert the array
          int[] invertedArray = new int[arr.length];
          for (int i = 0; i < arr.length; i++) {
               invertedArray[i] = -arr[i];
          }
          // find max subarry sum of inverted Array
          int maxInvertedSum = kadane(invertedArray);
          // Add the sum of original array to get circular sum
          int totalSum = 0;
          for (int num : arr)
               totalSum += num;
          int maxCircularSum = totalSum + maxInvertedSum;
          return Math.max(maxSum, maxCircularSum);
     }

     public static void main(String[] args) {
          int[] arr = { -2, -3, 4, -1, -2, 1, 5, -3 };
          int n = arr.length;
          System.out.println("Maximum Subarray Sum:");
          System.out.println(maxSubArraySum(arr, n));
          System.out.println(Print_that_Subarray_withmaxSubArraySum(arr, n));
          System.out.println("Maximum Circular Subarray Sum:");
          System.out.println(maxCircularSubArraySum(arr));
     }
}
