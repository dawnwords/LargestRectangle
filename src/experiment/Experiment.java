package experiment;

import core.MyRectangle;
import core.SwarmDisplay;
import core.SwarmThread;
import net.sourceforge.jswarm_pso.Swarm;

import java.awt.geom.Path2D;

public abstract class Experiment implements SwarmDisplay {
    protected int group;
    private int neighborNum, particleNum, iterationTimes, experimentTimes, groupCount;
    private int currentTimes;
    private String n;

    protected Experiment(int neighborNum, int particleNum, int iterationTimes, int experimentTimes, int groupCount) {
        this.neighborNum = neighborNum;
        this.particleNum = particleNum;
        this.iterationTimes = iterationTimes;
        this.experimentTimes = experimentTimes;
        this.groupCount = groupCount;
        this.currentTimes = 0;
        this.group = 0;
    }

    public void start() {
        System.out.println("Times\t" + getHeader());
        n = "1";
        startAlgorithm();
    }

    private void startAlgorithm() {
        new SwarmThread(this).start();
    }

    @Override
    public void showEvolveResult(MyRectangle rectangle) {
    }

    @Override
    public void endEvolve(MyRectangle best) {
        write(best.h * best.w);
        group++;

        if (group % groupCount == 0) {
            group = 0;
            newLine();
        }

        if (currentTimes != experimentTimes) {
            startAlgorithm();
        }
    }

    @Override
    public Path2D.Double getPath() {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(100, 100);
        path.lineTo(400, 100);
        path.lineTo(400, 200);
        path.lineTo(100, 200);
        path.closePath();
        return path;
    }

    @Override
    public double getTitleRatio() {
        return 3;
    }

    @Override
    public int particleNum() {
        return particleNum;
    }

    @Override
    public int iterationTimes() {
        return iterationTimes;
    }

    @Override
    public double[] maxVelocity() {
        return new double[]{1, 1, 1, 0.1};
    }

    @Override
    public double[] minVelocity() {
        return new double[]{0.1, 0.1, 0.1, 0.01};
    }

    @Override
    public int neighborNum() {
        return neighborNum;
    }

    @Override
    public double inertia() {
        return Swarm.DEFAULT_INERTIA;
    }

    @Override
    public double particleIncrement() {
        return Swarm.DEFAULT_PARTICLE_INCREMENT;
    }

    @Override
    public double globalIncrement() {
        return Swarm.DEFAULT_GLOBAL_INCREMENT;
    }

    protected abstract String getHeader();

    protected void write(int i) {
        write("" + i);
    }

    protected void write(double d) {
        write("" + d);
    }

    private void write(String s) {
        System.out.print(n + "\t" + s);
        n = "";
    }

    protected void newLine() {
        System.out.println();
        currentTimes++;
        n = "" + (currentTimes + 1);
    }

}
