package trial;

public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String username, String password) {
        super(username, password, "Admin");  // Admin role
    }

    // Method to invite a new user by generating an invitation code
    public Invitation inviteUser(String role) {
        return new Invitation(role);
    }

    // Method to delete a user
    public void deleteUser(Database db, String username) {
        User userToDelete = db.findUserByUsername(username);
        if (userToDelete != null) {
            db.removeUser(userToDelete);
            System.out.println("User " + username + " deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to list all users
    public void listUsers(Database db) {
        db.listAllUsers();
    }
}
