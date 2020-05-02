import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Modele {
    private int width, height;
    private final int scoreMaxParNiveau = 5;

    public Modele(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void mourir() {
        System.out.println("T'es mort !!!");
    }

    public boolean isSortiEcran(Poisson poisson) {
        // si le poisson va vers la droite
        if (poisson.getDirection()) {
            return (poisson.getX() > width);
        } else { // si le poisson va vers la gauche
            return (poisson.getX() < -poisson.getTaille());
        }
    }


    public void tirer(double xTir, double yTir, Jeu jeu) {
        System.out.println("on tire en ("+xTir+","+yTir+")");

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

    public void update(Jeu jeu, double dt) {
         double deltaTLevel = jeu.getDeltaLvl();
         int nbPoissons = jeu.getNbPoissons();
         int nbPoissonsSpeciaux = jeu.getNbPoissonsSpecial();
         int niveau = jeu.getNiveau();
         Bulles bulles = jeu.getBulles();
         ArrayList<Poisson> poissons = jeu.getPoissons();

        jeu.setDeltaTLvl(jeu.getDeltaLvl() + dt);

        if (deltaTLevel > 3*nbPoissons)
            jeu.ajouterPoisson();

        if ((deltaTLevel > 5*nbPoissonsSpeciaux) && (niveau > 1))
            jeu.ajouterPoissonSpecial();

        bulles.update(dt);


        ArrayList<Poisson> poissonsSortis = new ArrayList<Poisson>();
        // on vérifie si le poisson est sorti de l'écran
        for (Poisson poisson : poissons) {
            poisson.update(dt);
            if (isSortiEcran(poisson)) {
                poissonsSortis.add(poisson);
                jeu.baisserVie();
            }
        }

        // on retire tous les poissons sortis de l'écran
        for (Poisson poisson : poissonsSortis) {
            poissons.remove(poisson);
        }
    }

    public void draw(GraphicsContext context, Jeu jeu) {
        Bulles bulles = jeu.getBulles();
        ArrayList<Poisson> poissons = jeu.getPoissons();

        bulles.draw(context);
        for (Poisson poisson : poissons)
            poisson.draw(context);
    }
}
