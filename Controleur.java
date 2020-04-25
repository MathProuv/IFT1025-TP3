import javafx.scene.canvas.GraphicsContext;

public class Controleur {
    private int width, height;
    private final Modele modele = new Modele(width, height);
    private final Jeu jeu = new Jeu(width, height, modele);
    private Bulles bulles;

    public Controleur(int width, int height){
        this.width = width;
        this.height = height;
        //this.bulles = new Bulles(width, height);
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
    

    public void clique(double x, double y) {
        System.out.println("on tire en ("+x+","+y+")");
    }

    public void mourir() {
        System.out.println("T'es mort !!!");
    }

    public void monterNiveau() {
        jeu.monterNiveau();
    }

    public void monterScore() {
        jeu.monterScore();
    }

    public void monterVie() {
        jeu.monterVie();
    }
}
