package Phase1;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Phase1.Main;
public class AdminHomeScreen extends Application{
    private Admin admin;
    private Database db = Main.getdb();

    public AdminHomeScreen(Admin admin) {
        this.admin = admin;
    }

    public AdminHomeScreen() {
        String tusername = "Admin";
        Password pass = new Password();
        String temppass = pass.setOTP();
        this.admin = new Admin(tusername, temppass);
    }

    @Override
    public void start(Stage primaryStage) {
        // Buttons
        Button inviteUserButton = new Button("Invite User");
        Button resetPasswordButton = new Button("Reset User Password");
        Button deleteUserButton = new Button("Delete User");
        Button listUsersButton = new Button("List All Users");

        // Input fields for user interaction
        TextField inviteCodeInput = new TextField();
        inviteCodeInput.setPromptText("Enter invite code");

        TextField resetUserInput = new TextField();
        resetUserInput.setPromptText("Enter user for password reset");

        // Invite User Button Action
        inviteUserButton.setOnAction(e -> {
            String newUser = inviteCodeInput.getText();
            //Role newUser = new Role();
            if(newUser.equals("User") || newUser.equals("Admin") || newUser.equals("Instuctor"))
            admin.inviteUser( newUser);
            else {
            	System.out.println("Error: invaild role");
            }
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

        // List Users Button Action
        listUsersButton.setOnAction(e -> admin.listUsers(db));

        // Layout
        VBox layout = new VBox(10, inviteCodeInput, inviteUserButton, resetUserInput, resetPasswordButton, deleteUserButton, listUsersButton);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
