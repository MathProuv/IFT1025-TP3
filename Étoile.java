import javafx.scene.image.Image;

public class Étoile extends Poisson {

    public Étoile(int width, int height, double vitesseLevel) {
        super(width, height, vitesseLevel);
        this.ay = 0;
        this.image = new Image("/images/star.png");
    }


}
