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

/**
 * @author Mathilde Prouvost et Augustine Poirier
 */
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
    private int nbPoissons = 0; // nombre de poissons depuis le début du niveau
    private int nbPoissonsSpeciaux = 0; // nombre de poissons spéciaux depuis le début du niveau
    private double deltaTLvl;

    private int niveau;
    private int score;
    private int nbVies;
    private double vitesse;
    
    /**
     * Constructeur d'un jeu
     *
     * @param width largeur de la fenêtre
     * @param height hauteur de la fenêtre
     * @param vue la vue utilisée
     */
    Jeu(int width, int height, FishHunt vue){
        this.width = width;
        this.height = height;
        this.modele = new Modele(width, height);
        this.vue = vue;
        this.xImage1Score = width/2 - (nbViesMax*tailleImgScore + (nbViesMax-1)*espacementImgScore)/2;
    }
    
    /**
     * Méthode pour commencer une nouvelle partie, qui réinitialise les attributs du jeu
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
        this.nbPoissonsSpeciaux = 0;
        this.deltaTLvl = 0;
    }
    
    /**
     * Fonction qui ajoute une instance de Bulles dans l'ArrayList en mémoire
     */
    public void ajouterBulles() { this.bulles = new Bulles(width, height); }

    /**
     * Fonction qui ajoute une instance de Poisson dans l'ArrayList en mémoire
     */
    public void ajouterPoisson() {
        Poisson poisson = new Poisson(width, height, vitesse);
        this.poissons.add(poisson);
        this.nbPoissons += 1;
    }

    /**
     * Fonction qui ajoute une instance de Crabe ou Étoile dans l'ArrayList en mémoire
     */
    public void ajouterPoissonSpecial() {
        Random rand = new Random();
        boolean type = rand.nextBoolean();
        Poisson poisson = type ? new Crabe(width, height, vitesse) : new Étoile(width, height, vitesse);
        this.poissons.add(poisson);
        this.nbPoissonsSpeciaux += 1;
    }

    /**
     * Fonction qui retire une instance de poisson de l'ArrayList en mémoire
     */
    public void retirerPoisson(Poisson poisson) { this.poissons.remove(poisson); }

    /**
     * Update du jeu
     *
     * @param dt temps entre les deux frames
     */
    public void update(double dt){ modele.update(this, dt); }
    
    /**
     * Draw du jeu
     *
     * @param context du canvas à dessiner
     */
    public void draw(GraphicsContext context){

        context.clearRect(0, 0, width, height);

        // vies restantes (3 poissons en haut de l'écran)
        for (int i = 0; i<nbViesMax; i++){
            int xImageI = xImage1Score + i * (tailleImgScore+espacementImgScore);
            context.drawImage(imagesScore[i], xImageI, 100, tailleImgScore, tailleImgScore);
        }

        // score
        context.setFont(new Font(30));
        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(Color.RED);
        context.fillText(((Integer)score).toString(), width/2.0, 60);

        modele.draw(context, this);
    }
    
    /**
     * Méthode pour tirer
     *
     * @param xTir coordonnée x du tir
     * @param yTir coordonnée y du tir
     */
    public void tirer(double xTir, double yTir) {
        modele.tirer(xTir, yTir, this);
        vue.tirer();
    }
    
    /**
     * Méthode pour augmenter le niveau et mettre à jour la vitesse des futurs poissons
     */
    public void monterNiveau(){
        this.niveau ++;
        // nouvelle vitesse des poissons
        this.vitesse = 100*pow(this.niveau,1/3.0)+200;
        this.scoreDansCeNiveau = 0;

        // on réinitialise les quantités de poissons et le temps depuis le début du niveau
        this.deltaTLvl = 0;
        this.nbPoissonsSpeciaux = 0;
        this.nbPoissons = 0;

        vue.monterNiveau(niveau);
    }
    
    /**
     * Méthode pour augmenter le score
     */
    public void monterScore(){
        this.score ++;
        this.scoreDansCeNiveau ++;
        // si le score est assez élevé pour changer de niveau, on augmente le niveau
        if (scoreDansCeNiveau >= scoreMaxParNiveau)
            monterNiveau();
    }
    
    /**
     * Méthode pour ugmenter le nombre de vies
     */
    public void monterVie(){
        if (nbVies < nbViesMax){ // on ne peut pas avoir plus de 3 vies
            this.nbVies ++;
            imagesScore[nbVies-1] = ImageHelpers.colorize(imgScore, Color.WHITE);
        }
    }
    
    /**
     * Méthode pour diminuer le nombre de vies dans la limite du possible
     */
    public void baisserVie() {
        if (nbVies > 0) { // on ne peut pas avoir moins de 0 vie
            this.nbVies--;
            imagesScore[nbVies] = ImageHelpers.colorize(imgScore, Color.BLACK);
        }
        if (nbVies <= 0) // si on devrait avoir moins de 0 vie, on meurt
            mourir();
    }

    /**
     * Méthode pour faire mourir le joueur
     */
    public void mourir() {
        vue.mourir();
    }

    /**
     * Méthode pour vérifier si le score obtenu par le joueur fait partie des 10 meilleurs scores
     *
     * @return booléen true si le score est dans le top 10, false sinon
     */
    public boolean isInTop10() {
        try {
            Scanner scan = new Scanner(new FileInputStream("scores.txt"));
            int scoreFichier = 0;
            // on parcourt tout le fichier de score jusqu'à arriver au dernier
            while (scan.hasNext()) {
                scoreFichier = scan.nextInt();
                scan.nextLine();
            }
            // si le score est supérieur au 10e meilleur score, on retourne true
            return (score > scoreFichier);

        } catch (FileNotFoundException ex) {
            System.out.println("Erreur à l'ouverture du fichier");
        }

        // si le score n'est pas supérieur au 10e meilleur score, on retourne false
        return false;
    }

    /**
     * Méthode pour ajouter un nouveau score dans le fichier scores.txt au bon classement
     *
     * @param nom le nom du joueur
     */
    public void updateScore(String nom) {

        try {
            ArrayList<Integer> listScore = new ArrayList<Integer>();
            ArrayList<String> listNoms = new ArrayList<String>();
            Scanner scan = new Scanner(new FileInputStream("scores.txt"));
            // on ajoute tous les scores du fichier dans l'ArrayList listScore
            // et tous les noms du fichier dans l'ArrayList listNoms
            while (scan.hasNextInt()) {
                int scoreFichier = scan.nextInt();
                listScore.add(scoreFichier);
                String nomFichier = scan.nextLine().substring(1);
                listNoms.add(nomFichier);
            }


            // on parcourt la liste des scores en commençant par le bas
            int indexScore;
            boolean isBest = true;
            for (indexScore = listScore.size() - 1; indexScore >= 0; indexScore--) {
                int curScore = listScore.get(indexScore);

                // si on tombe sur un score supérieur au score actuel, on ajoute le score et le nom à la bonne
                // position dans le top 10 et on arrête
                if (score < curScore) {
                    listScore.add(indexScore+1, score);
                    listNoms.add(indexScore+1, nom);
                    isBest = false; // le score n'est donc pas le meilleur score
                    break;
                }
            }

            // si le score est le meilleur score, on ajoute le score et le nom en première position
            if (isBest) {
                listScore.add(0, score);
                listNoms.add(0, nom);
            }

            // on vérifie que la liste contient au maximum 10 scores et on enlève le score et le nom de trop si besoin
            if (listScore.size() > 9) {
                listScore.remove(10);
                listNoms.remove(10);
            }

            // on réécrit le fichier scores.txt avec le nouveau score et le nouveau nom
            try {
                FileWriter wr = new FileWriter("scores.txt");
                BufferedWriter writer = new BufferedWriter(wr);

                for (int i=0; i<listScore.size(); i++)
                    writer.append(listScore.get(i) + " " + listNoms.get(i) + "\n");

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

    public int getNbPoissonsSpeciaux() { return this.nbPoissonsSpeciaux; }

    public int getNiveau() { return this.niveau; }

    public Bulles getBulles() { return this.bulles; }

    public ArrayList<Poisson> getPoissons() { return this.poissons; }

    public int getScore() { return this.score; }

    public int getNbViesMax() { return this.nbViesMax; }

    public int getxImage1Score() { return this.xImage1Score; }

    public int getTailleImgScore() { return this.tailleImgScore; }

    public int getEspacementImgScore() { return this.espacementImgScore; }

    public Image[] getImageScore() { return this.imagesScore; }
}
