import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.Locale;

public class Experiment {
    private static final int[] SIZES = {1_000, 5_000, 10_000, 50_000, 100_000, 500_000};
    private static final String[] TYPES = {"random", "sorted", "reverse", "duplicates"};
    private static final int RUNS = 5;

    private final Random random = new Random(42);

    public void run(String csvPath) throws IOException {
        try (PrintWriter out = new PrintWriter(csvPath)) {
            print(out, "algorithm,input,n,time_ms,max_depth,comparisons");
            for (int n : SIZES) {
                for (String type : TYPES) {
                    int[] data = generate(type, n);

                    MergeSorter merge = new MergeSorter();
                    measure(out, "MergeSort", type, n, data, merge::sort, () -> merge.maxDepth, () -> merge.comparisons);

                    QuickSorter quick = new QuickSorter();
                    measure(out, "QuickSort", type, n, data, quick::sort, () -> quick.maxDepth, () -> quick.comparisons);

                    DeterministicSelector select = new DeterministicSelector();
                    measure(out, "Select", type, n, data, a -> select.select(a, a.length / 2),
                            () -> select.maxDepth, () -> select.comparisons);
                }
                measureClosestPair(out, n);
            }
        }
    }

    private void measure(PrintWriter out, String name, String type, int n, int[] data,
                         Consumer<int[]> algorithm, IntSupplier depth, LongSupplier comparisons) {
        algorithm.accept(data.clone());
        long total = 0;
        for (int r = 0; r < RUNS; r++) {
            int[] copy = data.clone();
            long start = System.nanoTime();
            algorithm.accept(copy);
            total += System.nanoTime() - start;
        }
        print(out, name, type, n, total, depth.getAsInt(), comparisons.getAsLong());
    }

    private void measureClosestPair(PrintWriter out, int n) {
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) points[i] = new Point(random.nextDouble() * 1e6, random.nextDouble() * 1e6);

        ClosestPairSolver solver = new ClosestPairSolver();
        solver.solve(points);
        long total = 0;
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            solver.solve(points);
            total += System.nanoTime() - start;
        }
        print(out, "ClosestPair", "random", n, total, solver.maxDepth, solver.distanceChecks);
    }

    private void print(PrintWriter out, String name, String type, int n, long totalNanos, int depth, long comparisons) {
        double ms = totalNanos / (double) RUNS / 1e6;
        print(out, String.format(Locale.US, "%s,%s,%d,%.3f,%d,%d", name, type, n, ms, depth, comparisons));
    }

    private void print(PrintWriter out, String line) {
        System.out.println(line);
        out.println(line);
    }

    private int[] generate(String type, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            switch (type) {
                case "sorted" -> a[i] = i;
                case "reverse" -> a[i] = n - i;
                case "duplicates" -> a[i] = random.nextInt(10);
                default -> a[i] = random.nextInt(1_000_000);
            }
        }
        return a;
    }
}
