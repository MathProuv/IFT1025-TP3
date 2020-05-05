import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * @autor Mathilde Prouvost et Augustine Poirier
 */
public class Modele {
    private int width, height;
    private final int scoreMaxParNiveau = 5, periodePoisson = 3, periodePoissonSpecial = 5, debutPoissonSpecial = 2;

    /**
     * Constructeur du modèle
     *
     * @param width la largeur de la fenêtre
     * @param height la hauteur de la fenêtre
     */
    public Modele(int width, int height){
        this.width = width;
        this.height = height;
    }


    /**
     * Vérifie si un poisson donné est sorti de la fenêtre
     *
     * @param poisson le poisson dont on vérifie la position
     * @return booléen : true si sorti, false sinon
     */
    public boolean isSortiEcran(Poisson poisson) {
        // si le poisson va vers la droite
        if (poisson.getDirection()) {
            return (poisson.getX() > width);
        } else { // si le poisson va vers la gauche
            return (poisson.getX() < -poisson.getTaille());
        }
    }

    /**
     * Méthode pour tirer qui retire un poisson s'il est touché
     *
     * @param xTir coordonnée x du tir
     * @param yTir coordonnée y du tir
     * @param jeu l'instance actuelle de jeu
     */
    public void tirer(double xTir, double yTir, Jeu jeu) {

        ArrayList<Poisson> poissons = jeu.getPoissons();
        ArrayList<Poisson> poissonsMorts = new ArrayList<Poisson>();


        for (Poisson poisson : poissons) {
            double x = poisson.getX();
            double y = poisson.getY();
            int taille = poisson.getTaille();

            // si le tir est à l'intérieur du carré qui représente le poisson
            boolean intersect = ((xTir >= x && xTir <= x+taille) && (yTir >= y && yTir <= y+taille));

            // si on tire sur le poisson, le score augmente et le poisson sera retiré
            if (intersect) {
                poissonsMorts.add(poisson);
                jeu.monterScore();;
            }
        }

        // on retire tous les poissons qu'on a tués
        for (Poisson poissonMort : poissonsMorts)
            poissons.remove(poissonMort);
    }

    /**
     * Update du modèle
     *
     * @param jeu l'instance actuelle de jeu
     * @param dt durée entre 2 frames
     */
    public void update(Jeu jeu, double dt) {
         double deltaTLevel = jeu.getDeltaLvl();
         int nbPoissons = jeu.getNbPoissons();
         int nbPoissonsSpeciaux = jeu.getNbPoissonsSpeciaux();
         int niveau = jeu.getNiveau();
         Bulles bulles = jeu.getBulles();
         ArrayList<Poisson> poissons = jeu.getPoissons();

        // on génère un poisson à chaque 3 secondes
        if (deltaTLevel > periodePoisson*nbPoissons)
            jeu.ajouterPoisson();

        // on génère un poisson spécial à chaque 5 secondes à partir du niveau 2
        if ((deltaTLevel > periodePoissonSpecial*nbPoissonsSpeciaux) && (niveau >= debutPoissonSpecial))
            jeu.ajouterPoissonSpecial();

        bulles.update(dt);


        ArrayList<Poisson> poissonsSortis = new ArrayList<Poisson>();

        // on vérifie si le poisson est sorti de l'écran
        for (Poisson poisson : poissons) {
            poisson.update(dt);
            if (isSortiEcran(poisson)) {
                // s'il est sorti, on l'enlève et le joueur perd une vie
                poissonsSortis.add(poisson);
                jeu.baisserVie();
            }
        }

        // on retire tous les poissons sortis de l'écran
        for (Poisson poisson : poissonsSortis) {
            poissons.remove(poisson);
        }

        // on incrémente deltaTLvl
        jeu.setDeltaTLvl(jeu.getDeltaLvl() + dt);
    }

    /**
     * Méthode draw du modèle
     *
     * @param context le context du canvas du jeu
     * @param jeu l'instance actuelle de jeu
     */
    public void draw(GraphicsContext context, Jeu jeu) {
        Bulles bulles = jeu.getBulles();
        ArrayList<Poisson> poissons = jeu.getPoissons();

        // on dessine les bulles
        bulles.draw(context);

        // on dessine les poissons
        for (Poisson poisson : poissons)
            poisson.draw(context);
    }

}
