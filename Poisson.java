import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.ArrayList;
import java.util.Random;

public class Poisson {
    protected final int width, height, taille;
    // x et y représentent les coordonnées en haut à gauche du poisson
    protected double x, y, vx, vy;
    protected int ax, ay;
    // true : droite, false : gauche
    protected boolean direction;
    private static Image[] imagesList = new Image[] {
            new Image("/images/fish/00.png"),
            new Image("/images/fish/01.png"),
            new Image("/images/fish/02.png"),
            new Image("/images/fish/03.png"),
            new Image("/images/fish/04.png"),
            new Image("/images/fish/05.png"),
            new Image("/images/fish/06.png"),
            new Image("/images/fish/07.png")
    };
    protected Image image;

    public Poisson(int width, int height, double vitesseLevel) {
        Random rand = new Random();
        // Taille aléatoire entre 80 et 120px
        this.taille = rand.nextInt(41) + 80;
        this.width = width;
        this.height = height;

        boolean randX = rand.nextBoolean();
        // Position en x aléatoire : à droite ou à gauche
        this.x = randX ? -taille : width+taille;
        this.direction = (randX);
        // Position y entre 0.2 et 0.8 de la taille de la fenêtre (en haut à gauche du poisson)
        this.y = rand.nextDouble()*0.6*height+height/5.0;
        // Vitesse verticale entre 100 et 200 px/s
        this.vy = -(rand.nextDouble()*100+100);
        this.vx = randX ? vitesseLevel : -vitesseLevel;
        this.ay = 100;
        this.ax = 0;
        int randImage = rand.nextInt(imagesList.length);
        this.image = imagesList[randImage];
        if (!direction)
            this.image = ImageHelpers.flop(this.image);
    }

    public void draw(GraphicsContext context) {
        context.drawImage(this.image, x, y, taille, taille);
        context.setFill(Color.RED);
        context.fillRect(x, y, 5, 5);
    }

    public void update(double dt) {
        this.x += vx*dt;
        this.y += vy*dt;
        this.vx += ax*dt;
        this.vy += ay*dt;

        //this.x = width/2;
        //this.y = height/2;
    }

    public double getX(){ return this.x; }

    public double getY(){ return this.y; }

    public double getvx(){ return this.vx; }

    public double getvy(){ return this.vy; }

    public boolean getDirection(){ return this.direction; }

    public int getTaille() { return this.taille; }
}
