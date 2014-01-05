package experiment;

import core.MyRectangle;

public class NeighborhoodExperiment extends Experiment {
    private boolean withNeighborhood;

    public NeighborhoodExperiment(int neighborNum, int particleNum, int iterationTimes, int experimentTime) {
        super(neighborNum, particleNum, iterationTimes, experimentTime);
        this.withNeighborhood = true;
    }

    @Override
    protected void showResult(MyRectangle best) {
        write(best.w * best.h);
        if (!withNeighborhood) {
            newLine();
        }
        withNeighborhood = !withNeighborhood;
    }

    @Override
    protected String getHeader() {
        return "Times\tWithNeighbor\tWithoutNeighbor";
    }

    @Override
    public int neighborNum() {
        return withNeighborhood ? super.neighborNum() : 0;
    }
}
