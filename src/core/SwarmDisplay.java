package core;

import java.awt.geom.Path2D;

public interface SwarmDisplay {
    void showEvolveResult(MyRectangle rectangle);

    void endEvolve();

    Path2D.Double getPath();

    double getTitleRatio();

    int particleNum();

    int iterationTimes();

    double[] maxVelocity();

    double[] minVelocity();

    int neighborNum();

    double inertia();

    double particleIncrement();

    double globalIncrement();
}


