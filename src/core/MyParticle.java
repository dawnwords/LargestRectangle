package core;

import net.sourceforge.jswarm_pso.Particle;

public class MyParticle extends Particle {
    public static final int DIMENSION = 4;

    public MyParticle() {
        super(DIMENSION);
    }
}
