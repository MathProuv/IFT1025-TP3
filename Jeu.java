import static java.lang.Math.pow;

public class Jeu {
    private final int width, height;
    private final Modele modele;
    
    private final int nbViesMax = 3;
    
    private int niveau = 0;
    private int score = 0;
    private int nbVies = nbViesMax;
    private double vitesse;
    
    Jeu(int width, int height, Modele modele){
        this.width = width;
        this.height = height;
        this.modele = modele;
        monterNiveau();
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
        if (nbVies<nbViesMax)
            this.nbVies ++;
        System.out.println("nbVie = " + this.nbVies);
    }
}
