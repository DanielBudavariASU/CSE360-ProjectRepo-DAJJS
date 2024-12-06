package trial;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


public class HelpGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String groupName;               // Name of the group (e.g., "Eclipse", "H2 Database")
    private List<HelpArticle> articles;     // List of articles belonging to this group

    /**
     * <p> Constructor: Initializes a new HelpGroup with a given name. </p>
     * 
     * @param groupName The name of the group.
     */
    public HelpGroup(String groupName) {
        this.groupName = groupName;
        this.articles = new ArrayList<>();
    }

    /**
     * <p> Returns the name of the group. </p>
     * @return The group name.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * <p> Sets the name of the group. </p>
     * @param groupName The name of the group.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    /**
     * <p> Returns the list of articles in this group. </p>
     * @return A list of articles in this group.
     */
    public List<HelpArticle> getArticles() {
        return new ArrayList<>(articles);
    }

    /**
     * <p> Adds an article to this group. </p>
     * @param article The help article to add.
     */
    public void addArticle(HelpArticle article) {
        if (!articles.contains(article)) {
            articles.add(article);
            article.addGroup(this.groupName);  // Ensure the article's group list is updated
        }
    }

    /**
     * <p> Removes an article from this group. </p>
     * @param article The help article to remove.
     */
    public void removeArticle(HelpArticle article) {
        if (articles.contains(article)) {
            articles.remove(article);
            article.removeGroup(this.groupName);  // Ensure the article's group list is updated
        }
    }

    /**
     * <p> Checks if the group contains a specific article. </p>
     * @param article The article to check.
     * @return True if the article is in the group, false otherwise.
     */
    public boolean containsArticle(HelpArticle article) {
        return articles.contains(article);
    }
}