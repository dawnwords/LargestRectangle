package experiment;

public class ParameterExperiment extends Experiment {
    private static final double[][] parameter = {
            {1, 1, 1}, {0.2, 1, 1}, {1, 0.2, 1}, {1, 1, 0.2}
    };

    protected ParameterExperiment(int neighborNum, int particleNum, int iterationTimes, int experimentTimes) {
        super(neighborNum, particleNum, iterationTimes, experimentTimes, parameter.length);
    }

    @Override
    protected String getHeader() {
        return "Normal\tInertia*0.2\tParticleIcr*0.2\tGlobalIcr*0.2";
    }

    @Override
    public double inertia() {
        return super.inertia() * parameter[group][0];
    }

    @Override
    public double particleIncrement() {
        return super.particleIncrement() * parameter[group][1];
    }

    @Override
    public double globalIncrement() {
        return super.globalIncrement() * parameter[group][2];
    }
}
