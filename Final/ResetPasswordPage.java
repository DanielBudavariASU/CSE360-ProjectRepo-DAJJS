package trial;
import java.time.LocalDateTime;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ResetPasswordPage extends Application{
	private CardPane cardPane;
	private Database db = Driver.getDb(); 
	private Admin admin;
	private User foundUser;
	private String username;
	
	public ResetPasswordPage() {
		 String tusername = "Admin";
	     Password pass = new Password();
	     String temppass = " ";
	     this.admin = new Admin(tusername, temppass);
    }

	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Reset Password");

        cardPane = new CardPane();
        Scene scene = new Scene(cardPane, 400, 900);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class CardPane extends StackPane {
        public CardPane() {
            getChildren().add(createUserValidation());
        }
        
        private VBox createUserValidation() {
        	VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField usernameField = new TextField();
            username = usernameField.getText();
            Button loginButton = new Button("Login");
                 
            loginButton.setOnAction(e -> {
            	foundUser = db.findUserByUsername(usernameField.getText());

                if (foundUser != null && foundUser.doesPasswordNeedReset() == true)
                {
                	foundUser.passwordNeedReset(false); //reset flag
                	Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password meeds to be reset! Redirecting..");
                	alert.showAndWait();
                	getChildren().clear();
                	getChildren().add(createWelcomePanel());

                }
                else
                {
                	Alert alert = new Alert(Alert.AlertType.ERROR, "The user either doesn't exists or does not at an OTP!");
                	alert.showAndWait();
                	Stage stage = new Stage(); 
                    Driver createAccount = new Driver(); // Create an instance of CreateAccount
                    try {
                        createAccount.start(stage); // Start CreateAccount stage (login page)
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            panel.getChildren().addAll(
                new Label("Username:"), usernameField,
                loginButton 
            );


            return panel;
        }

        private VBox createWelcomePanel() {
        	VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            PasswordField tempPasswordField = new PasswordField();
            PasswordField passwordField = new PasswordField();
            PasswordField confirmPasswordField = new PasswordField();
            Button submitButton = new Button("Create Account");
            Button verify = new Button("Verify OTP");

            panel.getChildren().addAll(
            	new Label("Temporary Password:"), tempPasswordField,
            	verify,
                new Label("New Password:"), passwordField,
                new Label("Confirm Password:"), confirmPasswordField,
                submitButton
                
            );
            
            verify.setOnAction(e -> {
            	//&& foundUser.validateExipration() == true
            	if (foundUser.validateLogin(tempPasswordField.getText()) == true && foundUser.validateExipration() == true)
            	{
            		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please enter you new password!");
            		alert.showAndWait();
            	}
            	else
            	{
            		Alert alert = new Alert(Alert.AlertType.ERROR, "Password is expired or does not match our records!");
            		alert.showAndWait();
            		Stage stage = new Stage(); 
                    Driver createAccount = new Driver(); // Create an instance of CreateAccount
                    try {
                        createAccount.start(stage); // Start CreateAccount stage (login page)
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                	
            	}
            	
            });

            submitButton.setOnAction(e -> {
            	
            	if (passwordField.getText().equals(confirmPasswordField.getText())) {
            		
            		admin.resetUserPassword(passwordField.getText(), foundUser.getUsername(), db);
            		Alert alert = new Alert(Alert.AlertType.INFORMATION, "New password is: " + passwordField.getText());
            		alert.showAndWait();
            		System.out.println(foundUser.validateLogin(passwordField.getText()));
            		Stage stage = new Stage(); 
            		Driver createAccount = new Driver(); // Create an instance of CreateAccount
                    try {
                        createAccount.start(stage); // Start CreateAccount stage (login page)
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            		

            	} else {
            		Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match!");
            		alert.showAndWait();
            	}
            });

            return panel;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}