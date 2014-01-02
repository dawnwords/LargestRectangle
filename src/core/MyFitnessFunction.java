package core;

import net.sourceforge.jswarm_pso.FitnessFunction;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class MyFitnessFunction extends FitnessFunction {
    public static final int X = 0;
    public static final int Y = 1;
    public static final int H = 2;
    public static final int A = 3;
    private Path2D.Double path;
    private double r;

    public MyFitnessFunction(Path2D.Double path, double r) {
        this.path = path;
        this.r = r;
    }

    @Override
    public double evaluate(double[] position) {
        double x = position[X];
        double y = position[Y];
        double h = position[H];
        double a = position[A];
        double w = h * r;

        AffineTransform at = new AffineTransform();
        at.rotate(-a, x, y);

        Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y - h, w, h);
        return Path2D.contains(path.getPathIterator(at), rectangle) ? h * w : 0;
    }
}
