import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static java.lang.Math.pow;

public class Jeu {
    private final int width, height;
    private final Modele modele;
    
    private final int nbViesMax = 3;
    private final Image imgScore = new Image("/images/fish/00.png");
    private Image imagesScore[] = new Image[nbViesMax];
    private final int tailleImgScore = 30, espacementImgScore = 30, xImage1Score;
    private final int scoreMaxParNiveau = 5;
    private int scoreDansCeNiveau;
    
    private Bulles bulles;
    
    private int niveau;
    private int score;
    private int nbVies;
    private double vitesse;
    
    /**
     * Constructeur d'un jeu
     */
    Jeu(int width, int height){
        this.width = width;
        this.height = height;
        this.modele = new Modele(width, height);
        this.xImage1Score = width/2 - (nbViesMax*tailleImgScore + (nbViesMax-1)*espacementImgScore)/2;
    }
    
    /**
     * commencer une partie
     */
    public void commencer(){
        this.niveau = 0;
        this.score = 0;
        this.nbVies = nbViesMax;
        for (int i = 0; i<nbViesMax; i++)
            imagesScore[i] = imgScore;
        this.scoreDansCeNiveau = 0;
        monterNiveau();
    }
    
    /**
     * Fonction qui crée une instance de Bulles
     */
    public void creerBulles() {
        this.bulles = new Bulles(width, height);
    }
    
    /**
     *
     * @param dt temps entre les deux frames
     */
    public void update(double dt){
        bulles.update(dt);
    }
    
    /**
     *
     * @param context du canvas à dessiner
     */
    public void draw(GraphicsContext context){
        context.clearRect(0, 0, width, height);
        bulles.draw(context);
    
        context.setFont(new Font(30));
        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(Color.RED);
        context.fillText(((Integer)score).toString(), width/2.0, 60);
        
        for (int i = 0; i<nbViesMax; i++){
            int xImageI = xImage1Score + i * (tailleImgScore+espacementImgScore);
            context.drawImage(imagesScore[i], xImageI, 100, tailleImgScore, tailleImgScore);
        }
    }
    
    /**
     * Lancer une balle pour essayer de capturer un poisson
     * @param xTir coordonnée x du tir
     * @param yTir coordonnée y du tir
     */
    public void tirer(double xTir, double yTir) {
        System.out.println("on tire en ("+xTir+","+yTir+")");
    }
    
    /**
     * Augmenter le niveau et mettre à jour la vitesse des futurs poissons
     */
    public void monterNiveau(){
        this.niveau ++;
        this.vitesse = 100*pow(this.niveau,1/3.0)+200;
        this.scoreDansCeNiveau = 0;
        System.out.println("Niveau = " + this.niveau);
    }
    
    /**
     * Augmenter le score
     */
    public void monterScore(){
        this.score ++;
        this.scoreDansCeNiveau ++;
        if (scoreDansCeNiveau >= scoreMaxParNiveau)
            monterNiveau();
    }
    
    /**
     * Augmenter le nombre de vies dans la limite du possible
     */
    public void monterVie(){
        if (nbVies < nbViesMax){
            this.nbVies ++;
            imagesScore[nbVies-1] = ImageHelpers.colorize(imgScore, Color.WHITE);
        }
        System.out.println("nbVie = " + this.nbVies);
    }
    
    /**
     * Diminuer le nombre de vies dans la limite du possible
     */
    public void baisserVie() {
        if (nbVies > 0) {
            this.nbVies--;
            imagesScore[nbVies] = ImageHelpers.colorize(imgScore, Color.BLACK);
        }
        System.out.println("nbVie = " + this.nbVies);
        if (nbVies <= 0)
            mourir();
    }
    
    /**
     * Faire perdre la partie
     */
    public void mourir() {
        System.out.println("T'es mort !!!");
    }
}
