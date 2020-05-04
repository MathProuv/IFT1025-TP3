import javafx.scene.image.Image;

public class Étoile extends Poisson {

    private double deltaTEtoile;
    private final int amplitude = 50;
    private double yInit;

    public Étoile(int width, int height, double vitesseLevel) {
        super(width, height, vitesseLevel);
        this.yInit = this.y;
        this.ay = 0;
        if (!direction)
            this.image = ImageHelpers.flop(this.image);
        this.image = new Image("/images/star.png");
        this.deltaTEtoile = 0;
    }

    @Override
    public void update(double dt) {
        this.x += vx*dt;
        this.y = yInit+amplitude*Math.cos(2*Math.PI*deltaTEtoile);
        this.deltaTEtoile += dt;
        this.vx += ax*dt;
        this.vy += ay*dt;
    }


}
