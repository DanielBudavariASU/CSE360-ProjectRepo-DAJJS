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
