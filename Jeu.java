import static java.lang.Math.pow;

public class Jeu {
    private int width, height;

    private final int nbViesMax = 3;

    private int nbVies;
    private int level;
    private int score;
    private double vitesse;

    Jeu(int width, int height){
        this.width = width;
        this.height = height;
        this.score = 0;
        this.nbVies = nbViesMax;
        this.level = 0;
        augmenterLevel();
    }

    public void augmenterLevel(){
        this.level ++;
        this.vitesse = 100*pow(this.level,1/3)+200;
    }


}
