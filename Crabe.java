import javafx.scene.image.Image;

public class Crabe extends Poisson {

    private double deltaTCrabe, vxInit;
    private final double tempsAvance = 0.5, tempsRecule = 0.2;
    private boolean avance;

    public Crabe(int width, int height, double vitesseLevel) {
        super(width, height, 1.3*vitesseLevel);
        this.ay = 0;
        this.vy = 0;
        this.image = new Image("/images/crabe.png");
        this.deltaTCrabe = 0;
        this.avance = true;
        this.vxInit = this.vx;
    }

    @Override
    public void update(double dt) {
        this.x += vx*dt;
        this.y += vy*dt;

        if (deltaTCrabe > tempsAvance)
            this.avance = false;
        else
            this.avance = true;

        if (!avance)
            this.vx = -vxInit;

        else
            this.vx = vxInit;

        this.vy += ay*dt;

        deltaTCrabe += dt;
        // on s'assure que deltaTCrabe est toujours entre 0 et tempsAvance+tempsRecule
        if (deltaTCrabe > tempsAvance + tempsRecule)
            deltaTCrabe -= tempsAvance + tempsRecule;
    }
}
