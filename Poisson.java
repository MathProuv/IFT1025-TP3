import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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
        int randX = rand.nextInt();
        // Position en x aléatoire : à droite ou à gauche
        this.x = randX == 0 ? -taille : height+taille;
        this.direction = randX == 0;
        // Position y entre 0.2 et 0.8 de la taille de la fenêtre
        this.y = rand.nextDouble()*0.6*height+height/5.0;
        // Vitesse verticale entre 100 et 200 px/s
        this.vy = rand.nextDouble()*100+100;
        this.vx = vitesseLevel;
        this.ay = -100;
        this.ax = 0;
        int randImage = rand.nextInt(imagesList.length);
        this.image = imagesList[randImage];
        if (!direction)
            this.image = ImageHelpers.flip(this.image);
    }

    public void draw(GraphicsContext context) {
        context.drawImage(this.image, x, height-taille-y, taille, taille);
    }

    public void update(double dt) {
        this.x += vx*dt;
        this.y += vy*dt;
        this.vx += ax*dt;
        this.vy += ay*dt;
    }

    public double getX(){ return this.x; }

    public double getY(){ return this.y; }
}
