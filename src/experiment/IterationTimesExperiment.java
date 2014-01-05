package experiment;

public class IterationTimesExperiment extends Experiment {
    private static final int[] factor = {1, 4, 16, 64};

    protected IterationTimesExperiment(int neighborNum, int particleNum, int iterationTimes, int experimentTimes) {
        super(neighborNum, particleNum, iterationTimes, experimentTimes, factor.length);
    }

    @Override
    protected String getHeader() {
        return "T=32\tT=128\tT=512\tT=2048";
    }

    @Override
    public int iterationTimes() {
        return super.iterationTimes() * factor[group];
    }
}
