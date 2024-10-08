package Phase1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class CreateAccount extends Application {

    private CardPane cardPane;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login System");

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

            Label welcomeLabel = new Label("Welcome to the Login System");
            Button createAccountButton = new Button("Create Account");
            Button loginButton = new Button("Login");

            createAccountButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(createInviteCodePanel());
            });

            loginButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(createLoginPanel());
            });

            panel.getChildren().addAll(welcomeLabel, createAccountButton, loginButton);
            return panel;
        }

        private VBox createInviteCodePanel() {
        	VBox panel = new VBox(10);
        	panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField inviteCodeField = new TextField();
            Button nextButton = new Button("Next");

            panel.getChildren().addAll(
                new Label("Enter Invite Code:"), inviteCodeField,
                nextButton
            );

            nextButton.setOnAction(e -> {
                String inviteCode = inviteCodeField.getText();
                // we can add logic to validate the invite code but i am not gonna do that :P
                    getChildren().clear();
                    getChildren().add(createSignupPanel());
               
            });

            return panel;
        }
        
        private VBox createSignupPanel() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField firstNameField = new TextField();
            TextField middleNameField = new TextField();
            TextField lastNameField = new TextField();
            TextField preferredNameField = new TextField();
            TextField emailField = new TextField();
            TextField usernameField = new TextField();
            PasswordField passwordField = new PasswordField();
            PasswordField confirmPasswordField = new PasswordField();
            Button submitButton = new Button("Create Account");

            panel.getChildren().addAll(
                new Label("First Name:"), firstNameField,
                new Label("Middle Name:"), middleNameField,
                new Label("Last Name:"), lastNameField,
                new Label("Preferred Name:"), preferredNameField,
                new Label("Email:"), emailField,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Confirm Password:"), confirmPasswordField,
                submitButton
            );

            submitButton.setOnAction(e -> {
                if (passwordField.getText().equals(confirmPasswordField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully!");
                    alert.showAndWait();
                    // Redirect to the login page
                    getChildren().clear();
                    getChildren().add(createLoginPanel());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match!");
                    alert.showAndWait();
                }
            });

            return panel;
        }

        private VBox createLoginPanel() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField usernameField = new TextField();
            PasswordField passwordField = new PasswordField();
            Button loginButton = new Button("Login");

            panel.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginButton
            );

            loginButton.setOnAction(e -> {
                // potential logic for authentication
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login successful!");
                alert.showAndWait();
                // redirects to role options
                //should add that in soon hopefully
            });

            return panel;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
