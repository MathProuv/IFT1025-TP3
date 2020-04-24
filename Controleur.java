public class Controleur {
    private int width, height;
    private Jeu jeu;
    private Modele modele;

    public Controleur(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void clique(double x, double y) {
        System.out.println("on tire en ("+x+","+y+")");
    }

    public void mourir() {
        System.out.println("T'es mort !!!");
    }
}
