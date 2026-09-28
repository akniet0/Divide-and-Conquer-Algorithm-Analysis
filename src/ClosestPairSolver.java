import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    public static double solve(Point[] points, Metrics m) {
        if (points == null || points.length < 2) return Double.POSITIVE_INFINITY;
        Point[] sortedX = points.clone();
        Arrays.sort(sortedX, Comparator.comparingDouble(p -> p.x));
        Point[] aux = new Point[sortedX.length];
        return findClosest(sortedX, aux, 0, sortedX.length - 1, m);
    }

    private static double findClosest(Point[] px, Point[] aux, int low, int high, Metrics m) {
        m.enter();
        if (high - low <= 3) {
            double min = bruteForce(px, low, high, m);
            Arrays.sort(px, low, high + 1, Comparator.comparingDouble(p -> p.y));
            m.exit();
            return min;
        }

        int mid = low + (high - low) / 2;
        double midX = px[mid].x;

        double d1 = findClosest(px, aux, low, mid, m);
        double d2 = findClosest(px, aux, mid + 1, high, m);
        double d = Math.min(d1, d2);

        mergeY(px, aux, low, mid, high);

        int stripCount = 0;
        for (int i = low; i <= high; i++) {
            if (Math.abs(px[i].x - midX) < d) {
                aux[stripCount++] = px[i];
            }
        }

        for (int i = 0; i < stripCount; i++) {
            for (int j = i + 1; j < stripCount && (aux[j].y - aux[i].y) < d; j++) {
                m.comparisons++;
                double dist = aux[i].distanceTo(aux[j]);
                if (dist < d) {
                    d = dist;
                }
            }
        }

        m.exit();
        return d;
    }

    private static void mergeY(Point[] a, Point[] aux, int low, int mid, int high) {
        System.arraycopy(a, low, aux, low, high - low + 1);
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > high) a[k] = aux[i++];
            else if (aux[j].y < aux[i].y) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    public static double bruteForce(Point[] p, int low, int high, Metrics m) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = low; i <= high; i++) {
            for (int j = i + 1; j <= high; j++) {
                if (m != null) m.comparisons++;
                double dist = p[i].distanceTo(p[j]);
                if (dist < min) min = dist;
            }
        }
        return min;
    }
}