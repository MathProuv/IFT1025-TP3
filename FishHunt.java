import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Mathilde Prouvost et Augustine Poirier
 */
public class FishHunt extends Application {
    private final int width = 640, height = 480;
    private final int tailleMoitieCible = 50/2;
    private final int tailleBalleTir = 50, vitesseTirCible = 300;
    private Stage primaryStage;

    private Controleur controleur = new Controleur(width, height);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	
    	this.primaryStage = primaryStage;
    	
    	primaryStage.setScene(creerSceneMenu());
    	
        primaryStage.setTitle("Fish Hunt");
        primaryStage.setResizable(false);

        Image icone = new Image("/images/cible.png");
        primaryStage.getIcons().add(icone);

        primaryStage.show();
    }
	
	/**
	 *
	 * @return la scène du menu
	 */
	private Scene creerSceneMenu(){
	
	    VBox rootMenu = new VBox();
	    
	    BackgroundFill fillFondBleu = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
	    Background fondBleu = new Background(fillFondBleu);
	    rootMenu.setBackground(fondBleu);
	    Scene sceneMenu = new Scene(rootMenu, width, height);
	
	    rootMenu.setAlignment(Pos.CENTER);
	    rootMenu.setSpacing(10);
	    Image logo = new Image("/images/logo.png");
	    ImageView logoMenu = new ImageView(logo);
	    rootMenu.getChildren().add(logoMenu);
	
	    Button boutonNouvellePartie = new Button("Nouvelle Partie!");
	    boutonNouvellePartie.setOnMouseClicked(e -> {
		    primaryStage.setScene(creerSceneJeu());
	    });
	    rootMenu.getChildren().add(boutonNouvellePartie);
	
	    Button boutonMeilleursScores = new Button ("Meilleurs Scores");
	    boutonMeilleursScores.setOnAction(e -> {
	    	primaryStage.setScene(creerSceneScores());
	    });
	    rootMenu.getChildren().add(boutonMeilleursScores);
	    
	    sceneMenu.setOnKeyPressed(e -> {
		    switch (e.getCode()){
			    case ESCAPE:
				    Platform.exit();
				    break;
			    case N:
				    primaryStage.setScene(creerSceneJeu());
				    break;
		    }
	    });
	
	    return sceneMenu;
    }
	
	/**
	 *
	 * @return la scène du jeu
	 */
	private Scene creerSceneJeu() {
		Pane rootJeu = new Pane();
		
		BackgroundFill fillFondBleu = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
		Background fondBleu = new Background(fillFondBleu);
		rootJeu.setBackground(fondBleu);
		Scene sceneJeu = new Scene(rootJeu, width, height);
		
		Canvas canvas = new Canvas(width, height);
		GraphicsContext context = canvas.getGraphicsContext2D();
		rootJeu.getChildren().add(canvas);
		
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
				controleur.draw(context);
				
				lastTime = now;
			}
		};
		timerBulles.start();
		
		sceneJeu.setOnMouseClicked(e -> {
			double xTir = e.getX(), yTir = e.getY();
			controleur.clique(xTir, yTir);
			AnimationTimer timerShoot = new AnimationTimer() {
				private long startTime = 0;
				private long lastTime = 0;
				private double rayonTir = tailleBalleTir;
				@Override
				public void handle(long now) {
					if (startTime == 0 || lastTime == 0){
						startTime = now;
						lastTime = now;
						return;
					}
					
					double deltaT = (now - startTime)*1e-9;
					double dt = (now - lastTime)*1e-9;
					
					context.setFill(Color.BLACK);
					context.fillOval(xTir-rayonTir/2.0, yTir-rayonTir/2.0, rayonTir, rayonTir);
					rayonTir -= vitesseTirCible * dt;
					
					if (deltaT > (double)tailleBalleTir/vitesseTirCible){
						startTime = 0;
						lastTime = 0;
						rayonTir = tailleBalleTir;
						stop();
					}
					lastTime = now;
				}
			};
			timerShoot.start();
		});
		
		sceneJeu.setOnKeyPressed(e -> {
			switch (e.getCode()){
				case ESCAPE:
					timerBulles.stop();
					primaryStage.setScene(creerSceneMenu());
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
				case G:
					controleur.baisserVie();
					break;
				case L:
					controleur.mourir();
					timerBulles.stop();
					primaryStage.setScene(creerSceneScores());
					break;
			}
		});
		
		Image imgCible = new Image("/images/cible.png");
		ImageView imageViewCible = new ImageView(imgCible);
		imageViewCible.setFitHeight(2 * tailleMoitieCible);
		imageViewCible.setFitWidth(2 * tailleMoitieCible);
		rootJeu.getChildren().add(imageViewCible);
		
		rootJeu.setOnMouseMoved((event) -> {
			double x = event.getX() - tailleMoitieCible;
			double y = event.getY() - tailleMoitieCible;
			imageViewCible.setX(x);
			imageViewCible.setY(y);
		});
		
		return sceneJeu;
	}
	
	/**
	 *
	 * @return la scène des scores
	 */
	private Scene creerSceneScores() {
		
		VBox rootScores = new VBox();
		Scene sceneScores = new Scene(rootScores, width, height, Color.GRAY);
		
		Button boutonMenu = new Button("Menu");
		boutonMenu.setOnAction(e -> {
			primaryStage.setScene(creerSceneMenu());
		});
		rootScores.getChildren().add(boutonMenu);
		
		sceneScores.setOnKeyPressed(e -> {
			switch (e.getCode()){
				case ESCAPE:
					Platform.exit();
					break;
				case N:
					primaryStage.setScene(creerSceneJeu());
			}
		});
		
		return sceneScores;
    }
    
}
