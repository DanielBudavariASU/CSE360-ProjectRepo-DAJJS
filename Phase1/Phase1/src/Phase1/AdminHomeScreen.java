package Phase1;

public class AdminHomeScreen extends Application {
    private Admin admin;

    public AdminHomeScreen(Admin admin) {
        this.admin = admin;
    }

    @Override
    public void start(Stage primaryStage) {
        Button inviteUserButton = new Button("Invite User");
        Button resetPasswordButton = new Button("Reset User Password");
        Button deleteUserButton = new Button("Delete User");
        Button listUsersButton = new Button("List All Users");

        inviteUserButton.setOnAction(e -> {
            // Admin invites a user
        });

        // Other buttons would similarly trigger their respective admin functions

        // Layout
        VBox layout = new VBox(10, inviteUserButton, resetPasswordButton, deleteUserButton, listUsersButton);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
