import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * @author Mathilde Prouvost et Augustine Poirier
 */
public class FishHunt extends Application {
    private final int width = 640, height = 480;
	
	private final Jeu jeu = new Jeu(width, height, this);
	
	private Stage primaryStage;
    
    private final int tailleMoitieCible = 50/2;
    private final int tailleBalleTir = 50, vitesseTirCible = 300;
    
    private boolean son = false, bruit = false;
	
	/**
	 * Fonction main principale qui lance l'application
	 * @param args
	 */
	public static void main(String[] args) { launch(args); }
	
	/**
	 * lancement de l'application avec une scène de menu
	 * @param primaryStage
	 */
    @Override
    public void start(Stage primaryStage) {
    	
    	this.primaryStage = primaryStage;
    	
        primaryStage.setTitle("Fish Hunt");
        primaryStage.setResizable(false);

        Image icone = new Image("/images/cible.png");
        primaryStage.getIcons().add(icone);
	
	    primaryStage.setScene(creerSceneMenu());

        primaryStage.show();
    }
	
	/**
	 * La scène du menu permet de choisir entre les différentes scènes
	 * On a ajouté un bouton pour activer/désactiver le son et les bruitages (par défaut, ils sont éteints)
	 * @return la scène du menu
	 */
	private Scene creerSceneMenu(){
		//par défaut, les effets sonores sont désactivés
		this.son = false;
		this.bruit = false;
	
	    VBox rootMenu = new VBox();
		Scene sceneMenu = new Scene(rootMenu, width, height);
	 
		//root
	    BackgroundFill fillFondBleu = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
	    Background fondBleu = new Background(fillFondBleu);
	    rootMenu.setBackground(fondBleu);
	    rootMenu.setAlignment(Pos.CENTER);
	    rootMenu.setSpacing(10);
		
	    //image du logo
		Image logo = new Image("/images/logo.png");
	    ImageView logoMenu = new ImageView(logo);
	    rootMenu.getChildren().add(logoMenu);
	
	    //nouvelle partie
	    Button boutonNouvellePartie = new Button("Nouvelle Partie!");
	    boutonNouvellePartie.setOnMouseClicked((click) -> {
		    primaryStage.setScene(creerSceneJeu());
	    });
	    rootMenu.getChildren().add(boutonNouvellePartie);
	
	    //meilleurs scores
	    Button boutonMeilleursScores = new Button ("Meilleurs Scores");
	    boutonMeilleursScores.setOnAction((click) -> {
	    	primaryStage.setScene(creerSceneScores());
	    });
	    rootMenu.getChildren().add(boutonMeilleursScores);
	    
	    //effets sonores
	    HBox effetsSonores = new HBox();
	    effetsSonores.setAlignment(Pos.CENTER);
	    effetsSonores.setSpacing(10);
		rootMenu.getChildren().add(effetsSonores);
		
	    //son (musique)
		CheckBox boutonSon = new CheckBox("Activer le son");
		boutonSon.setTextFill(Color.WHITE);
		boutonSon.setOnAction((click) -> {
			son = !son;
		});
		effetsSonores.getChildren().add(boutonSon);
		
		//bruit (effets spéciaux)
		CheckBox boutonBruit = new CheckBox("Activer les bruits");
		boutonBruit.setTextFill(Color.WHITE);
		boutonBruit.setOnAction((click) -> {
			bruit = !bruit;
		});
		effetsSonores.getChildren().add(boutonBruit);
	 
		//raccourcis
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
	 * La scène de jeu est celle où passent les poissons qu'il faut tirer avec la souris
	 * @return la scène du jeu
	 */
	private Scene creerSceneJeu() {
		jeu.commencer();
		
		Pane rootJeu = new Pane();
		Scene sceneJeu = new Scene(rootJeu, width, height);
		
		//root
		BackgroundFill fillFondBleu = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
		Background fondBleu = new Background(fillFondBleu);
		rootJeu.setBackground(fondBleu);
		
		//canvas
		Canvas canvas = new Canvas(width, height);
		GraphicsContext context = canvas.getGraphicsContext2D();
		rootJeu.getChildren().add(canvas);
		
		//gestion des bulles
		AnimationTimer timerJeu = new AnimationTimer() {
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
					jeu.creerBulles();
					nbBulles += 1;
				}
				jeu.update(dt);
				jeu.draw(context);

				lastTime = now;
			}
		};
		timerJeu.start();
		
		//image de la cible
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
		
		//action de tirer
		sceneJeu.setOnMouseClicked(click -> {
			double xTir = click.getX(), yTir = click.getY();
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
					
					if (deltaT > (double)tailleBalleTir/vitesseTirCible) {
						jeu.tirer(xTir, yTir);
						stop();
					}
					lastTime = now;
				}
			};
			timerShoot.start();
		});
		
		//raccourcis debug
		sceneJeu.setOnKeyPressed(e -> {
			switch (e.getCode()){
				case ESCAPE:
					timerJeu.stop();
					primaryStage.setScene(creerSceneMenu());
					break;
				case H:
					jeu.monterNiveau();
					break;
				case J:
					jeu.monterScore();
					break;
				case K:
					jeu.monterVie();
					break;
				case G:
					jeu.baisserVie();
					break;
				case L:
					jeu.mourir();
					timerJeu.stop();
					break;
			}
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
		
		//root
		rootScores.setAlignment(Pos.CENTER);
		rootScores.setSpacing(10);
		
		//titre
		Text titre = new Text("Meilleurs scores");
		titre.setFont(new Font(28));
		titre.setFill(Color.BLACK);
		rootScores.getChildren().add(titre);
		
		//Retour au menu
		Button boutonMenu = new Button("Menu");
		boutonMenu.setOnAction(e -> {
			primaryStage.setScene(creerSceneMenu());
		});
		rootScores.getChildren().add(boutonMenu);
		
		//raccouris
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

    void mourir() {

		BorderPane rootGameOver = new BorderPane();
		Scene sceneGameOver = new Scene(rootGameOver, width, height);

		//root
		BackgroundFill fillFondNoir = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
		Background fondNoir = new Background(fillFondNoir);
		rootGameOver.setBackground(fondNoir);

		Text gameOverText = new Text("Game Over\n( ͡° ʖ̯ ͡°)");
		gameOverText.setFill(Color.WHITE);
		gameOverText.setTextAlignment(TextAlignment.CENTER);
		gameOverText.setFont(new Font("Comic Sans MS", 80));
		rootGameOver.setCenter(gameOverText);

		primaryStage.setScene(sceneGameOver);

		AnimationTimer gameOver = new AnimationTimer() {
			double startTime;

			@Override
			public void handle(long now) {
				if (startTime == 0) {
					startTime = now;
					return;
				}

				if (now - startTime > 3e9) {
					primaryStage.setScene(creerSceneScores());
					stop();
				}
			}
		};
		gameOver.start();
	}
}
