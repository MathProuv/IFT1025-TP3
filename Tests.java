import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * @author Mathilde Prouvost et Augustine Poirier
 */
public class Tests extends Application {
    
    private Stage primaryStage;
    private ArrayList<Integer> arrayInt = new ArrayList<Integer>();
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        this.primaryStage = primaryStage;
        
        primaryStage.setScene(creerSceneA());
        
        primaryStage.show();

        for (int nombre : arrayInt) {
            System.out.println(nombre);
        }
    }
    
    private Scene creerSceneA() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        Scene sceneA = new Scene(root, 200, 200);
        
        Text titre = new Text("Scène A");
        
        Button btn = new Button("Changer de scène");
        btn.setOnAction((e) -> {
            primaryStage.setScene(creerSceneB());
        });
        
        Button btnAC = new Button("Scène C");
        btnAC.setOnAction(e -> {
            primaryStage.setScene(creerSceneC());
        });
        
        root.getChildren().addAll(titre, new Separator(), btn, btnAC);
        
        return sceneA;
    }
    
    private Scene creerSceneB() {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        Scene sceneB = new Scene(root, 200, 200);
        
        Text titre = new Text("Scène B");
        
        Button btn = new Button("Scène A");
        btn.setOnAction((e) -> {
            primaryStage.setScene(creerSceneA());
        });
        
        Button btnBC = new Button("Scène C");
        btnBC.setOnAction(e -> {
            primaryStage.setScene(creerSceneC());
        });
        
        root.getChildren().addAll(titre, new Separator(Orientation.VERTICAL), btn, btnBC);
        
        return sceneB;
    }
    
    private Scene creerSceneC() {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        Scene sceneC = new Scene(root, 200, 200);
    
        Text titre = new Text("Scène C");
        
        Button btnA = new Button("Scène A");
        btnA.setOnAction(e -> {
            primaryStage.setScene(creerSceneA());
        });
    
        Button btnB = new Button("Scène B");
        btnB.setOnAction(e -> {
            primaryStage.setScene(creerSceneB());
        });
    
        root.getChildren().addAll(titre, new Separator(Orientation.VERTICAL), btnA, btnB);
    
        return sceneC;
    }
}
