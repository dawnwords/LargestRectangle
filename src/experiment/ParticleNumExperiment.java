package experiment;

public class ParticleNumExperiment extends Experiment {
    private static final int[] factors = {1, 16};

    protected ParticleNumExperiment(int neighborNum, int particleNum, int iterationTimes, int experimentTimes) {
        super(neighborNum, particleNum, iterationTimes, experimentTimes, factors.length);
    }

    @Override
    public int particleNum() {
        return super.particleNum() * factors[group];
    }

    @Override
    protected String getHeader() {
        return "P=64\tP=1024";
    }
}
