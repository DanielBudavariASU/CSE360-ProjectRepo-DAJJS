package trial;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminHomeScreen extends Application{
    private Admin admin;
    private Database db = CreateAccount.getDb(); 

    public AdminHomeScreen(Admin admin) {
        this.admin = admin;
    }

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

        // Invite User Button Action
        inviteUserButton.setOnAction(e -> {
            String newRole = inviteCodeInput.getText();
            //Role newUser = new Role();
            Invitation invite = admin.inviteUser( newRole);
            db.addInvitation(invite);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invitation sent to new user with role of " + invite.getRole() + ": " + invite.getCode());
       	 	alert.showAndWait();
       	 	System.out.println(invite.getCode()); //print on console so you can copy it
            // userList.add(newUser); Uncomment if needed
        });

        // Reset Password Button Action
        resetPasswordButton.setOnAction(e -> {
        	System.out.println("Enter name of User who's password needs to be reset");
            String in = resetUserInput.getText();
          //  LocalDateTime now = LocalDateTime.now();
          //  LocalDateTime expirationTime = now.plusMinutes(3);
            Password OTP = new Password();
            String pass = OTP.setOTP();
            // Assuming User class has a way to look up by username
            User temp = db.findUserByUsername(in);
            String username = temp.getUsername();
            admin.resetUserPassword(username, pass, db);
        });

        // Delete User Button Action
        deleteUserButton.setOnAction(e -> {
        	System.out.println("Enter name of User who's needs to be deleted");
            String in = resetUserInput.getText();
            String toDel = db.findUserByUsername(in).getUsername();
            admin.deleteUser(db, toDel);
        });

        listUsersButton.setOnAction(e -> {
        	// Create a new Stage (popup window)
            Stage popupStage = new Stage();
            popupStage.setTitle("List of Users");

            // Set it to be a modal window
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // VBox to hold the list of users
            VBox userListVBox = new VBox(10);
            userListVBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Call admin.listUsers() which internally calls db.listAllUsers()
            String userList = db.listAllUsers();  // Adjust listUsers to return the user list string

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
        
        logoutButton.setOnAction(e -> {
        	Stage stage = (Stage) logoutButton.getScene().getWindow(); // Get current stage
            CreateAccount createAccount = new CreateAccount(); // Create an instance of CreateAccount
            try {
                createAccount.start(stage); // Start CreateAccount stage (login page)
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Layout
        VBox layout = new VBox(10, inviteCodeInput, inviteUserButton, resetUserInput, resetPasswordButton, deleteUserButton, listUsersButton, logoutButton);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
