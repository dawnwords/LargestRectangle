package experiment;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static final Class[] EXPERIMENT_CLASSES = new Class[]{NeighborhoodExperiment.class,
            IterationParticleNumExperiment.class};

    public static void main(String[] args) throws Exception {
        System.out.println("Choose Experiment:format(%d)");
        for (int i = 0; i < EXPERIMENT_CLASSES.length; i++) {
            System.out.printf("%d:%s\t", i, EXPERIMENT_CLASSES[i].getSimpleName());
        }
        System.out.println();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final int experimentNum = safeParseInt(reader.readLine(), 0);
        Class experimentClass = EXPERIMENT_CLASSES[experimentNum];

        System.out.println("Perform " + experimentClass.getSimpleName());
        System.out.println("Input Experiment Parameter: format(%d %d %d %d %d)");
        System.out.println("neighborNum particleNum iterationTimes experimentTime");
        String tokens[] = reader.readLine().split(" ");
        if (tokens.length != 4) {
            tokens = new String[4];
        }

        final int neighborNum = safeParseInt(tokens[0], 25);
        final int particleNum = safeParseInt(tokens[1], 250);
        final int iterationTimes = safeParseInt(tokens[2], 1000);
        final int experimentTime = safeParseInt(tokens[3], 100);

        ((Experiment) experimentClass.getDeclaredConstructors()[0]
                .newInstance(neighborNum, particleNum, iterationTimes, experimentTime))
                .start();
    }

    private static int safeParseInt(String s, int def) {
        int result;
        try {
            result = Integer.parseInt(s);
        } catch (Exception e) {
            result = def;
        }
        return result;
    }

}
