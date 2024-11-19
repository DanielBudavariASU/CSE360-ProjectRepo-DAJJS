package trial;

import java.time.LocalDateTime;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The ResetPasswordPage class handles the user interface for resetting user passwords.
 * It validates user information, allows OTP verification, and provides the option to reset the password.
 */
public class ResetPasswordPage extends Application {

    private CardPane cardPane;  // UI component to manage different panels
    private Database db = Driver.getDb();  // Access to the database
    private Admin admin;  // Administrator handling password reset
    private User foundUser;  // The user whose password needs to be reset
    private String username;  // Username of the user

    /**
     * Constructor initializes an admin object for handling password resets.
     */
    public ResetPasswordPage() {
        String tusername = "Admin";
        Password pass = new Password();
        String temppass = " ";
        this.admin = new Admin(tusername, temppass);
    }

    /**
     * Starts the application by setting up the primary stage and showing the card pane.
     *
     * @param primaryStage The main stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Reset Password");

        cardPane = new CardPane();  // Initialize card pane
        Scene scene = new Scene(cardPane, 400, 900);  // Set the scene dimensions

        primaryStage.setScene(scene);  // Add the scene to the primary stage
        primaryStage.show();  // Show the stage
    }

    /**
     * Inner class CardPane defines the different UI panels (user validation and reset password).
     */
    class CardPane extends StackPane {

        /**
         * Constructor adds the initial user validation panel.
         */
        public CardPane() {
            getChildren().add(createUserValidation());  // Add the user validation panel initially
        }

        /**
         * Creates a panel for validating the user's identity (username).
         *
         * @return A VBox containing the user validation UI components.
         */
        private VBox createUserValidation() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField usernameField = new TextField();  // Field to enter username
            Button loginButton = new Button("Login");  // Login button

            // Handle login button action
            loginButton.setOnAction(e -> {
                foundUser = db.findUserByUsername(usernameField.getText());  // Find user by username

                if (foundUser != null && foundUser.doesPasswordNeedReset()) {
                    foundUser.passwordNeedReset(false);  // Reset password flag
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password needs to be reset! Redirecting..");
                    alert.showAndWait();
                    
                    getChildren().clear();
                    getChildren().add(createWelcomePanel());  // Navigate to welcome panel
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The user either doesn't exist or does not need an OTP!");
                    alert.showAndWait();
                    
                    Stage stage = new Stage(); 
                    Driver createAccount = new Driver();  // Redirect to create account
                    
                    try {
                        createAccount.start(stage);  // Start CreateAccount stage
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Add components to panel
            panel.getChildren().addAll(
                new Label("Username:"), 
                usernameField,
                loginButton
            );

            return panel;  // Return the panel
        }

        /**
         * Creates a panel that allows users to reset their password after OTP validation.
         *
         * @return A VBox containing the password reset UI components.
         */
        private VBox createWelcomePanel() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            PasswordField tempPasswordField = new PasswordField();  // Temporary password field (OTP)
            PasswordField passwordField = new PasswordField();  // New password field
            PasswordField confirmPasswordField = new PasswordField();  // Confirm new password field
            Button submitButton = new Button("Create Account");  // Submit new password
            Button verify = new Button("Verify OTP");  // OTP verification button

            // Add components to panel
            panel.getChildren().addAll(
                new Label("Temporary Password:"), 
                tempPasswordField,
                verify,
                new Label("New Password:"), 
                passwordField,
                new Label("Confirm Password:"), 
                confirmPasswordField,
                submitButton
            );

            // Handle OTP verification button action
            verify.setOnAction(e -> {
                if (foundUser.validateLogin(tempPasswordField.getText()) && foundUser.validateExipration()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please enter your new password!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password is expired or does not match our records!");
                    alert.showAndWait();
                    
                    Stage stage = new Stage(); 
                    Driver createAccount = new Driver();  // Redirect to create account
                    
                    try {
                        createAccount.start(stage);  // Start CreateAccount stage
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Handle password submission and reset
            submitButton.setOnAction(e -> {
                if (passwordField.getText().equals(confirmPasswordField.getText())) {
                    admin.resetUserPassword(passwordField.getText(), foundUser.getUsername(), db);  // Reset user's password
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "New password is: " + passwordField.getText());
                    alert.showAndWait();
                    System.out.println(foundUser.validateLogin(passwordField.getText()));  // Confirm password reset
                    
                    Stage stage = new Stage(); 
                    Driver createAccount = new Driver();  // Redirect to create account
                    
                    try {
                        createAccount.start(stage);  // Start CreateAccount stage
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match!");
                    alert.showAndWait();
                }
            });

            return panel;  // Return the panel
        }
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}