import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Mathilde Prouvost et Augustine Poirier
 */
public class FishHunt extends Application {
	private final int width = 640, height = 480, periodeBulles = 3;

	private final Jeu jeu = new Jeu(width, height, this);

	private Stage primaryStage;

	private final int tailleMoitieCible = 50 / 2;
	private final int tailleBalleTir = 50, vitesseTirCible = 300;

	private boolean son = false, bruit = false;

	private Canvas canvas = new Canvas(width, height);
	private GraphicsContext context = canvas.getGraphicsContext2D();

	public AnimationTimer timerJeu = new AnimationTimer() {
		private long startTime = 0;
		private long lastTime = 0;
		private int nbBulles = 0;
		
		@Override
		public void start() {
			super.start();
			// chaque réinitialisation du timer correspond à un nouveau niveau, on doit donc réinitialiser
			// le startTime à 0
			startTime = 0;
		}


		@Override
		public void handle(long now) {
			if (startTime == 0 || lastTime == 0) {
				startTime = now;
				lastTime = now;
				return;
			}

			double deltaT = (now - startTime) * 1e-9;
			double dt = (now - lastTime) * 1e-9;

			// les bulles sont crées à chaque 3 secondes à partir du début de la partie
			if (deltaT > nbBulles * periodeBulles) {
				jeu.ajouterBulles();
				nbBulles += 1;
			}
			jeu.update(dt);
			jeu.draw(context);

			lastTime = now;
		}
	};

	/**
	 * Fonction main qui lance l'application
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Méthode pour lancer l'application avec une scène de menu
	 *
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
	 * Méthode pour créer la scène du menu, qui permet de choisir entre les différentes scènes
	 * Possède un bouton pour activer/désactiver les bruitages (par défaut, ils sont éteints)
	 *
	 * @return la scène du menu
	 */
	private Scene creerSceneMenu() {
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
			primaryStage.setScene(creerSceneJeu(true));
		});
		rootMenu.getChildren().add(boutonNouvellePartie);

		//meilleurs scores
		Button boutonMeilleursScores = new Button("Meilleurs Scores");
		boutonMeilleursScores.setOnAction((click) -> {
			try {
				primaryStage.setScene(creerSceneScores());
			} catch (FileNotFoundException e) {
				System.out.println("Erreur de lecture du fichier");
				;
			}
		});
		rootMenu.getChildren().add(boutonMeilleursScores);

		//effets sonores
		HBox effetsSonores = new HBox();
		effetsSonores.setAlignment(Pos.CENTER);
		effetsSonores.setSpacing(10);
		rootMenu.getChildren().add(effetsSonores);

		//bruit (effets spéciaux)
		CheckBox boutonEffetsSonores = new CheckBox("Activer les bruits");
		boutonEffetsSonores.setTextFill(Color.WHITE);
		boutonEffetsSonores.setOnAction((click) -> {
			bruit = !bruit;
		});
		effetsSonores.getChildren().add(boutonEffetsSonores);

		//raccourcis
		sceneMenu.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case ESCAPE:
					Platform.exit();
					break;
				case N:
					primaryStage.setScene(creerSceneJeu(true));
					break;
			}
		});

		return sceneMenu;
	}

	/**
	 * Méthode pour créer la scène de jeu, qui contient les poissons, les bulles, le score, les vies
	 * dans laquelle on peut tirer
	 *
	 * @param debut booléen true si on est au début du jeu, false si on est au début d'un niveau
	 * @return la scène du jeu
	 */
	private Scene creerSceneJeu(boolean debut) {

		if (debut)
			jeu.commencer();

		// on commence le jeu
		timerJeu.start();

		Pane rootJeu = new Pane();
		Scene sceneJeu = new Scene(rootJeu, width, height);

		//root
		BackgroundFill fillFondBleu = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
		Background fondBleu = new Background(fillFondBleu);
		rootJeu.setBackground(fondBleu);

		//canvas
		rootJeu.getChildren().add(canvas);

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
					if (startTime == 0 || lastTime == 0) {
						startTime = now;
						lastTime = now;
						return;
					}

					double deltaT = (now - startTime) * 1e-9;
					double dt = (now - lastTime) * 1e-9;

					context.setFill(Color.BLACK);
					context.fillOval(xTir - rayonTir / 2.0, yTir - rayonTir / 2.0, rayonTir, rayonTir);
					rayonTir -= vitesseTirCible * dt;

					if (deltaT > (double) tailleBalleTir / vitesseTirCible) {
						jeu.tirer(xTir, yTir);
						stop();
					}
					lastTime = now;
				}
			};
			timerShoot.start();
		});

		//raccourcis pour débugger
		sceneJeu.setOnKeyPressed(e -> {
			switch (e.getCode()) {
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
					break;
			}
		});

		return sceneJeu;
	}

	/**
	 * Méthode pour créer la scène des meilleurs scores
	 *
	 * @return la scène des scores
	 */
	private Scene creerSceneScores() throws FileNotFoundException {

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

		//liste de scores
		ArrayList<String> fichierScores = new ArrayList<String>();

		try {
			Scanner scan = new Scanner(new FileInputStream("scores.txt"));
			int i = 1;
			// on va chercher tous les scores du fichier et on les ajoute dans l'ArrayList fichierScores
			while (scan.hasNextInt()) {
				int scoreFichier = scan.nextInt();
				String nomFichier = scan.nextLine().substring(1);
				String temp = "#" + i + " - " + nomFichier + " - " + scoreFichier + "";
				fichierScores.add(temp);
				i++;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Erreur de lecture du fichier");
		}

		// on affiche les meilleurs scores
		ObservableList<String> scores = FXCollections.observableArrayList(fichierScores);
		ListView<String> scoreListView = new ListView<String>(scores);
		rootScores.getChildren().add(scoreListView);
		scoreListView.setMaxSize(0.85 * width, 0.75 * height);

		// si le score est dans le top 10, on demande au joueur de l'ajouter avec son nom
		if (jeu.isInTop10()) {
			int score = jeu.getScore();
			HBox inputNom = new HBox();
			inputNom.setAlignment(Pos.CENTER);

			TextField champNom = new TextField();
			Text texte = new Text();
			texte.setText("a fait " + score + " points!");
			Button ajouter = new Button("Ajouter");


			inputNom.getChildren().add(new Label("Votre nom : "));
			inputNom.getChildren().add(champNom);
			inputNom.getChildren().add(texte);
			inputNom.getChildren().add(ajouter);
			rootScores.getChildren().add(inputNom);

			ajouter.setOnAction((e) -> {
				String nom = champNom.getText();
				jeu.updateScore(nom);
			});
		}

		// retour au menu
		Button boutonMenu = new Button("Menu");
		boutonMenu.setOnAction(e -> {
			primaryStage.setScene(creerSceneMenu());
		});
		rootScores.getChildren().add(boutonMenu);

		// raccouris
		sceneScores.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case ESCAPE:
					Platform.exit();
					break;
				case N:
					primaryStage.setScene(creerSceneJeu(true));
			}
		});


		return sceneScores;
	}

	/**
	 * Méthode pour faire mourir le joueur
	 */
	void mourir() {

		timerJeu.stop();

		// si les bruits sont activés, on fait jouer le bruit de mort
		if (bruit) {
			File fileSon = new File("sounds/Mourir.m4a");
			Media sonMort = new Media(fileSon.toURI().toString());
			MediaPlayer sonMortPlayer = new MediaPlayer(sonMort);
			sonMortPlayer.play();
		}

		BorderPane rootGameOver = new BorderPane();
		Scene sceneGameOver = new Scene(rootGameOver, width, height);

		// root
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
					try {
						primaryStage.setScene(creerSceneScores());
					} catch (FileNotFoundException e) {
						System.out.println("Erreur de lecture du fichier");
						;
					}
					stop();
				}
			}
		};
		gameOver.start();
	}

	/**
	 * Méthode pour monter de niveau
	 *
	 * @param prochainNiveau le niveau vers lequel on monte
	 */
	void monterNiveau(int prochainNiveau) {
		timerJeu.stop();

		// si les bruits sont activés, on fait jouer le bruit de level up
		if (bruit) {
			File fileSon = new File("sounds/LevelUp.m4a");
			Media sonLevelUp = new Media(fileSon.toURI().toString());
			MediaPlayer sonLevelUpPlayer = new MediaPlayer(sonLevelUp);
			sonLevelUpPlayer.play();
		}

		BorderPane rootLvlUp = new BorderPane();
		Scene sceneMonterNiveau = new Scene(rootLvlUp, width, height);

		//root
		BackgroundFill fillFondBleu = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
		Background fondBleu = new Background(fillFondBleu);
		rootLvlUp.setBackground(fondBleu);
		Canvas canvasLvlUp = new Canvas(width, height);
		GraphicsContext contextLvlUp = canvasLvlUp.getGraphicsContext2D();

		contextLvlUp.setFont(new Font(30));
		contextLvlUp.setTextAlign(TextAlignment.CENTER);
		contextLvlUp.setFill(Color.RED);
		contextLvlUp.fillText(((Integer) jeu.getScore()).toString(), width / 2.0, 60);


		Text lvlUpText = new Text("Niveau " + prochainNiveau);
		lvlUpText.setFill(Color.WHITE);
		lvlUpText.setTextAlignment(TextAlignment.CENTER);
		lvlUpText.setFont(new Font(50));
		rootLvlUp.setCenter(lvlUpText);

		for (int i = 0; i < jeu.getNbViesMax(); i++) {
			int xImageI = jeu.getxImage1Score() + i * (jeu.getTailleImgScore() + jeu.getEspacementImgScore());
			contextLvlUp.drawImage(jeu.getImageScore()[i], xImageI, 100, jeu.getTailleImgScore(),
					jeu.getTailleImgScore());
		}

		rootLvlUp.getChildren().add(canvasLvlUp);

		primaryStage.setScene(sceneMonterNiveau);

		AnimationTimer lvlUpTimer = new AnimationTimer() {
			double startTime;

			@Override
			public void handle(long now) {
				if (startTime == 0) {
					startTime = now;
					return;
				}

				if (now - startTime > 3e9) {
					primaryStage.setScene(creerSceneJeu(false));
					stop();
				}
			}
		};
		lvlUpTimer.start();
	}


	/**
	 * Méthode pour tirer
	 */
	public void tirer() {
		// si les bruits sont activés, on fait jouer le bruit de tir
		if (bruit) {
			File fileSon = new File("sounds/Tir.m4a");
			Media sonTir = new Media(fileSon.toURI().toString());
			MediaPlayer sonTirPlayer = new MediaPlayer(sonTir);
			sonTirPlayer.play();
		}
	}
}
