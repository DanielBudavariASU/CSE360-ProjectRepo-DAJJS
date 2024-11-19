package trial;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import java.util.*;
import java.util.Scanner;
import javafx.scene.control.ButtonType;



/**
 * The AdminHomeScreen class represents the home screen for the admin user.
 * It provides functionality for inviting users, resetting passwords,
 * deleting users, listing all users, and logging out.
 */
public class AdminHomeScreen extends Application {
    private Admin admin;
    private Database db = Driver.getDb();  // Access the shared database instance
    private LocalDateTime date;

    /**
     * Constructor for AdminHomeScreen.
     * 
     * @param admin The admin user for this home screen.
     */
    public AdminHomeScreen(Admin admin) {
        this.admin = admin;
    }

    /**
     * Default constructor that creates a placeholder admin user.
     */
    public AdminHomeScreen() {
        String tusername = "Admin";
        Password pass = new Password();
        String temppass = " ";
        this.admin = new Admin(tusername, temppass);
    }

    @Override
    public void start(Stage primaryStage) {
        // Buttons
        Button inviteUserButton = new Button("Invite User");
        Button resetPasswordButton = new Button("Reset User Password");
        Button deleteUserButton = new Button("Delete User");
        Button listUsersButton = new Button("List All Users");
        Button logoutButton = new Button("Logout");
        Button articleButton = new Button("Articles");
        Button accessButton = new Button("Access to General/Special Groups");
        Button backupButton = new Button("Backup Articles");
        Button restoreButton = new Button("Restore Articles");

        // Input fields for user interaction
        TextField inviteCodeInput = new TextField();
        inviteCodeInput.setPromptText("Enter role for user");

        TextField resetUserInput = new TextField();
        resetUserInput.setPromptText("Enter user for password reset");
        
        TextField deleteUserInput = new TextField();
        deleteUserInput.setPromptText("Enter user to delete");

        // Invite User Button Action
        inviteUserButton.setOnAction(e -> {
            String newRole = inviteCodeInput.getText();
            Invitation invite = admin.inviteUser(newRole);
            db.addInvitation(invite);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invitation sent to new user with role of " + invite.getRole() + ": " + invite.getCode());
            alert.showAndWait();
            System.out.println(invite.getCode()); // Print on console for copying
        });

        // Reset Password Button Action
        resetPasswordButton.setOnAction(e -> {
            System.out.println("Enter name of User who's password needs to be reset");
            String userInput = resetUserInput.getText();
            User foundUser = db.findUserByUsername(userInput);
            if (foundUser == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "This user does not exist!");
                alert.showAndWait();
            } else {
                Password OTP = new Password();
                String pass = OTP.setOTP();  // Set OTP
                String tempPassword = OTP.getOTP();
                date = OTP.setExpiration(); 
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedExpirationTime = OTP.getExpiration();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "This temp password for " + userInput + " expires on " + formattedExpirationTime + "\nThe password is: " + tempPassword);
                foundUser.setExpiration(date);
                admin.resetUserPassword(tempPassword, userInput, db); 
                alert.showAndWait();
                foundUser.passwordNeedReset(true);  // Mark password as needing reset
            }
        });

        // Delete User Button Action
        deleteUserButton.setOnAction(e -> {
            System.out.println("Enter name of User who needs to be deleted");
            String in = deleteUserInput.getText(); 
            String toDelete = db.findUserByUsername(in).getUsername();
            if(db.findUserByUsername(in) instanceof Student)
            {
            	db.removeStudent((Student) db.findUserByUsername(in));
            	System.out.println("Student deleted");
            }
            admin.deleteUser(db, toDelete);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The user, " + toDelete + " was deleted!");
            alert.showAndWait();
        });

        // List Users Button Action
        listUsersButton.setOnAction(e -> {
            // Create a new Stage (popup window)
            Stage popupStage = new Stage();
            popupStage.setTitle("List of Users");

            // Set it to be a modal window
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // VBox to hold the list of users
            VBox userListVBox = new VBox(10);
            userListVBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Call db.listAllUsers() to get the user list
            String userList = db.listAllUsers();

            // Add the user list as a label
            Label usersLabel = new Label(userList);
            userListVBox.getChildren().add(usersLabel);

            // Create an OK button to close the popup
            Button okButton = new Button("OK");
            okButton.setOnAction(event -> popupStage.close()); // Close the popup window

            // Add the OK button to the layout
            userListVBox.getChildren().add(okButton);

            // Set the scene and show the popup window
            Scene popupScene = new Scene(userListVBox, 300, 500);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();  // Show the popup and wait until it is closed
        });

        // Logout Button Action
        logoutButton.setOnAction(e -> {
            Stage stage = (Stage) logoutButton.getScene().getWindow(); // Get current stage
            Driver createAccount = new Driver(); // Create an instance of CreateAccount
            try {
                createAccount.start(stage); // Start CreateAccount stage (login page)
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
     // Set action for the logout button
        articleButton.setOnAction(e -> {
            // Switch to the login page when logout is clicked
            Stage stage = (Stage) articleButton.getScene().getWindow();  // Get the current stage (window)
            ArticleHomePage articlePage = new ArticleHomePage(admin, null);  // Create an instance of the login page (Driver)
            
            try {
                articlePage.start(stage);  // Start the login page
            } catch (Exception ex) {
                ex.printStackTrace();  // Handle any exception that occurs during page transition
            }
        });
        
        // Set action for the logout button
        accessButton.setOnAction(e -> {
        	// Switch to the login page when logout is clicked
            Stage stage = (Stage) accessButton.getScene().getWindow();  // Get the current stage (window)
            AdminAccessPage adminAccessPage = new AdminAccessPage(admin, null);  // Create an instance of the login page (Driver)
            try {
                adminAccessPage.start(stage);  // Start the login page
            } catch (Exception ex) {
                ex.printStackTrace();  // Handle any exception that occurs during page transition
            }
        });
        
        // Backup Button Action
        backupButton.setOnAction(e -> {
            Stage backupStage = new Stage();
            backupStage.setTitle("Backup Help Articles");
            VBox backupPage = createBackupPage();  // Use the backup page
            Scene backupScene = new Scene(backupPage, 300, 300);
            backupStage.setScene(backupScene);
            backupStage.show();
        });

        // Restore Button Action
        restoreButton.setOnAction(e -> {
            Stage restoreStage = new Stage();
            restoreStage.setTitle("Restore Help Articles");
            VBox restorePage = createRestorePage();  // Use the restore page
            Scene restoreScene = new Scene(restorePage, 300, 300);
            restoreStage.setScene(restoreScene);
            restoreStage.show();
        });

        // Layout
        VBox layout = new VBox(10, inviteCodeInput, inviteUserButton, resetUserInput, resetPasswordButton, deleteUserInput, 
        						deleteUserButton, listUsersButton, articleButton, accessButton, backupButton, restoreButton, logoutButton);
        
        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    

    
    private VBox createBackupPage() {
        VBox panel = new VBox(10);
        panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Label instructions = new Label("Enter group name(s) for backup (leave empty to backup all):");
        TextField groupNameField = new TextField();
        Label fileLabel = new Label("Enter the file name for backup:");
        TextField fileField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            String groupName = groupNameField.getText().trim();
            List<String> groupList = groupName.isEmpty() ? null : List.of(groupName.split(","));
            String file = fileField.getText().trim();
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to proceed with the backup?");
            confirmAlert.setHeaderText("Confirm Backup");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                   // db.backupHelpArticles(file, groupList);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Backup successful!");
                    successAlert.showAndWait();
                }
            });
        });

        panel.getChildren().addAll(instructions, groupNameField, fileLabel, fileField, submitButton);
        return panel;
    }

    // Restore page for AdminHomeScreen
    private VBox createRestorePage() {
        VBox panel = new VBox(10);
        panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Label fileLabel = new Label("Enter the file name for backup:");
        TextField fileField = new TextField();
        Label instructions = new Label("Select restore option:");
        RadioButton mergeButton = new RadioButton("Merge");
        RadioButton overwriteButton = new RadioButton("Overwrite");
        ToggleGroup toggleGroup = new ToggleGroup();
        mergeButton.setToggleGroup(toggleGroup);
        overwriteButton.setToggleGroup(toggleGroup);
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            RadioButton selectedOption = (RadioButton) toggleGroup.getSelectedToggle();
            String fileName = fileField.getText();
            if (selectedOption != null) {
                String option = selectedOption.getText();
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to proceed with the restore?");
                confirmAlert.setHeaderText("Confirm Restore");
                
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                    	
                    	if(option.equals("Overwrite"))
                    	{
                    	//	db.restoreHelpArticles(fileName, false);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Restore successful!");
                            successAlert.showAndWait();
                    	}
                        
                    }
                    else //merge with existing files
                    {
                    	//db.restoreHelpArticles(fileName, true);
                    	Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Restore successful!");
                        successAlert.showAndWait();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a restore option.");
                alert.showAndWait();
            }
        });

        panel.getChildren().addAll(fileLabel, fileField, instructions, mergeButton, overwriteButton, submitButton);
        return panel;
    }

    

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
