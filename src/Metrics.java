public class Metrics {
    public long comparisons = 0;
    public int maxDepth = 0;
    public int currentDepth = 0;

    public void enter() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exit() {
        currentDepth--;
    }

    public void reset() {
        comparisons = 0;
        maxDepth = 0;
        currentDepth = 0;
    }
}