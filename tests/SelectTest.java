import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SelectTest {
    private final Random random = new Random(2);

    @Test
    void matchesSortedArrayOnRandomInputs() {
        DeterministicSelector selector = new DeterministicSelector();
        for (int test = 0; test < 200; test++) {
            int n = 1 + random.nextInt(2000);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = random.nextInt(500);
            int k = random.nextInt(n);

            int[] sorted = a.clone();
            Arrays.sort(sorted);
            assertEquals(sorted[k], selector.select(a.clone(), k));
        }
    }

    @Test
    void handlesDuplicates() {
        int[] a = new int[1000];
        Arrays.fill(a, 4);
        assertEquals(4, new DeterministicSelector().select(a, 500));
    }

    @Test
    void singleElement() {
        assertEquals(9, new DeterministicSelector().select(new int[]{9}, 0));
    }

    @Test
    void invalidKThrows() {
        DeterministicSelector selector = new DeterministicSelector();
        assertThrows(IllegalArgumentException.class, () -> selector.select(new int[0], 0));
        assertThrows(IllegalArgumentException.class, () -> selector.select(new int[]{1, 2}, 2));
    }
}
