package trial;

import java.util.List;
import java.util.ArrayList;

/**
 * The Student class represents a student user who can search for articles,
 * view articles, and send feedback or questions to the system when needed.
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;
    private List<String> searchHistory;
    private List<String> feedbackMessages;

    /**
     * Constructor for the Student class.
     * Initializes a student user with a username and password, assigning the "Student" role.
     *
     * @param username The username of the student.
     * @param password The password of the student.
     */
    public Student(String username, String password) {
        super(username, password, "Student");
        this.searchHistory = new ArrayList<>();
      
        this.feedbackMessages = new ArrayList<>();
        System.out.println("Student account created for username: " + username);
    }
    
    public List<HelpArticle> searchHelpArticles(Database db, String keyword, String groupName, String level) {
    	addSearchQuery("Keyword: " + keyword + "\nGroup Name: " + groupName + "\nLevel: " + level);
        return db.searchArticles(this, keyword, groupName, level);
    }

    /**
     * Adds a search query to the student's search history.
     *
     * @param query The search query to add.
     */
    public void addSearchQuery(String query) {
        searchHistory.add(query);
    }

    /**
     * Gets the search history of the student.
     *
     * @return A list of previous search queries made by the student.
     */
    public List<String> getSearchHistory() {
        return new ArrayList<>(searchHistory);
    }

    /**
     * Sends a generic feedback message to the system operations team.
     *
     * @param message The feedback message to send.
     */
    public void sendGenericFeedback(String message) {
        feedbackMessages.add(message);
        System.out.println("Thank you for your feedback!");
    }

    /**
     * Sends a specific request to the help system.
     * This is used when the student cannot find the information they need.
     *
     * @param specificRequest The specific request message.
     */
    public void sendSpecificRequest(String specificRequest) {
        feedbackMessages.add(specificRequest);
        System.out.println("Your request has been sent to the help team. We will get back to you soon.");
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
