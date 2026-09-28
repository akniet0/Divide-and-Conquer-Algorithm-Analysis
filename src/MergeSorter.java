public class MergeSorter {
    private static final int CUTOFF = 16;

    public static Metrics sort(int[] a) {
        Metrics m = new Metrics();
        if (a == null || a.length <= 1) return m;
        int[] aux = new int[a.length];
        sort(a, aux, 0, a.length - 1, m);
        return m;
    }

    private static void sort(int[] a, int[] aux, int low, int high, Metrics m) {
        m.enter();
        if (high <= low + CUTOFF) {
            insertionSort(a, low, high, m);
            m.exit();
            return;
        }

        int mid = low + (high - low) / 2;
        sort(a, aux, low, mid, m);
        sort(a, aux, mid + 1, high, m);
        merge(a, aux, low, mid, high, m);
        m.exit();
    }

    private static void merge(int[] a, int[] aux, int low, int mid, int high, Metrics m) {
        System.arraycopy(a, low, aux, low, high - low + 1);

        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > high) {
                a[k] = aux[i++];
            } else {
                m.comparisons++;
                if (aux[j] < aux[i]) {
                    a[k] = aux[j++];
                } else {
                    a[k] = aux[i++];
                }
            }
        }
    }

    private static void insertionSort(int[] a, int low, int high, Metrics m) {
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
    }
}