import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.pow;

public class Jeu {
    private final int width, height;
    private final Modele modele;
    private FishHunt vue;
    
    private final int nbViesMax = 3;
    private final Image imgScore = new Image("/images/fish/00.png");
    private Image imagesScore[] = new Image[nbViesMax];
    private int scoreDansCeNiveau;
    private final int tailleImgScore = 30, espacementImgScore = 30, xImage1Score;
    private final int scoreMaxParNiveau = 5;
    
    private Bulles bulles;
    private ArrayList<Poisson> poissons;
    // nombre de poissons depuis le début de la partie
    private int nbPoissons = 0;
    private int nbPoissonsSpecial = 0;
    private double deltaTLvl;

    private int niveau;
    private int score;
    private int nbVies;
    private double vitesse;
    
    /**
     * Constructeur d'un jeu
     */
    Jeu(int width, int height, FishHunt vue){
        this.width = width;
        this.height = height;
        this.modele = new Modele(width, height);
        this.vue = vue;
        this.xImage1Score = width/2 - (nbViesMax*tailleImgScore + (nbViesMax-1)*espacementImgScore)/2;
    }
    
    /**
     * commencer une partie
     */
    public void commencer(){
        this.niveau = 1;
        this.score = 0;
        this.nbVies = nbViesMax;
        this.vitesse = 100*pow(this.niveau,1/3.0)+200;
        for (int i = 0; i<nbViesMax; i++)
            imagesScore[i] = imgScore;
        this.scoreDansCeNiveau = 0;
        this.poissons = new ArrayList<Poisson>();
        this.nbPoissons = 0;
        this.nbPoissonsSpecial = 0;
        this.deltaTLvl = 0;
    }
    
    /**
     * Fonction qui crée une instance de Bulles
     */
    public void creerBulles() {
        this.bulles = new Bulles(width, height);
    }

    /**
     * Fonction qui ajoute une instance de Poisson
     */
    public void ajouterPoisson() {
        Poisson poisson = new Poisson(width, height, vitesse);
        this.poissons.add(poisson);
        this.nbPoissons += 1;
    }

    /**
     * Fonction qui ajoute une instance de Crabe ou Étoile
     */
    public void ajouterPoissonSpecial() {
        Random rand = new Random();
        boolean type = rand.nextBoolean();
        Poisson poisson = type ? new Crabe(width, height, vitesse) : new Étoile(width, height, vitesse);
        this.poissons.add(poisson);
        this.nbPoissonsSpecial += 1;
    }

    /**
     * Fonction qui retire une instance de poisson
     */
    public void retirerPoisson(Poisson poisson) { this.poissons.remove(poisson); }

    /**
     *
     * @param dt temps entre les deux frames
     */
    public void update(double dt){
        modele.update(this, dt);
    }
    
    /**
     *
     * @param context du canvas à dessiner
     */
    public void draw(GraphicsContext context){

        context.clearRect(0, 0, width, height);

        for (int i = 0; i<nbViesMax; i++){
            int xImageI = xImage1Score + i * (tailleImgScore+espacementImgScore);
            context.drawImage(imagesScore[i], xImageI, 100, tailleImgScore, tailleImgScore);
        }

        context.setFont(new Font(30));
        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(Color.RED);
        context.fillText(((Integer)score).toString(), width/2.0, 60);

        modele.draw(context, this);
    }
    
    /**
     * Lancer une balle pour essayer de capturer un poisson
     * @param xTir coordonnée x du tir
     * @param yTir coordonnée y du tir
     */
    public void tirer(double xTir, double yTir) {
        modele.tirer(xTir, yTir, this);
    }
    
    /**
     * Augmenter le niveau et mettre à jour la vitesse des futurs poissons
     */
    public void monterNiveau(){
        this.niveau ++;
        this.vitesse = 100*pow(this.niveau,1/3.0)+200;
        this.scoreDansCeNiveau = 0;
        System.out.println("Niveau = " + this.niveau);

        // on réinitialise les quantités de poissons et le temps depuis le début du niveau
        this.deltaTLvl = 0;
        this.nbPoissonsSpecial = 0;
        this.nbPoissons = 0;

        vue.monterNiveau(niveau);
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
        modele.mourir();
        vue.mourir();
    }

    public boolean verifScore() {
        try {
            Scanner scan = new Scanner(new FileInputStream("scores.txt"));
            int scoreFichier = 0;
            while (scan.hasNext()) {
                scoreFichier = scan.nextInt();
                scan.nextLine();
            }
            return (score > scoreFichier);

        } catch (FileNotFoundException ex) {
            System.out.println("Erreur à l'ouverture du fichier");
        }

        return false;
    }

    /**
     * Ajoute un nouveau score dans le fichier scores.txt
     * @param nom le nom du joueur
     */
    public void updateScore(String nom) throws FileNotFoundException {

        try {
            ArrayList<Integer> listScore = new ArrayList<Integer>();
            ArrayList<String> listNom = new ArrayList<String>();
            Scanner scan = new Scanner(new FileInputStream("scores.txt"));
            while (scan.hasNextInt()) {
                int scoreFichier = scan.nextInt();
                listScore.add(scoreFichier);
                String nomFichier = scan.nextLine().substring(1);
                listNom.add(nomFichier);
            }


            // on parcourt la liste des scores en commençant par le bas
            int indexScore;
            boolean isBest = true;
            for (indexScore = listScore.size() - 1; indexScore >= 0; indexScore--) {
                int curScore = listScore.get(indexScore);
                if (score < curScore) {
                    listScore.add(indexScore+1, score);
                    listNom.add(indexScore+1, nom);
                    isBest = false;
                    break;
                }
            }

            // si score est le meilleur score
            if (isBest) {
                listScore.add(0, score);
                listNom.add(0, nom);
            }

            // on vérifie que la liste contient au maximum 10 scores
            if (listScore.size() > 9) {
                listScore.remove(10);
                listNom.remove(10);
            }

            // on réécrit le fichier
            try {
                FileWriter wr = new FileWriter("scores.txt");
                BufferedWriter writer = new BufferedWriter(wr);

                for (int i=0; i<listScore.size(); i++)
                    writer.append(listScore.get(i) + " " + listNom.get(i) + "\n");

                writer.close();

            } catch (IOException ex) {
                System.out.println("Erreur avec le fichier");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Erreur à l'ouverture du fichier");
        }
    }

    public void setDeltaTLvl(double deltaTLvl) { this.deltaTLvl = deltaTLvl; }

    public double getDeltaLvl() { return this.deltaTLvl; }

    public int getNbPoissons() { return this.nbPoissons; }

    public int getNbPoissonsSpecial() { return this.nbPoissonsSpecial; }

    public int getNiveau() { return this.niveau; }

    public Bulles getBulles() { return this.bulles; }

    public ArrayList<Poisson> getPoissons() { return this.poissons; }

    public int getScore() { return this.score; }
}
