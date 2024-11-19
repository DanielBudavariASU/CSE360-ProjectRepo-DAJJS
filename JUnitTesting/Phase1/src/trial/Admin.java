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
    public boolean deleteUser(Database db, String username) {
        User userToDelete = db.findUserByUsername(username);  // Find user by username
        if (userToDelete != null) {
            db.removeUser(userToDelete);  // Remove user from database
            System.out.println("User " + username + " deleted successfully.");
            return true;
        } else {
            System.out.println("User not found.");  // User does not exist
            return false;
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
    public boolean resetUserPassword(String newPassword, String username, Database db) {
        User userToReset = db.findUserByUsername(username);  // Find user by username

        if (userToReset == null) {
            System.out.println("User not found.");  // User does not exist
            return false;
        } else {
            userToReset.resetPassword(newPassword);  // Reset user's password
            System.out.println("Password reset successfully for user: " + username);
            return true;
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
        return db.listHelpArticles(groupNames, this);
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
        db.backupHelpArticles(fileName, groupNames, this);  // Call the Database method to back up articles
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
        db.restoreHelpArticles(fileName, merge, this);  // Call the Database method to restore articles
    }
    
    
    // -------------------------------------------PHASE-3--------------------------------------------------
    
    /**
     * Adds a student to a special access group.
     *
     * @param db The Database object to interact with.
     * @param groupName The name of the special access group.
     * @param student The Student object to be added.
     */
    public void addStudentToSpecialAccessGroup(Database db, String groupName, Student student) {
        db.addStudentToSpecialAccessGroup(groupName, student);
    }

    /**
     * Removes a student from a special access group.
     *
     * @param db The Database object to interact with.
     * @param groupName The name of the special access group.
     * @param student The Student object to be removed.
     */
    public void removeStudentFromSpecialAccessGroup(Database db, String groupName, Student student) {
        db.removeStudentFromSpecialAccessGroup(groupName, student);
    }
    
    /**
     * Adds an admin to a special access group.
     * 
     * @param db The Database object to interact with.
     * @param groupName The name of the special access group.
     * @param admin The Admin object to be added.
     */
    public void addAdminToSpecialAccessGroup(Database db, String groupName, Admin admin) {
        db.addAdminToSpecialAccessGroup(groupName, admin);
    }

    /**
     * Removes an admin from a special access group.
     * 
     * @param db The Database object to interact with.
     * @param groupName The name of the special access group.
     * @param admin The Admin object to be removed.
     */
    public void removeAdminFromSpecialAccessGroup(Database db, String groupName, Admin admin) {
        db.removeAdminFromSpecialAccessGroup(groupName, admin);
    }

    /**
     * Adds an instructor as an admin to a special access group.
     * 
     * @param db The Database object to interact with.
     * @param groupName The name of the special access group.
     * @param instructor The Instructor object to be added as admin.
     */
    public void addInstructorAdminToSpecialAccessGroup(Database db, String groupName, Instructor instructor) {
        db.addInstructorAdminToSpecialAccessGroup(groupName, instructor);
    }

    /**
     * Removes an instructor admin from a special access group.
     * 
     * @param db The Database object to interact with.
     * @param groupName The name of the special access group.
     * @param instructor The Instructor object to be removed from admin.
     */
    public void removeInstructorAdminFromSpecialAccessGroup(Database db, String groupName, Instructor instructor) {
        db.removeInstructorAdminFromSpecialAccessGroup(groupName, instructor);
    }


    /**
     * Views all students in a special access group.
     *
     * @param db The Database object to interact with.
     * @param groupName The name of the special access group.
     * @return A list of students in the special access group.
     */
    public List<String> viewStudentsInSpecialAccessGroup(Database db, String groupName) {
        SpecialAccessGroup group = db.findSpecialAccessGroupByName(groupName);
        if (group != null) {
            return group.getStudents();
        }
        return null;
    }


}