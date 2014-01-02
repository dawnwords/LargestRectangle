package core;

import gui.SwarmDisplay;
import net.sourceforge.jswarm_pso.Swarm;

import java.awt.*;

public class SwarmThread extends Thread {

    private SwarmDisplay display;

    public SwarmThread(SwarmDisplay display) {
        this.display = display;
    }

    @Override
    public void run() {
        int i = 0;
        Swarm swarm = initSwarm();
        while (i < display.iterationTimes() && !isInterrupted()) {
            swarm.evolve();
            if (swarm.getBestFitness() > 0) {
                double[] best = swarm.getBestPosition();
                double h = best[MyFitnessFunction.H];
                double x = best[MyFitnessFunction.X];
                double y = best[MyFitnessFunction.Y];
                double a = best[MyFitnessFunction.A];
                double w = h * display.getTitleRatio();
                display.showEvolveResult(new MyRectangle(x, y, w, h, a));
                System.out.println(swarm.toStringStats());
            }
            i++;
        }
        display.endEvolve();
    }

    private Swarm initSwarm() {
        MyFitnessFunction function = new MyFitnessFunction(display.getPath(), display.getTitleRatio());
        Swarm swarm = new Swarm(display.particleNum(), new MyParticle(), function);

        double[] maxPosition = new double[MyParticle.DIMENSION];
        double[] minPosition = new double[MyParticle.DIMENSION];

        Rectangle r = display.getPath().getBounds();
        double titleRatio = display.getTitleRatio();

        maxPosition[MyFitnessFunction.X] = r.getX() + r.getWidth();
        maxPosition[MyFitnessFunction.Y] = r.getY() + r.getHeight();
        maxPosition[MyFitnessFunction.H] = diagonal(r.getWidth(), r.getHeight()) / ((titleRatio > 1) ? titleRatio : 1);
        maxPosition[MyFitnessFunction.A] = Math.PI / 2;

        minPosition[MyFitnessFunction.X] = r.getX();
        minPosition[MyFitnessFunction.Y] = r.getY();
        minPosition[MyFitnessFunction.H] = 0;
        minPosition[MyFitnessFunction.A] = -Math.PI / 2;

        swarm.setMaxPosition(maxPosition);
        swarm.setMinPosition(minPosition);
        swarm.setMaxVelocity(display.maxVelocity());
        swarm.setMinVelocity(display.minVelocity());
        return swarm;
    }

    private double diagonal(double w, double h) {
        return Math.sqrt(w * w + h * h);
    }
}
