import javafx.scene.image.Image;

public class Crabe extends Poisson {

    public Crabe(int width, int height, double vitesseLevel) {
        super(width, height, 1.3*vitesseLevel);
        this.ay = 0;
        this.image = new Image("/images/crabe.png");
    }

}
