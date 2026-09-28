public class DeterministicSelector {
    public static int select(int[] a, int k, Metrics m) {
        if (a == null || k < 0 || k >= a.length) {
            throw new IllegalArgumentException("Index k out of bounds.");
        }
        return select(a, 0, a.length - 1, k, m);
    }

    private static int select(int[] a, int low, int high, int k, Metrics m) {
        m.enter();
        if (low == high) {
            m.exit();
            return a[low];
        }

        int pivotValue = medianOfMedians(a, low, high, m);
        int pivotIndex = partition(a, low, high, pivotValue, m);

        int result;
        if (k == pivotIndex) {
            result = a[k];
        } else if (k < pivotIndex) {
            result = select(a, low, pivotIndex - 1, k, m);
        } else {
            result = select(a, pivotIndex + 1, high, k, m);
        }
        m.exit();
        return result;
    }

    private static int medianOfMedians(int[] a, int low, int high, Metrics m) {
        int n = high - low + 1;
        if (n <= 5) {
            return insertionSortMedian(a, low, high, m);
        }

        int numGroups = (int) Math.ceil((double) n / 5);
        for (int i = 0; i < numGroups; i++) {
            int gLow = low + i * 5;
            int gHigh = Math.min(gLow + 4, high);
            int medianVal = insertionSortMedian(a, gLow, gHigh, m);
            for (int j = gLow; j <= gHigh; j++) {
                if (a[j] == medianVal) {
                    swap(a, low + i, j);
                    break;
                }
            }
        }
        return select(a, low, low + numGroups - 1, low + numGroups / 2, m);
    }

    private static int partition(int[] a, int low, int high, int pivotVal, Metrics m) {
        for (int i = low; i <= high; i++) {
            if (a[i] == pivotVal) {
                swap(a, i, high);
                break;
            }
        }
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

    private static int insertionSortMedian(int[] a, int low, int high, Metrics m) {
        for (int i = low + 1; i <= high; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= low) {
                m.comparisons++;
                if (a[j] > key) {
                    a[j + 1] = a[j];
                    j--;
                } else {
                    break;
                }
            }
            a[j + 1] = key;
        }
        return a[low + (high - low) / 2];
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}