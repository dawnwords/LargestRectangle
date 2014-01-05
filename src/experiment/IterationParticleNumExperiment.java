package experiment;

import core.MyRectangle;

public class IterationParticleNumExperiment extends Experiment {
    private static final int CONST = 1024 * 1024;
    private int factor;
    private int group;

    protected IterationParticleNumExperiment(int neighborNum, int particleNum, int iterationTimes, int experimentTimes) {
        super(25, 64, CONST / 64, 10);
        factor = 1;
        group = 1;
    }

    @Override
    protected String getHeader() {
        return "ParticleNum\tResult";
    }

    @Override
    protected void showResult(MyRectangle best) {
        write(best.h * best.w);
        group++;

        if (group % 10 == 0) {
            factor *= 2;
            newLine();
        }
    }

    @Override
    public int particleNum() {
        return super.particleNum() * factor;
    }

    @Override
    public int iterationTimes() {
        return super.iterationTimes() / factor;
    }
}
