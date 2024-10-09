package trial;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserHomePage extends Application{
	private CardPane cardPane;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User/Instructor Homepage");

        cardPane = new CardPane();
        Scene scene = new Scene(cardPane, 400, 900);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class CardPane extends StackPane {
        public CardPane() {
            getChildren().add(createWelcomePanel());
        }

        private VBox createWelcomePanel() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Label welcomeLabel = new Label("Welcome to User/Instructor Homepage");
            Button logoutButton = new Button("Logout");

            logoutButton.setOnAction(e -> {
                // Switch to login page
                Stage stage = (Stage) logoutButton.getScene().getWindow(); // Get current stage
                Driver createAccount = new Driver(); // Create an instance of CreateAccount
                try {
                    createAccount.start(stage); // Start CreateAccount stage (login page)
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            panel.getChildren().addAll(welcomeLabel, logoutButton);
            return panel;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}