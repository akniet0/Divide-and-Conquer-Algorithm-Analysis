public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting experiments...");
            Experiment.runExperiments("results/results.csv");
            System.out.println("Execution finished. Data exported to results/results.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}