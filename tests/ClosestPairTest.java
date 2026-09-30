import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;

class ClosestPairTest {
    private final Random random = new Random(3);

    private Point[] randomPoints(int n, int range) {
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) points[i] = new Point(random.nextInt(range), random.nextInt(range));
        return points;
    }

    @Test
    void matchesBruteForceOnRandomInputs() {
        ClosestPairSolver solver = new ClosestPairSolver();
        for (int test = 0; test < 100; test++) {
            Point[] points = randomPoints(2 + random.nextInt(1999), 100_000);
            assertEquals(ClosestPairSolver.bruteForce(points), solver.solve(points), 1e-9);
        }
    }

    @Test
    void matchesBruteForceWithManyDuplicatePoints() {
        Point[] points = randomPoints(1000, 20);
        assertEquals(0.0, new ClosestPairSolver().solve(points), 1e-9);
    }

    @Test
    void pointsOnOneVerticalLine() {
        Point[] points = new Point[500];
        for (int i = 0; i < points.length; i++) points[i] = new Point(5, random.nextInt(1_000_000));
        assertEquals(ClosestPairSolver.bruteForce(points), new ClosestPairSolver().solve(points), 1e-9);
    }

    @Test
    void twoPoints() {
        Point[] points = {new Point(0, 0), new Point(3, 4)};
        assertEquals(5.0, new ClosestPairSolver().solve(points), 1e-9);
    }

    @Test
    void fewerThanTwoPoints() {
        assertEquals(Double.POSITIVE_INFINITY, new ClosestPairSolver().solve(new Point[0]));
        assertEquals(Double.POSITIVE_INFINITY, new ClosestPairSolver().solve(new Point[]{new Point(1, 1)}));
    }
}
