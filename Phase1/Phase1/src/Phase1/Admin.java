package Phase1;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, new Role("Admin"));
    }

    public void inviteUser(String inviteCode, Role role) {
        // generate and send a one-time invitation code to a new user
    }
 //helpe me
    public void resetUserPassword(User user, String newPassword, LocalDateTime expiration) {
        // give one-time password and validate its expiration
    }

    public void deleteUser(User user) {
        // Show confirmation message "Are you sure?"
        if (confirmDeletion()) {
            // Logic to remove the user from the system
			
			Alert alert = new Alert(Alert.AlertType.showAndWait());
			alert.setTitle("User Removed");
			alert.setHeaderText("The user has been removed");
        }
    }

    public void listAllUsers(List<User> users) {
        for (User user : users) {
            System.out.println("Username: " + user.username + ", Role: " + user.role.getRoleName());
        }
    }

    public void addOrRemoveRole(User user, Role newRole) {
        user.role = newRole;
        // Change user role
    }

    private boolean confirmDeletion() {
        // Display confirmation dialog
        return true; // Placeholder 
    }
}