package trial;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class Driver extends Application {

    private CardPane cardPane;
    private static Database d = new Database();  // Use your existing Database class

    public static Database getDb() 
    {
    	return d;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login System");

        cardPane = new CardPane();
        Scene scene = new Scene(cardPane, 400, 900);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class CardPane extends StackPane {
    	Database db = d; 
        public CardPane() {
            getChildren().add(createWelcomePanel());
        }

        private VBox createWelcomePanel() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Label welcomeLabel = new Label("Welcome to the Login System");
            Button createAccountButton = new Button("Create Account");
            Button loginButton = new Button("Login");
            

            if(!db.isAdminPresent()) 
            {
            	createAccountButton.setOnAction(e -> {
                    getChildren().clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No admin found. Creating first admin account.");
                	alert.showAndWait();
                    getChildren().add(createSignupPanel("admin"));
                });
                
            }
            else {
            	
            	createAccountButton.setOnAction(e -> {
            		getChildren().clear();
            		getChildren().add(createInviteCodePanel());
            	});
            }

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

	            // Find the invitation by the invite code in the database
	            Invitation invitation = db.findInvitationByCode(inviteCode);

	            if (invitation != null) {
	            	// If the invitation exists, move to the signup panel
	            	getChildren().clear();
	            	getChildren().add(createSignupPanel(invitation.getRole()));
	            } 
	            else {
	            	// If no valid invitation found, show an error
	            	Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid or expired invite code. Please try again.");
	            	alert.showAndWait();
	            }
            	
            });
            

            return panel;
        }
        
        private VBox createSignupPanel(String role) {
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
            	
            	String username = usernameField.getText();
                String password = passwordField.getText();
                
            	if (passwordField.getText().equals(confirmPasswordField.getText())) {
                	// Check if admin is present
                    if (!db.isAdminPresent()) {
                        // No admin exists, first user becomes admin
                        Admin admin = new Admin(username, password);
                        db.addUser(admin);

                        // Show success alert for admin creation
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "User is set to Admin.");
                        alert.showAndWait();
                    } else {
                        // Add regular user
                        User user = new User(username, password, role);
                        db.addUser(user);

                        // Show success alert for user creation
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully!");
                        alert.showAndWait();
                    }
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
            Button resetButton = new Button("Reset password");
            Button loginButton = new Button("Login");
            TextField usernameField = new TextField();
            PasswordField passwordField = new PasswordField();
            
            resetButton.setOnAction(e -> {
            	getChildren().add(createResetPasswordPage());
            });

            panel.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginButton,
                resetButton
            );

            loginButton.setOnAction(e -> {
                // potential logic for authentication
            	User foundUser = db.findUserByUsername(usernameField.getText());

                if (foundUser != null && foundUser.validateLogin(passwordField.getText())) {
                	 Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login successful! Welcome, "  + foundUser.getUsername());
                	 alert.showAndWait();
                	 if (foundUser.doesPasswordNeedReset() == true)
                	 {
                		 foundUser.passwordNeedReset(false); //reset flag
                		 alert = new Alert(Alert.AlertType.INFORMATION, "Password meeds to be reset! Redirecting..");
                    	 alert.showAndWait();
                    	 getChildren().add(createResetPasswordPage());
                         
                	 }
                	 else if(foundUser.getRole().getRoles().contains("Instructor, Admin") 
                			 || foundUser.getRole().getRoles().contains("Instructor, Admin, User") 
                			 || foundUser.getRole().getRoles().contains("User, Admin"))
                	 {
                		 getChildren().clear();
                		 getChildren().add(createMultipleRolePage());
                	 }
                	 else if(foundUser.getRole().getRoles().contains("Admin"))
                	 {
                		 getChildren().add(createAdminHomePage());
                	 }	 
                	 else
                	 {
                		 getChildren().add(createUserHomePage());
                	 }
                	 
                }
                else
                {
                	Alert alert = new Alert(Alert.AlertType.ERROR, "A credential error occurred.");
                    alert.showAndWait();
                }
 
            });

            return panel;
        }
        
        private VBox createAdminHomePage() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Stage stage = new Stage();
            AdminHomeScreen adminHomescreen = new AdminHomeScreen(); // Create an instance of AdminHomeScreen
            try {
            	adminHomescreen.start(stage); // Start AdminHomeScreen stage (login page)
            } catch (Exception ex) {
                  ex.printStackTrace();
                }
            
            return panel;
        }
        
        private VBox createUserHomePage() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Stage stage = new Stage();
            UserHomePage userHomePage = new UserHomePage();
            try {
            	userHomePage.start(stage); 
            } catch (Exception ex) {
                  ex.printStackTrace();
                }
            
            return panel;
        }
        
         private VBox createResetPasswordPage()
         {
        	 VBox panel = new VBox(10);
             panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

             Stage stage = new Stage();
             ResetPasswordPage resetScreen = new ResetPasswordPage(); 
             try {
             	resetScreen.start(stage); 
             } catch (Exception ex) {
                   ex.printStackTrace();
                 }
             
             return panel;
         }
         
         private VBox createMultipleRolePage()
         {
        	 VBox panel = new VBox(10);
        	 panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
        	 Label welcomeLabel = new Label("Please choose which role for your session!");
             Button userInstructorButton = new Button("User/Instructor");
             Button adminButton = new Button("Admin");
                  
             userInstructorButton.setOnAction(e -> {
            	 getChildren().clear();
            	 getChildren().add(createUserHomePage());
             });
             
             adminButton.setOnAction(e -> {
            	 getChildren().clear(); 
            	 getChildren().add(createAdminHomePage());
             });

             panel.getChildren().addAll(
                 welcomeLabel, userInstructorButton,
                 adminButton 
             );


             return panel;
         }
    }

    public static void main(String[] args) {
        launch(args);
    }
}