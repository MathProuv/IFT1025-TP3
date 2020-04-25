import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FishHunt extends Application {
    private final int width = 640, height = 480;
    private Stage primaryStage;

    private Controleur controleur;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.controleur = new Controleur(width, height);

        BackgroundFill fillFondBleu = new BackgroundFill(Color.rgb(0,0,138), CornerRadii.EMPTY, Insets.EMPTY);
        Background fondBleu = new Background(fillFondBleu);

        // ***** MENU *********************************

        VBox rootMenu = new VBox();
        rootMenu.setBackground(fondBleu);
        Scene sceneMenu = new Scene(rootMenu, width, height);

        rootMenu.setAlignment(Pos.CENTER);
        rootMenu.setSpacing(10);
        Image logo = new Image("/images/logo.png");
        ImageView logoMenu = new ImageView(logo);
        rootMenu.getChildren().add(logoMenu);

        Button boutonNouvellePartie = new Button("Nouvelle Partie!");
        rootMenu.getChildren().add(boutonNouvellePartie);

        Button boutonMeilleursScores = new Button ("Meilleurs Scores");
        rootMenu.getChildren().add(boutonMeilleursScores);


        // ***** JEU **********************************

        Pane rootJeu = new Pane();
        rootJeu.setBackground(fondBleu);
        Scene sceneJeu = new Scene(rootJeu, width, height);

        Canvas canvas = new Canvas(width, height);
        rootJeu.getChildren().add(canvas);

        sceneJeu.setOnMouseClicked(e -> {
            controleur.clique(e.getX(), e.getY());
        });
    
        
        AnimationTimer timerBulles = new AnimationTimer() {
            private long startTime = 0;
            private long lastTime = 0;
            private int nbBulles = 0;
            
            @Override
            public void handle(long now) {
                if (startTime == 0) {
                    startTime = now;
                    lastTime = now;
                    return;
                }
            
                double deltaT = (now - startTime)*1e-9;
                double dt = (now - lastTime)*1e-9;
            
                // les bulles sont crées à chaque 3 secondes à partir du début de la partie
                if (deltaT > nbBulles * 3) {
                    controleur.creerBulles();
                    nbBulles += 1;
                }
                controleur.update(dt);
                controleur.draw(canvas.getGraphicsContext2D());
                
                lastTime = now;
            }
        };

        // ***** MEILLEURS SCORES **************************

        VBox rootScores = new VBox();
        Scene sceneScores = new Scene(rootScores, width, height, Color.GRAY);

        Button boutonMenu = new Button("Menu");
        rootScores.getChildren().add(boutonMenu);


        // ***** Actions ********************************

        boutonNouvellePartie.setOnMouseClicked(e -> {
            primaryStage.setScene(sceneJeu);
            timerBulles.start();
        });
        boutonMeilleursScores.setOnMouseClicked(e -> { primaryStage.setScene(sceneScores); });
        boutonMenu.setOnMouseClicked(e -> { primaryStage.setScene(sceneMenu); });

        sceneMenu.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case ESCAPE:
                    Platform.exit();
                    break;
                case N:
                    primaryStage.setScene(sceneJeu);
                    break;
            }
        });
        sceneJeu.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case ESCAPE:
                    timerBulles.stop();
                    primaryStage.setScene(sceneMenu);
                    break;
                case H:
                    controleur.monterNiveau();
                    break;
                case J:
                    controleur.monterScore();
                    break;
                case K:
                    controleur.monterVie();
                    break;
                case L:
                    controleur.mourir();
                    timerBulles.stop();
                    primaryStage.setScene(sceneScores);
                    break;
            }
        });
        sceneScores.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case ESCAPE:
                    Platform.exit();
                    break;
            }
        });


        primaryStage.setScene(sceneMenu);

        primaryStage.setTitle("Fish Hunt");
        primaryStage.setResizable(false);

        Image icone = new Image("/images/cible.png");
        primaryStage.getIcons().add(icone);

        primaryStage.show();
    }
}
