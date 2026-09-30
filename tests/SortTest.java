import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SortTest {
    private final Random random = new Random(1);

    private void check(int[] input) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        int[] merged = input.clone();
        new MergeSorter().sort(merged);
        assertArrayEquals(expected, merged);

        int[] quick = input.clone();
        new QuickSorter().sort(quick);
        assertArrayEquals(expected, quick);
    }

    @Test
    void emptyArray() {
        check(new int[0]);
    }

    @Test
    void singleElement() {
        check(new int[]{7});
    }

    @Test
    void twoElements() {
        check(new int[]{2, 1});
    }

    @Test
    void randomArrays() {
        for (int n : new int[]{2, 10, 17, 100, 1000, 10_000}) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = random.nextInt(1000) - 500;
            check(a);
        }
    }

    @Test
    void sortedArray() {
        int[] a = new int[5000];
        for (int i = 0; i < a.length; i++) a[i] = i;
        check(a);
    }

    @Test
    void reverseSortedArray() {
        int[] a = new int[5000];
        for (int i = 0; i < a.length; i++) a[i] = a.length - i;
        check(a);
    }

    @Test
    void duplicateHeavyArray() {
        int[] a = new int[5000];
        for (int i = 0; i < a.length; i++) a[i] = random.nextInt(5);
        check(a);
    }

    @Test
    void allEqualArray() {
        int[] a = new int[5000];
        Arrays.fill(a, 3);
        check(a);
    }
}
