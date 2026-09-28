import java.util.concurrent.ThreadLocalRandom;

public class QuickSorter {
    public static Metrics sort(int[] a) {
        Metrics m = new Metrics();
        if (a == null || a.length <= 1) return m;
        sort(a, 0, a.length - 1, m);
        return m;
    }

    private static void sort(int[] a, int low, int high, Metrics m) {
        while (low < high) {
            m.enter();
            int pivotIndex = partition(a, low, high, m);

            if (pivotIndex - low < high - pivotIndex) {
                sort(a, low, pivotIndex - 1, m);
                low = pivotIndex + 1;
            } else {
                sort(a, pivotIndex + 1, high, m);
                high = pivotIndex - 1;
            }
            m.exit();
        }
    }

    private static int partition(int[] a, int low, int high, Metrics m) {
        int r = ThreadLocalRandom.current().nextInt(low, high + 1);
        swap(a, r, high);
        int pivot = a[high];

        int i = low;
        for (int j = low; j < high; j++) {
            m.comparisons++;
            if (a[j] < pivot) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, high);
        return i;
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}