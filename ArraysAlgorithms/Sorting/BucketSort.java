package Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketSort {
     public void bucketSort(int[] arr, int bucketSize) {
          if (arr.length == 0)
               return;
          int min = arr[0];
          int max = arr[0];
          for (int num : arr) {
               if (num < min)
                    min = num;
               if (num > max)
                    max = num;
          }
          int bucketCount = (max - min) / bucketSize + 1;
          List<List<Integer>> buckets = new ArrayList<>(bucketCount);
          for (int i = 0; i < bucketCount; i++) {
               buckets.add(new ArrayList<>());
          }
          for (int num : arr) {
               buckets.get((num - min) / bucketSize).add(num);
          }
          int idx = 0;
          for (List<Integer> bucket : buckets) {
               Collections.sort(bucket);
               for (int num : bucket) {
                    arr[idx++] = num;
               }
          }
     }
}
