// JavaFX Application with 6 Import Lines
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class program extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create label
        Label label = new Label("JavaFX Application Successfully Running!");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #0066cc; -fx-font-weight: bold;");
        
        // Create root pane
        StackPane root = new StackPane();
        root.setAlignment(label, Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to right, #f0f8ff, #e6f2ff);");
        root.getChildren().add(label);
        
        // Create scene
        Scene scene = new Scene(root, 600, 400);
        
        // Configure stage
        primaryStage.setTitle("JavaFX 6 Import Lines Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}