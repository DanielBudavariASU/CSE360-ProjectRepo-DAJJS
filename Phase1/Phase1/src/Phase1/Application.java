package Phase1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BasicJavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a button
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        
        // Define action when button is clicked
        btn.setOnAction(event -> System.out.println("Hello World!"));

        // Create a layout (StackPane) and add the button to it
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        // Create a scene with the layout and set it on the stage
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        
        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the application
        launch(args);
    }
}
