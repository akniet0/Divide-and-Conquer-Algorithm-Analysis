public class DeterministicSelector {
    public long comparisons;
    public int maxDepth;

    // Returns the k-th smallest element (0-based). Rearranges the array.
    public int select(int[] a, int k) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        comparisons = 0;
        maxDepth = 0;
        return select(a, 0, a.length - 1, k, 1);
    }

    private int select(int[] a, int lo, int hi, int k, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        if (hi - lo + 1 <= 5) {
            insertionSort(a, lo, hi);
            return a[k];
        }
        int pivot = medianOfMedians(a, lo, hi, depth);

        int lt = lo, gt = hi, i = lo;
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

        if (k < lt) return select(a, lo, lt - 1, k, depth + 1);
        if (k > gt) return select(a, gt + 1, hi, k, depth + 1);
        return pivot;
    }

    private int medianOfMedians(int[] a, int lo, int hi, int depth) {
        int groups = (hi - lo + 5) / 5;
        for (int g = 0; g < groups; g++) {
            int start = lo + g * 5;
            int end = Math.min(start + 4, hi);
            insertionSort(a, start, end);
            swap(a, lo + g, start + (end - start) / 2);
        }
        return select(a, lo, lo + groups - 1, lo + (groups - 1) / 2, depth + 1);
    }

    private void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= lo) {
                comparisons++;
                if (a[j] <= key) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
