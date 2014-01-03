package core;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MyRectangle {
    public double x, y, w, h, r;

    public MyRectangle(double x, double y, double w, double h, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.w = w;
        this.h = h;
    }

    public void paint(Graphics2D g) {
        g.rotate(r, x, y);
        g.draw(new Rectangle2D.Double(x, y - h, w, h));
        g.rotate(-r, x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyRectangle rectangle = (MyRectangle) o;

        return Double.compare(rectangle.h, h) == 0
                && Double.compare(rectangle.r, r) == 0
                && Double.compare(rectangle.w, w) == 0
                && Double.compare(rectangle.x, x) == 0
                && Double.compare(rectangle.y, y) == 0;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + w + " " + h + " " + r;
    }
}
