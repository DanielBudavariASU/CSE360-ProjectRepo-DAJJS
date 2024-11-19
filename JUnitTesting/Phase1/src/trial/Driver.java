package trial;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

/**
 * The Driver class that extends the JavaFX Application class and serves as the entry point
 * for the Login System. This class initializes the primary stage, sets up the initial scene,
 * and provides navigation between different panels like login, account creation, etc.
 */
public class Driver extends Application {

    private CardPane cardPane; // Custom pane to hold different panels
    private static Database d = new Database();  // Reference to the existing Database class

    /**
     * Returns the reference to the static Database object.
     * @return Database object
     */
    public static Database getDb() 
    {
    	return d;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login System");

        cardPane = new CardPane(); // Initialize the custom card pane
        Scene scene = new Scene(cardPane, 400, 900); // Set scene size
        
        primaryStage.setScene(scene);
        primaryStage.show(); // Display the stage
    }

    /**
     * Inner class representing the main pane that holds different panels and allows navigation between them.
     */
    class CardPane extends StackPane {
    	
    	Database db = d;  // Reference to the database

        /**
         * Constructor for CardPane. Sets the initial welcome panel.
         */
        public CardPane() {
            getChildren().add(createWelcomePanel()); // Initialize with the welcome panel
        }

        /**
         * Creates the welcome panel with options to create an account or login.
         * If no admin is present in the system, forces the creation of an admin account.
         * @return VBox containing the welcome panel UI
         */
        private VBox createWelcomePanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Label welcomeLabel = new Label("Welcome to the Login System");
            Button createAccountButton = new Button("Create Account");
            Button loginButton = new Button("Login");
            
            // Check if there is no admin present in the database
            if(!db.isAdminPresent()) 
            {
            	createAccountButton.setOnAction(e -> {
                    getChildren().clear();
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No admin found. Creating first admin account.");
                	alert.showAndWait();
                	
                    getChildren().add(createSignupPanel("admin")); // Redirect to create admin account
                });
                
            }
            else {
            	// If admin exists, allow user account creation via invite code
            	createAccountButton.setOnAction(e -> {
            		getChildren().clear();
            		getChildren().add(createInviteCodePanel());
            	});
            }

            loginButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(createLoginPanel()); // Navigate to login panel
            });

            panel.getChildren().addAll(welcomeLabel, createAccountButton, loginButton);
            
            return panel;
        }

        /**
         * Creates the panel for entering an invite code when creating a user account.
         * @return VBox containing the invite code panel UI
         */
        private VBox createInviteCodePanel() {
        	VBox panel = new VBox(10); // Vertical box layout
        	panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField inviteCodeField = new TextField(); // TextField for invite code input
            Button nextButton = new Button("Next"); // Button to proceed
            Button returnButton = new Button("Exit");
            
            panel.getChildren().addAll(
            		new Label("Enter Invite Code:"), inviteCodeField,
            		nextButton, returnButton
            );

            nextButton.setOnAction(e -> {
            	String inviteCode = inviteCodeField.getText();

	            // Find the invitation by the invite code in the database
	            Invitation invitation = db.findInvitationByCode(inviteCode);

	            if (invitation != null) {
	            	// If the invitation exists, move to the signup panel
	            	getChildren().clear();
	            	getChildren().add(createSignupPanel(invitation.getRole())); // Proceed to signup panel
	            } 
	            else {
	            	// If no valid invitation found, show an error
	            	Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid or expired invite code. Please try again.");
	            	alert.showAndWait();
	            }
            	
            });
            
            returnButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(createWelcomePanel());
            	
            });
            
            return panel;
        }
        
        /**
         * Creates the signup panel for creating a user or admin account based on the role provided.
         * @param role The role to be assigned to the new account (admin or user)
         * @return VBox containing the signup panel UI
         */
        private VBox createSignupPanel(String role) {
        	VBox panel = new VBox(10); // Vertical box layout
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

            // Add input fields for account creation
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
                    } else if(role.equals("Student")){
                        // Add regular user
                        Student student = new Student(username, password);
                        db.addStudent(student);

                        // Show success alert for user creation
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student Account created successfully!");
                        alert.showAndWait();
                    } else if(role.equals("Instructor")){
                        // Add regular user
                        Instructor instructor = new Instructor(username, password);
                        db.addUser(instructor);

                        // Show success alert for user creation
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Instructor Account created successfully!");
                        alert.showAndWait();
                    } else {
                    	Admin admin = new Admin(username, password);
                        db.addUser(admin);
                        System.out.println(role);

                        // Show success alert for user creation
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Admin Account created successfully!");
                        alert.showAndWait();
                    }
                    // Redirect to the login page
                    getChildren().clear();
                    getChildren().add(createLoginPanel());
                } else {
                    // Password mismatch error
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match!");
                    alert.showAndWait();
                }
            });

            return panel;
        }

        /**
         * Creates the login panel where users can enter their username and password.
         * @return VBox containing the login panel UI
         */
        private VBox createLoginPanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            Button resetButton = new Button("Reset password"); // Button to reset password
            Button loginButton = new Button("Login"); // Button to login
            TextField usernameField = new TextField(); // Username input
            PasswordField passwordField = new PasswordField(); // Password input
            
            resetButton.setOnAction(e -> {
            	getChildren().add(createResetPasswordPage()); // Navigate to reset password panel
            });

            panel.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginButton,
                resetButton
            );

            loginButton.setOnAction(e -> {
                // Login logic
            	User foundUser = db.findUserByUsername(usernameField.getText());

                if (foundUser != null && foundUser.validateLogin(passwordField.getText())) {
                	
                	 Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login successful! Welcome, "  + foundUser.getUsername());
                	 alert.showAndWait();
                	 
                	 if (foundUser.doesPasswordNeedReset() == true)
                	 {
                		 foundUser.passwordNeedReset(false); // Reset flag
                		 alert = new Alert(Alert.AlertType.INFORMATION, "Password needs to be reset! Redirecting..");
                    	 alert.showAndWait();
                    	 getChildren().add(createResetPasswordPage()); // Redirect to reset password page
                         	 
                	 }
                	 // If the user has multiple roles (Admin, Instructor, etc.)
                	 else if(foundUser.getRole().getRoles().contains("Instructor, Admin") 
                			 || foundUser.getRole().getRoles().contains("Instructor, Admin, Student") 
                			 || foundUser.getRole().getRoles().contains("Instructor, User") 
                			 || foundUser.getRole().getRoles().contains("Student, Admin"))
         
                	 {
                		 getChildren().clear();
                		 getChildren().add(createMultipleRolePage(foundUser)); // Navigate to the role selection page
                	 }
                	 else if(foundUser.getRole().getRoles().contains("Admin"))
                	 {
                		 getChildren().add(createAdminHomePage((Admin) foundUser)); // Navigate to Admin home page
                	 }	 
                	 else if(foundUser.getRole().getRoles().contains("Student")) 
                	 {
                		 getChildren().add(createUserHomePage((Student) foundUser)); // Navigate to User home page
                	 }
                	 else
                	 {
                		 getChildren().add(createInstructorHomePage((Instructor) foundUser));
                	 }

                }
                else
                {
                	Alert alert = new Alert(Alert.AlertType.ERROR, "A credential error occurred.");
                    alert.showAndWait(); // Login failed alert
                }
 
            });

            return panel;
        }
        
        /**
         * Creates the admin home page UI.
         * @return VBox containing the admin home page UI
         */
        private VBox createAdminHomePage(Admin admin) {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Stage stage = new Stage();
            AdminHomeScreen adminHomescreen = new AdminHomeScreen(admin); // Create an instance of AdminHomeScreen
            
            try {
            	adminHomescreen.start(stage); // Start AdminHomeScreen stage (login page)
            } catch (Exception ex) {
                  ex.printStackTrace();
                }
            
            return panel;
        }
        
        /**
         * Creates the user home page UI.
         * @return VBox containing the user home page UI
         */
        private VBox createUserHomePage(Student student) {
            VBox panel = new VBox(10); // Vertical box layout
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
        
        /**
         * Creates the admin home page UI.
         * @return VBox containing the admin home page UI
         */
        private VBox createInstructorHomePage(Instructor instructor) {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Stage stage = new Stage();
            InstructorHomePage instructorHomePage = new InstructorHomePage(instructor); // Create an instance of AdminHomeScreen
            
            try {
            	instructorHomePage.start(stage); // Start AdminHomeScreen stage (login page)
            } catch (Exception ex) {
                  ex.printStackTrace();
                }
            
            return panel;
        }
        
        /**
         * Creates the reset password page.
         * @return VBox containing the reset password page UI
         */
         private VBox createResetPasswordPage()
         {
        	 VBox panel = new VBox(10); // Vertical box layout
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
         
         /**
          * Creates the role selection page for users with multiple roles.
          * @return VBox containing the multiple role selection page UI
          */
         private VBox createMultipleRolePage(User user)
         {
        	 VBox panel = new VBox(10); // Vertical box layout
        	 panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
        	 
        	 Label welcomeLabel = new Label("Please choose which role for your session!");
             Button userButton = new Button("Student"); // Option for User role
             Button adminButton = new Button("Admin"); // Option for Admin role
             Button instructorButton = new Button("Instructor"); 
                  
             userButton.setOnAction(e -> {
            	 getChildren().clear();
            	 getChildren().add(createUserHomePage((Student) user)); // Navigate to user home page
             });
             
             adminButton.setOnAction(e -> {
            	 getChildren().clear(); 
            	 getChildren().add(createAdminHomePage((Admin) user)); // Navigate to admin home page
             });
             
             instructorButton.setOnAction(e -> {
            	 getChildren().clear(); 
            	 getChildren().add(createInstructorHomePage((Instructor) user)); // Navigate to admin home page
             });

             panel.getChildren().addAll(
                 welcomeLabel, userButton,
                 adminButton, instructorButton 
             );

             return panel;
         }
    }

    /**
     * Main method that launches the JavaFX application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
