import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tests extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        Scene scene = new Scene(root, 400, 150);

        Text titre = new Text("Ceci est un test !");
        root.getChildren().add(titre);
        root.getChildren().add(new Separator());

        HBox buttonGroup = new HBox();

        Button gauche = new Button("gauche");
        Button milieu = new Button("milieu");
        Button droite = new Button("droite");

        buttonGroup.getChildren().add(gauche);
        buttonGroup.getChildren().add(milieu);
        buttonGroup.getChildren().add(droite);
        buttonGroup.setAlignment(Pos.CENTER);

        root.getChildren().add(buttonGroup);
        root.getChildren().add(new Separator());

        CheckBox checkBox = new CheckBox("Peux tu cocher ça ?");

        root.getChildren().add(checkBox);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        primaryStage.setTitle("Salut!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
