import java.util.Random;

public class QuickSorter {
    public long comparisons;
    public int maxDepth;
    private final Random random = new Random();
    private int lt, gt;

    public void sort(int[] a) {
        comparisons = 0;
        maxDepth = 0;
        sort(a, 0, a.length - 1, 1);
    }

    private void sort(int[] a, int lo, int hi, int depth) {
        while (lo < hi) {
            maxDepth = Math.max(maxDepth, depth);
            partition(a, lo, hi);
            int left = lt, right = gt;
            if (left - lo < hi - right) {
                sort(a, lo, left - 1, depth + 1);
                lo = right + 1;
            } else {
                sort(a, right + 1, hi, depth + 1);
                hi = left - 1;
            }
        }
    }

    // Three-way partition: a[lo..lt-1] < pivot, a[lt..gt] == pivot, a[gt+1..hi] > pivot
    private void partition(int[] a, int lo, int hi) {
        swap(a, lo, lo + random.nextInt(hi - lo + 1));
        int pivot = a[lo];
        lt = lo;
        gt = hi;
        int i = lo + 1;
        while (i <= gt) {
            comparisons++;
            if (a[i] < pivot) {
                swap(a, lt++, i++);
            } else if (a[i] > pivot) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
