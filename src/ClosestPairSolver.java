import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    public long distanceChecks;
    public int maxDepth;
    private Point[] points;
    private Point[] buffer;

    // Returns the smallest distance between two points (infinity if fewer than 2 points).
    public double solve(Point[] input) {
        distanceChecks = 0;
        maxDepth = 0;
        if (input.length < 2) return Double.POSITIVE_INFINITY;
        points = input.clone();
        buffer = new Point[points.length];
        Arrays.sort(points, Comparator.comparingDouble(Point::x));
        return Math.sqrt(solve(0, points.length - 1, 1));
    }

    // Returns the smallest squared distance in points[lo..hi]; leaves that range sorted by y.
    private double solve(int lo, int hi, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        if (hi - lo + 1 <= 3) {
            double best = Double.POSITIVE_INFINITY;
            for (int i = lo; i <= hi; i++)
                for (int j = i + 1; j <= hi; j++)
                    best = Math.min(best, dist2(points[i], points[j]));
            Arrays.sort(points, lo, hi + 1, Comparator.comparingDouble(Point::y));
            return best;
        }

        int mid = (lo + hi) >>> 1;
        double midX = points[mid].x();
        double best = Math.min(solve(lo, mid, depth + 1), solve(mid + 1, hi, depth + 1));
        mergeByY(lo, mid, hi);

        int size = 0;
        for (int i = lo; i <= hi; i++) {
            double dx = points[i].x() - midX;
            if (dx * dx >= best) continue;
            for (int j = size - 1; j >= 0; j--) {
                double dy = points[i].y() - buffer[j].y();
                if (dy * dy >= best) break;
                best = Math.min(best, dist2(points[i], buffer[j]));
            }
            buffer[size++] = points[i];
        }
        return best;
    }

    private void mergeByY(int lo, int mid, int hi) {
        System.arraycopy(points, lo, buffer, lo, hi - lo + 1);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) points[k] = buffer[j++];
            else if (j > hi) points[k] = buffer[i++];
            else if (buffer[j].y() < buffer[i].y()) points[k] = buffer[j++];
            else points[k] = buffer[i++];
        }
    }

    private double dist2(Point p, Point q) {
        distanceChecks++;
        double dx = p.x() - q.x();
        double dy = p.y() - q.y();
        return dx * dx + dy * dy;
    }

    public static double bruteForce(Point[] pts) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x() - pts[j].x();
                double dy = pts[i].y() - pts[j].y();
                best = Math.min(best, dx * dx + dy * dy);
            }
        }
        return Math.sqrt(best);
    }
}
