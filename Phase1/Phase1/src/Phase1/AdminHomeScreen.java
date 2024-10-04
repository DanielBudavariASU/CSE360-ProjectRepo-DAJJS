package Phase1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AdminHomeScreen extends Application {
    private Admin admin;
    private  ArrayList<User> userList = new ArrayList<>();
    public AdminHomeScreen(Admin admin) {
        this.admin = admin;
    }
    public AdminHomeScreen() {
    	String tusername = "Admin";
    	Password pass =new Password();
    	String temppass = pass.setOTP();
        this.admin = new Admin(tusername, temppass);
    }

    @Override
    public void start(Stage primaryStage) {
        Button inviteUserButton = new Button("Invite User");
        Button resetPasswordButton = new Button("Reset User Password");
        Button deleteUserButton = new Button("Delete User");
        Button listUsersButton = new Button("List All Users");
        //if invite User is pressed send invite code to newUser
        inviteUserButton.setOnAction(e -> {
        	String code = "";
        	Role newUser = new Role("User");
        	//User temp = new User();
        	 Scanner input = new Scanner(System.in);
        	System.out.println("enter invite code ");
        	code = input.nextLine();
            admin.inviteUser(code, newUser);
            //userList.add(newUser);
        });
        resetPasswordButton.setOnAction(e -> {
        	String in  = ""; // take input to find correct user 
        	// set exparation time
        	LocalDateTime now = LocalDateTime.now(); 
        	LocalDateTime expirationTime = now.plusMinutes(3);
        	Scanner input = new Scanner(System.in);
        	System.out.println("enter user who's password needs to be reset ");
        	in = input.nextLine();
        	// gereante the OTP 
        	Password OTP = new Password();
        	String pass = OTP.setOTP();
        	//temp user for the sake of gettign the code to run
        	User temp = new User(null,null,null);
        	admin.resetUserPassword(temp, pass, expirationTime);
        });
        deleteUserButton.setOnAction(e -> {
        	//temp user for the sake of gettign the code to run
        	User temp = new User(null,null,null);
        	admin.deleteUser(temp);
        });
        
        listUsersButton.setOnAction(e -> {
        	admin.listAllUsers(userList);
        });

        // Other buttons would similarly trigger their respective admin functions

        // Layout
        VBox layout = new VBox(10, inviteUserButton, resetPasswordButton, deleteUserButton, listUsersButton);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //main for testing if javaFX code runs 
    public static void main(String[] args) {
        launch(args);
     	//start();
     }
}
