import javafx.scene.image.Image;

/**
 * @autor Mathilde Prouvost et Augustine Poirier
 */
public class Étoile extends Poisson {

    private double deltaTEtoile;
    private final int amplitude = 50;
    private double yInit;

    /**
     * Constructeur pour l'étoile
     *
     * @param width largeur de la fenêtre
     * @param height hauteur de la fenêtre
     * @param vitesseLevel vitesse de l'étoile
     */
    public Étoile(int width, int height, double vitesseLevel) {
        super(width, height, vitesseLevel);
        this.yInit = this.y;
        this.ay = 0;
        if (!direction)
            this.image = ImageHelpers.flop(this.image);
        this.image = new Image("/images/star.png");
        this.deltaTEtoile = 0;
    }

    /**
     * Update de l'étoile
     * 
     * @param dt temps entre 2 frames
     */
    @Override
    public void update(double dt) {
        this.x += vx*dt;
        // l'étoile oscille avec une amplitude de 50
        this.y = yInit+amplitude*Math.cos(2*Math.PI*deltaTEtoile);
        this.deltaTEtoile += dt;
        this.vx += ax*dt;
        this.vy += ay*dt;
    }
}
