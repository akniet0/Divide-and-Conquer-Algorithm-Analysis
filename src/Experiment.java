import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Experiment {
    public static void runExperiments(String outputPath) throws IOException {
        File file = new File(outputPath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        int[] sizes = {100, 1_000, 5_000, 10_000, 50_000, 100_000};
        String[] types = {"RANDOM", "SORTED", "REVERSE", "DUPLICATE"};

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Algorithm,InputSize,InputType,ExecutionTimeNs,MaxDepth,Comparisons");

            for (int n : sizes) {
                for (String type : types) {
                    int[] data = generateData(n, type);

                    benchmarkSort("MergeSort", data.clone(), writer, type);
                    benchmarkSort("QuickSort", data.clone(), writer, type);

                    Metrics mSel = new Metrics();
                    long t0 = System.nanoTime();
                    DeterministicSelector.select(data.clone(), n / 2, mSel);
                    long t1 = System.nanoTime();
                    writer.printf("Select,%d,%s,%d,%d,%d\n", n, type, (t1 - t0), mSel.maxDepth, mSel.comparisons);
                }

                Point[] pts = generatePoints(n);
                Metrics mCp = new Metrics();
                long t0 = System.nanoTime();
                ClosestPairSolver.solve(pts, mCp);
                long t1 = System.nanoTime();
                writer.printf("ClosestPair,%d,2D-POINTS,%d,%d,%d\n", n, (t1 - t0), mCp.maxDepth, mCp.comparisons);
            }
        }
    }

    private static void benchmarkSort(String name, int[] a, PrintWriter writer, String type) {
        long t0 = System.nanoTime();
        Metrics m = name.equals("MergeSort") ? MergeSorter.sort(a) : QuickSorter.sort(a);
        long t1 = System.nanoTime();
        writer.printf("%s,%d,%s,%d,%d,%d\n", name, a.length, type, (t1 - t0), m.maxDepth, m.comparisons);
    }

    private static int[] generateData(int n, String type) {
        int[] a = new int[n];
        Random rand = new Random(1337);
        switch (type) {
            case "RANDOM":
                for (int i = 0; i < n; i++) a[i] = rand.nextInt();
                break;
            case "SORTED":
                for (int i = 0; i < n; i++) a[i] = i;
                break;
            case "REVERSE":
                for (int i = 0; i < n; i++) a[i] = n - i;
                break;
            case "DUPLICATE":
                for (int i = 0; i < n; i++) a[i] = rand.nextInt(10);
                break;
        }
        return a;
    }

    private static Point[] generatePoints(int n) {
        Point[] pts = new Point[n];
        Random r = new Random(1337);
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(r.nextDouble() * 10000, r.nextDouble() * 10000);
        }
        return pts;
    }
}