import javafx.scene.canvas.GraphicsContext;

import static java.lang.Math.pow;

public class Jeu {
    private final int width, height;
    private final Modele modele;
    
    private final int nbViesMax = 3;
    
    private Bulles bulles;
    
    private int niveau;
    private int score;
    private int nbVies;
    private double vitesse;
    
    Jeu(int width, int height){
        this.width = width;
        this.height = height;
        this.modele = new Modele(width, height);
    }
    
    public void commencer(){
        this.niveau = 0;
        this.score = 0;
        this.nbVies = nbViesMax;
        monterNiveau();
    }
    
    /**
     * Fonction qui crée une instance de Bulles
     */
    public void creerBulles() {
        this.bulles = new Bulles(width, height);
        //System.out.println("creerBulles()");
    }
    
    public void update(double dt){
        bulles.update(dt);
    }
    
    public void draw(GraphicsContext context){
        context.clearRect(0, 0, width, height);
        bulles.draw(context);
    }
    
    public void tirer(double x, double y) {
        System.out.println("on tire en ("+x+","+y+")");
    }
    
    public void monterNiveau(){
        this.niveau ++;
        this.vitesse = 100*pow(this.niveau,1/3.0)+200;
        System.out.println("Niveau = " + this.niveau);
    }
    
    public void monterScore(){
        this.score ++;
        System.out.println("Score = " + this.score);
    }
    
    public void monterVie(){
        if (nbVies < nbViesMax)
            this.nbVies ++;
        System.out.println("nbVie = " + this.nbVies);
    }
    
    public void baisserVie() {
        if (nbVies > 0)
            this.nbVies --;
        System.out.println("nbVie = " + this.nbVies);
        if (nbVies <= 0)
            mourir();
    }
    
    public void mourir() {
        System.out.println("T'es mort !!!");
    }
}
