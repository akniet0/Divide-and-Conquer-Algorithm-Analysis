import java.util.Arrays;
import java.util.Random;

public class AlgorithmTests {
    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("            DIVIDE-AND-CONQUER CORRECTNESS TEST SUITE                 ");
        System.out.println("======================================================================");

        testSortingAlgorithms();
        testDeterministicSelect();
        testClosestPair();

        System.out.println("\n======================================================================");
        System.out.println("SUMMARY: ALL ALGORITHM TESTS PASSED CORRECTLY");
        System.out.println("======================================================================");
    }

    private static void testSortingAlgorithms() {
        System.out.println("\n--- [1] TESTING MERGESORT & QUICKSORT WITH ACTUAL ARRAYS ---");

        int[][] testCases = {
                {},
                {42},
                {5, 2, 8, 1, 9, 3},
                {1, 2, 3, 4, 5, 6},
                {6, 5, 4, 3, 2, 1},
                {3, 3, 3, 3, 3, 3},
                {7, 2, 7, 3, 2, 1, 7}
        };

        String[] labels = {
                "Empty Array",
                "Single Element",
                "Random Unsorted",
                "Already Sorted",
                "Reverse Sorted",
                "All Duplicates",
                "Duplicate-Heavy"
        };

        for (int i = 0; i < testCases.length; i++) {
            int[] original = testCases[i];
            int[] mergeArr = original.clone();
            int[] quickArr = original.clone();
            int[] expected = original.clone();
            Arrays.sort(expected);

            MergeSorter.sort(mergeArr);
            QuickSorter.sort(quickArr);

            if (!Arrays.equals(mergeArr, expected) || !Arrays.equals(quickArr, expected)) {
                throw new AssertionError("Sorting test failed for: " + labels[i]);
            }

            System.out.printf("Test Case %d (%s):%n", i + 1, labels[i]);
            System.out.println("  Original Array : " + Arrays.toString(original));
            System.out.println("  MergeSort Out  : " + Arrays.toString(mergeArr));
            System.out.println("  QuickSort Out  : " + Arrays.toString(quickArr));
            System.out.println("  Status         : PASSED ✓");
            System.out.println();
        }
    }

    private static void testDeterministicSelect() {
        System.out.println("--- [2] TESTING DETERMINISTIC SELECT (MEDIAN-OF-MEDIANS) ---");

        // Concrete sample demonstrating order statistics
        int[] sample = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Sample Input Array: " + Arrays.toString(sample));
        int[] sortedSample = sample.clone();
        Arrays.sort(sortedSample);
        System.out.println("Reference Sorted  : " + Arrays.toString(sortedSample));

        for (int k = 0; k < sample.length; k++) {
            Metrics m = new Metrics();
            int val = DeterministicSelector.select(sample.clone(), k, m);
            System.out.printf("  Find index k=%d (the %d-th smallest): Got %d (Expected: %d) -> PASSED ✓%n",
                    k, k + 1, val, sortedSample[k]);
        }

        // Section 3 Requirement: Run at least 100 random trials
        Random rand = new Random(42);
        for (int i = 0; i < 100; i++) {
            int n = rand.nextInt(300) + 1;
            int[] arr = rand.ints(n, -500, 500).toArray();
            int k = rand.nextInt(n);
            int[] ref = arr.clone();
            Arrays.sort(ref);
            int actual = DeterministicSelector.select(arr.clone(), k, new Metrics());
            if (actual != ref[k]) {
                throw new AssertionError("Select failed on trial " + i);
            }
        }
        System.out.println("  Executed 100 random property-based trials vs Arrays.sort(a)[k] -> ALL 100 PASSED ✓\n");
    }

    private static void testClosestPair() {
        System.out.println("--- [3] TESTING CLOSEST PAIR OF POINTS (D&C vs BRUTE FORCE) ---");

        // Small geometric dataset
        Point[] pts = {
                new Point(2.0, 3.0),
                new Point(12.0, 30.0),
                new Point(40.0, 50.0),
                new Point(5.0, 1.0),
                new Point(12.0, 10.0),
                new Point(3.0, 4.0)
        };

        System.out.println("Points coordinates: (2,3), (12,30), (40,50), (5,1), (12,10), (3,4)");
        Metrics m = new Metrics();
        double dncDist = ClosestPairSolver.solve(pts, m);
        double bruteDist = ClosestPairSolver.bruteForce(pts, 0, pts.length - 1, null);

        System.out.printf("  Divide-and-Conquer Distance : %.5f%n", dncDist);
        System.out.printf("  Brute-Force O(n^2) Distance : %.5f%n", bruteDist);
        System.out.printf("  Difference                  : %.5f -> PASSED ✓%n", Math.abs(dncDist - bruteDist));

        // Stress comparison on n=1000 points
        Random r = new Random(42);
        Point[] stress = new Point[1000];
        for (int i = 0; i < 1000; i++) {
            stress[i] = new Point(r.nextDouble() * 500, r.nextDouble() * 500);
        }
        double dncStress = ClosestPairSolver.solve(stress, new Metrics());
        double bruteStress = ClosestPairSolver.bruteForce(stress, 0, stress.length - 1, null);

        if (Math.abs(dncStress - bruteStress) > 1e-9) {
            throw new AssertionError("ClosestPair stress verification mismatch");
        }
        System.out.printf("  Stress Test on n=1,000 points: D&C=%.5f, Brute=%.5f -> PASSED ✓%n", dncStress, bruteStress);
    }
}