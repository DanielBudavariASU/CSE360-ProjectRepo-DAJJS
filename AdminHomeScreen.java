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
            System.out.println("Enter the name of the user that needs to be deleted");
            String in = deleteUserInput.getText(); 
            String toDelete = db.findUserByUsername(in).getUsername();
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
            Scene popupScene = new Scene(userListVBox, 300, 400);
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

        // Layout
        VBox layout = new VBox(10, inviteCodeInput, inviteUserButton, resetUserInput, resetPasswordButton, deleteUserInput, deleteUserButton, listUsersButton, logoutButton);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    ///// newly added code ***** phase 2
    /**
     * Launches the backup options page where the admin can specify group names.
     * @param primaryStage The primary stage for the backup scene.
     */
    private void launchBackupPage(Stage primaryStage) {
        VBox backupPage = new Driver().new CardPane().createBackupPage();
        Scene backupScene = new Scene(backupPage, 400, 300);
        primaryStage.setScene(backupScene);
        primaryStage.show();
    }

    /**
     * Launches the restore options page where the admin can choose merge or overwrite.
     * @param primaryStage The primary stage for the restore scene.
     */
    private void launchRestorePage(Stage primaryStage) {
        VBox restorePage = new Driver().new CardPane().createRestorePage();
        Scene restoreScene = new Scene(restorePage, 400, 300);
        primaryStage.setScene(restoreScene);
        primaryStage.show();
    }

    /////newly added code ***** phase 2

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}