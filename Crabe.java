import javafx.scene.image.Image;

/**
 * @author Mathilde Prouvost et Augustine Poirier
 */
public class Crabe extends Poisson {

    private double deltaTCrabe, vxInit;
    private final double tempsAvance = 0.5, tempsRecule = 0.2;
    private boolean avance; // true si le crabe avance, false s'il recule

    /**
     * Constructeur du crabe
     *
     * @param width largeur de la fenêtre
     * @param height hauteur de la fenêtre
     * @param vitesseLevel vitesse du crabe
     */
    public Crabe(int width, int height, double vitesseLevel) {
        super(width, height, 1.3*vitesseLevel);
        this.ay = 0;
        this.vy = 0;
        this.image = new Image("/images/crabe.png");
        this.deltaTCrabe = 0;
        this.avance = true;
        this.vxInit = this.vx;
    }

    /**
     * Update du crabe
     *
     * @param dt temps entre 2 frames
     */
    @Override
    public void update(double dt) {
        this.x += vx*dt;
        this.y += vy*dt;

        // si le crabe a avancé pendant 0.5 sec il change de direction, sinon il garde la même direction
        if (deltaTCrabe > tempsAvance)
            this.avance = false;
        else
            this.avance = true;

        // si le crabe recule, sa vitesse est multipliée par -1
        if (!avance)
            this.vx = -vxInit;
        // s'il avance, sa vitesse correspond à sa vitesse initiale
        else
            this.vx = vxInit;

        this.vy += ay*dt;

        deltaTCrabe += dt;

        // on s'assure que deltaTCrabe est toujours entre 0 et tempsAvance+tempsRecule
        if (deltaTCrabe > tempsAvance + tempsRecule)
            deltaTCrabe -= tempsAvance + tempsRecule;
    }
}
