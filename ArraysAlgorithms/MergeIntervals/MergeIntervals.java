import java.util.*;

public class MergeIntervals {
     // TC: O(nlogn)
     // SC: O(1)
     public static List<int[]> mergeStandard(int[][] intervals) {
          if (intervals.length <= 1) {
               List<int[]> res = new ArrayList<>();
               for (int[] interval : intervals)
                    res.add(interval);
               return res;
          }
          // SORT intervals based on start time
          Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
          List<int[]> merged = new ArrayList<>();
          int[] currentInterval = intervals[0];
          merged.add(currentInterval);
          // iterate throught the sorted intervals
          for (int i = 1; i < intervals.length; i++) {
               int[] interval = intervals[i];
               // if current interval's end is greater than
               // or equal to next Intervals start
               if (currentInterval[1] >= interval[0])
                    currentInterval[1] = Math.max(currentInterval[1], interval[1]);
               else {
                    currentInterval = interval;
                    merged.add(currentInterval);
               }
          }
          return merged;
     }

     public static List<int[]> mergeAllIntoOne(int[][] intervals) {
          if (intervals.length == 0)
               return new ArrayList<>();
          int minStart = intervals[0][0];
          int maxEnd = intervals[0][1];
          for (int[] interval : intervals) {
               minStart = Math.min(minStart, interval[0]);
               maxEnd = Math.max(maxEnd, interval[1]);
          }
          List<int[]> result = new ArrayList<>();
          result.add(new int[] { minStart, maxEnd });
          return result;
     }

     public static void main(String[] args) {
          int[][] intervals1 = { { 1, 3 }, { 2, 6 }, { 8, 10 }, { 15, 18 }, { 16, 20 }, { 19, 21 }, { 22, 25 } };
          int[][] intervals2 = { { 1, 4 }, { 5, 6 }, { 7, 9 } };
          int[][] intervals3 = { { 1, 4 }, { 2, 3 }, { 3, 5 } };
          // printIntervals(intervals1);
          // printIntervals(intervals2);
          // printIntervals(intervals3);
          System.out.println("Test Case 1 (Standard Merge):");
          System.out.println("Input Intervals:");
          printIntervals(intervals1);
          List<int[]> result1 = mergeStandard(intervals1);
          System.out.println("Merged Intervals:");
          printIntervals(result1);

          System.out.println("\nTest Case 2 (Standard Merge):");
          System.out.println("Input Intervals:");
          printIntervals(intervals2);
          List<int[]> result2 = mergeStandard(intervals2);
          System.out.println("Merged Intervals (Standard):");
          printIntervals(result2);
          System.out.println("Merged Intervals (All into One):");
          List<int[]> result2All = mergeAllIntoOne(intervals2);
          printIntervals(result2All);

          System.out.println("\nTest Case 3 (Standard Merge):");
          System.out.println("Input Intervals:");
          printIntervals(intervals3);
          List<int[]> result3 = mergeStandard(intervals3);
          System.out.println("Merged Intervals:");
          printIntervals(result3);

     }

     private static void printIntervals(List<int[]> intervals) {
          for (int[] interval : intervals) {
               System.out.print("[" + interval[0] + ", " + interval[1] + "] ");
          }
          System.out.println();
     }

     private static void printIntervals(int[][] intervals) {
          for (int[] interval : intervals) {
               System.out.print("[" + interval[0] + ", " + interval[1] + "] ");
          }
          System.out.println();
     }
}
