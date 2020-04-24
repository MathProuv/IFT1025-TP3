import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FishHunt extends Application {
    private final int width = 640, height = 480;

    private Controleur controleur;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.controleur = new Controleur(width, height);

        // ***** MENU *********************************

        Pane rootMenu = new Pane();
        Scene sceneMenu = new Scene(rootMenu, width, height, Color.rgb(0,25,99));
        
        VBox menu = new VBox();
        Image logo = new Image("/images/logo.png");
        ImageView logoMenu = new ImageView(logo);
        menu.getChildren().add(logoMenu);

        Button boutonNouvellePartie = new Button("Nouvelle Partie!");
        menu.getChildren().add(boutonNouvellePartie);

        Button boutonMeilleursScores = new Button ("Meilleurs Scores");
        menu.getChildren().add(boutonMeilleursScores);

        rootMenu.getChildren().add(menu);

        primaryStage.setScene(sceneMenu);


        // ***** JEU **********************************

        Pane rootJeu = new Pane();
        Scene sceneJeu = new Scene(rootJeu, width, height);

        Canvas canvas = new Canvas(width, height);
        rootJeu.getChildren().add(canvas);
        canvas.getGraphicsContext2D().setFill(Color.rgb(0, 25, 99));
        canvas.getGraphicsContext2D().fillRect(0,0,width,height);

        sceneJeu.setOnMouseClicked(e -> {
            controleur.clique(e.getX(), e.getY());
        });

        // ***** MEILLEURS SCORES **************************

        VBox rootScores = new VBox();
        Scene sceneScores = new Scene(rootScores, width, height, Color.GRAY);

        Button boutonMenu = new Button("Menu");
        rootScores.getChildren().add(boutonMenu);


        // ***** Actions ********************************

        boutonNouvellePartie.setOnMouseClicked(e -> { primaryStage.setScene(sceneJeu); });
        boutonMeilleursScores.setOnMouseClicked(e -> { primaryStage.setScene(sceneScores); });
        boutonMenu.setOnMouseClicked(e -> { primaryStage.setScene(sceneMenu); });

        sceneMenu.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case ESCAPE:
                    Platform.exit();
            }
        });
        sceneJeu.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case ESCAPE:
                    Platform.exit();
                    break;
                case L:
                    controleur.mourir();
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

        primaryStage.setTitle("Fish Hunt");
        primaryStage.setResizable(false);

        Image logoCible = new Image("/images/cible.png");
        primaryStage.getIcons().add(logoCible);

        primaryStage.show();
    }
}
