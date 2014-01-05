package experiment;

public class NeighborhoodExperiment extends Experiment {
    private static final int[] factor = {1, 0};

    public NeighborhoodExperiment(int neighborNum, int particleNum, int iterationTimes, int experimentTime) {
        super(neighborNum, particleNum, iterationTimes, experimentTime, factor.length);
    }

    @Override
    public int neighborNum() {
        return super.neighborNum() * factor[group];
    }

    @Override
    protected String getHeader() {
        return "WithNeighbor\tWithoutNeighbor";
    }
}
