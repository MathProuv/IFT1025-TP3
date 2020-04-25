import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Mathilde Prouvost et Augustine Poirier
 */

public class Bulles {
    private ArrayList<Double> y;
    private ArrayList<Integer> rayon, vy, x;
    private final Color color = Color.rgb(0, 0, 255, 0.4);
    private final int nbBaseX = 3, nbBulles = 5;
    private int heightFenetre;

    /**
     * Constructeur
     * @param widthFenetre largeur de la fenêtre en px
     * @param heightFenetre hauteur de la fenêtre en px
     */
    public Bulles(int widthFenetre, int heightFenetre) {
        rayon = new ArrayList<>();
        vy = new ArrayList<>();
        x = new ArrayList<>();
        y = new ArrayList<>();
        this.heightFenetre = heightFenetre;
    
        Random rand = new Random();
        // basex
        for (int i = 0; i<nbBaseX; i++) {
            int basexi = rand.nextInt(widthFenetre+1);
            for (int j = 0; j < nbBulles; j++) {
                // positions en x
                int x = rand.nextInt(41) - 20;
                this.x.add(basexi + x);
                // rayons
                int rayon = rand.nextInt(31) + 10;
                this.rayon.add(rayon);
                // positions en y
                this.y.add(-40.0);
                // vitesse
                int vy = rand.nextInt(101) + 350;
                this.vy.add(vy);
            }
        }
        //System.out.println("x=" + x.get(0) + " y=" + y.get(0) + " vy=" + vy.get(0));
    }

    /**
     * Fonction update des Bulles
     * @param dt temps entre 2 frames
     */
    public void update(double dt) {
        for (int i=0; i < nbBaseX*nbBulles; i++) {
            int vy = this.vy.get(i);
            double y = this.y.get(i);
            y += vy*dt;
            this.y.set(i, y);
        }
        //System.out.println(y.get(0));
    }

    /**
     * Fonction draw des Bulles
     * @param context context du canvas
     */
    public void draw(GraphicsContext context) {
        context.setFill(color);
        for (int i=0; i<nbBaseX*nbBulles; i++) {
            int x = this.x.get(i);
            double y = this.y.get(i);
            int rayon = this.rayon.get(i);
            context.fillOval(x, heightFenetre - y, rayon, rayon);
        }
    }
}