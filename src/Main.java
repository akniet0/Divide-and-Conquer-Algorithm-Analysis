import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new File("results").mkdirs();
        new Experiment().run("results/results.csv");
        System.out.println("Saved results/results.csv");
    }
}
