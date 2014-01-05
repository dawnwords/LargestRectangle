package experiment;

public class MinVelocityExperiment extends Experiment {
    private static final double[][] minVelocity = {
            {0.1, 0.1, 0.1, 0.01},
            {0.3, 0.3, 0.3, 0.03},
            {0.5, 0.5, 0.5, 0.05},
            {0.7, 0.7, 0.7, 0.07},
    };

    protected MinVelocityExperiment(int neighborNum, int particleNum, int iterationTimes, int experimentTimes) {
        super(neighborNum, particleNum, iterationTimes, experimentTimes, minVelocity.length);
    }

    @Override
    protected String getHeader() {
        return "MIN_XV=0.1\tMIN_XV=0.3\tMIN_XV=0.5\tMIN_XV=0.7";
    }

    @Override
    public double[] minVelocity() {
        return minVelocity[group];
    }
}
