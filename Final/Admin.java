package trial;

/**
 * The Admin class represents an administrative user with elevated privileges.
 * It extends the User class and provides additional functionality specific to 
 * administrative tasks such as inviting users, deleting users, and resetting passwords.
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for the Admin class.
     * Initializes an admin user with a username and password, assigning the "Admin" role.
     *
     * @param username The username of the admin.
     * @param password The password of the admin.
     */
    public Admin(String username, String password) {
        super(username, password, "Admin");  // Call the User constructor with "Admin" role
    }

    /**
     * Invites a new user by generating an invitation code associated with the specified role.
     *
     * @param role The role to be assigned to the invited user.
     * @return An Invitation object containing the invitation details.
     */
    public Invitation inviteUser(String role) {
        return new Invitation(role);
    }

    /**
     * Deletes a user from the database based on the provided username.
     * If the user exists, they are removed; otherwise, an error message is displayed.
     *
     * @param db The database from which the user should be deleted.
     * @param username The username of the user to delete.
     */
    public void deleteUser(Database db, String username) {
        User userToDelete = db.findUserByUsername(username);  // Find user by username
        if (userToDelete != null) {
            db.removeUser(userToDelete);  // Remove user from database
            System.out.println("User " + username + " deleted successfully.");
        } else {
            System.out.println("User not found.");  // User does not exist
        }
    }

    /**
     * Resets the password for a specified user in the database.
     * If the user is found, their password is updated; otherwise, a message is printed.
     *
     * @param newPassword The new password to be set for the user.
     * @param username The username of the user whose password is to be reset.
     * @param db The database that contains the user.
     */
    public void resetUserPassword(String newPassword, String username, Database db) {
        User userToReset = db.findUserByUsername(username);  // Find user by username

        if (userToReset == null) {
            System.out.println("User not found.");  // User does not exist
        } else {
            userToReset.resetPassword(newPassword);  // Reset user's password
            System.out.println("Password reset successfully for user: " + username);
        }
    }

    /**
     * Lists all users present in the database.
     *
     * @param db The database from which to list all users.
     */
    public void listUsers(Database db) {
        db.listAllUsers();  // List all users in the database
    }
}
