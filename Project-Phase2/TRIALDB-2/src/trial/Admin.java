package trial;

import java.util.ArrayList;
import java.util.List;

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
    
    
 // New Help Article Management Methods (similar to Instructor) ----------

    /**
     * Creates a new help article and adds it to the database.
     * 
     * @param db The Database object to interact with.
     * @param article The HelpArticle object to be added.
     */
    public void createHelpArticle(Database db, HelpArticle article) {
        db.addHelpArticle(article);
    }

    /**
     * Updates an existing help article in the database.
     * 
     * @param db The Database object to interact with.
     * @param article The HelpArticle object with updated information.
     */
    public void updateHelpArticle(Database db, HelpArticle article) {
        db.updateHelpArticle(article);
    }

    /**
     * Deletes a help article from the database based on its article ID.
     * 
     * @param db The Database object to interact with.
     * @param articleId The ID of the help article to be deleted.
     */
    public void deleteHelpArticle(Database db, long articleId) {
        db.removeHelpArticle(articleId);
    }

    /**
     * Lists all help articles in the database or filters them by specified groups.
     * 
     * @param db The Database object to interact with.
     * @param groupNames A list of group names to filter articles by. If null or empty, all articles are listed.
     * @return A list of help articles that match the filter criteria.
     */
    public List<HelpArticle> listHelpArticles(Database db, List<String> groupNames) {
        return db.listHelpArticles(groupNames);
    }

    /**
     * Views a specific help article by its ID.
     * 
     * @param db The Database object to interact with.
     * @param articleId The ID of the help article to be viewed.
     * @return The HelpArticle object if found, or null if not found.
     */
    public HelpArticle viewHelpArticle(Database db, long articleId) {
        return db.findHelpArticleById(articleId);
    }
    
    /**
     * Backs up help articles to a specified file. If groupNames are provided, only those
     * articles from the specified groups are backed up. If groupNames is null, all articles are backed up.
     * 
     * @param db The Database object to interact with.
     * @param fileName The name of the file to back up the articles to.
     * @param groupNames A list of group names to filter articles by. If null, all articles are backed up.
     */
    public void backupHelpArticles(Database db, String fileName, List<String> groupNames) {
        db.backupHelpArticles(fileName, groupNames);  // Call the Database method to back up articles
    }

    /**
     * Restores help articles from a specified file. The merge option determines whether to merge the
     * restored articles with the existing ones or replace them entirely.
     * 
     * @param db The Database object to interact with.
     * @param fileName The name of the file to restore articles from.
     * @param merge A boolean indicating whether to merge the restored articles with existing ones.
     */
    public void restoreHelpArticles(Database db, String fileName, boolean merge) {
        db.restoreHelpArticles(fileName, merge);  // Call the Database method to restore articles
    }

}
