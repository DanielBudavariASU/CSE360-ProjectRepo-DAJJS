package trial;

import java.util.List;

/**
 * The Instructor class extends the User class and provides specific functionality
 * for instructors to manage help articles. Instructors can create, update, view,
 * and delete help articles, as well as list all articles or filter by groups.
 */
public class Instructor extends User {
	
	private static final long serialVersionUID = 1L;

    /**
     * Constructor for the Instructor class.
     * Inherits attributes like username and password from the User class.
     * 
     * @param username The username of the instructor.
     * @param password The password of the instructor.
     */
    public Instructor(String username, String password) {
        super(username, password, "Instructor");  // Set the role to Instructor
    }

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
    
    // ----------------------------------------------------PHASE-3----------------------------------------------------
    
    /**
     * Creates a general or special access group.
     *
     * @param db The Database object to interact with.
     * @param group The HelpGroup or SpecialAccessGroup to be added.
     */
    public void createGroup(Database db, HelpGroup group) {
        if (group instanceof SpecialAccessGroup) {
            db.addSpecialAccessGroup((SpecialAccessGroup) group);
        } else {
            db.addHelpGroup(group);
        }
    }

    /**
     * Deletes a general group from the system.
     *
     * @param db The Database object to interact with.
     * @param groupName The name of the group to be deleted.
     */
    public void deleteGroup(Database db, String groupName) {
        db.removeHelpGroup(groupName);
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
     * Searches for help articles in the database based on keyword, group name, and content level.
     *
     * @param db The Database object to interact with.
     * @param keyword The search keyword.
     * @param groupName The group to filter articles by.
     * @param level The content level of the articles.
     * @return A list of help articles that match the search criteria.
     */
    public List<HelpArticle> searchHelpArticles(Database db, String keyword, String groupName, String level) {
        return db.searchArticles(this, keyword, groupName, level);
    }

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

    /**
     * Views all students in the help system.
     *
     * @param db The Database object to interact with.
     * @return A list of all students in the help system.
     */
    public List<Student> viewAllStudents(Database db) {
        return db.getAllStudents();
    }

    /**
     * Removes a student from the help system.
     *
     * @param db The Database object to interact with.
     * @param student The Student object to be removed.
     */
    public void removeStudent(Database db, Student student) {
        db.removeStudent(student);
    }
    
    /**
     * Views an article in detail using the sequence number from search results.
     *
     * @param db The database to fetch the article from.
     * @param articleId The ID of the article to view.
     */
    public void viewArticle(Database db, long articleId) {
        db.viewArticle(articleId);
    }

}