package trial;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class HelpArticle implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
    private long articleId;               // Unique identifier for the article
    private String title;                 // The title of the help article
    private String level;                 // The level of the article (beginner, intermediate, advanced, expert)
    private String author;
    private String shortDescription;      // A brief description (like an abstract)
    private List<String> keywords;        // List of keywords for search purposes
    private String body;                  // The body of the help article
    private List<String> references;      // A list of references for further reading
    private List<String> groups;          // List of groups the article belongs to
    private boolean isSpecialAccess; // Indicates whether the article is in a special access group
    private List<String> specialAccessGroups;    // List of special access groups the article belongs to


    /**
     * <p> Constructor: Creates a new HelpArticle with the given parameters. </p>
     * 
     * @param articleId The unique identifier for the article.
     * @param title The title of the help article.
     * @param level The level of the article (beginner, intermediate, advanced, expert).
     * @param shortDescription A brief description or abstract.
     * @param keywords A list of keywords or phrases to help with searching the article.
     * @param body The main content of the help article.
     * @param references A list of references for the article.
     * @param groups A list of groups the article belongs to.
     */
    public HelpArticle(String title, String author, String level, String shortDescription, List<String> keywords, String body, List<String> references, List<String> groups, List <String> specialGroups, boolean isInSpecialAccessGroup) {
        this.title = title;
        this.author = author;
        this.level = level;
        this.shortDescription = shortDescription;
        this.keywords = new ArrayList<>(keywords);
        this.body = body;
        this.references = new ArrayList<>(references);
        this.groups = new ArrayList<>(groups);
        this.specialAccessGroups = new ArrayList<>(specialGroups);
        this.isSpecialAccess = isInSpecialAccessGroup;
        
    }
    
    
    public HelpArticle(HelpArticle other) {
        this.articleId = other.articleId;
        this.title = other.title;
        this.level = other.level;
        this.shortDescription = other.shortDescription;
        this.keywords = new ArrayList<>(other.keywords);
        this.body = other.body;  // Note: body will be decrypted later
        this.references = new ArrayList<>(other.references);
        this.groups = new ArrayList<>(other.groups);
        this.specialAccessGroups = new ArrayList<>(other.specialAccessGroups);
        this.isSpecialAccess = other.isSpecialAccess;
        this.author = other.author; // Assuming 'author' is a field in HelpArticle
    }

    // Getters and Setters

    /**
     * <p> Returns the unique ID of the article. </p>
     * @return The unique article ID.
     */
    public long getArticleId() {
        return articleId;
    }

    /**
     * <p> Sets the unique ID of the article. </p>
     * @param articleId The unique ID to be set.
     */
    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    /**
     * <p> Returns the title of the article. </p>
     * @return The title of the article.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p> Sets the title of the article. </p>
     * @param title The title to be set for the article.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p> Returns the level of the article. </p>
     * @return The level of the article (beginner, intermediate, advanced, expert).
     */
    public String getLevel() {
        return level;
    }

    /**
     * <p> Sets the level of the article. </p>
     * @param level The level to be set (beginner, intermediate, advanced, expert).
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * <p> Returns the short description of the article. </p>
     * @return The short description of the article.
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * <p> Sets the short description of the article. </p>
     * @param shortDescription The short description to be set.
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * <p> Returns the list of keywords for the article. </p>
     * @return A list of keywords.
     */
    public List<String> getKeywords() {
        return new ArrayList<>(keywords);
    }

    /**
     * <p> Sets the keywords for the article. </p>
     * @param keywords A list of keywords to be set.
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = new ArrayList<>(keywords);
    }

    /**
     * <p> Returns the body of the article. </p>
     * @return The body content of the article.
     */
    public String getBody() {
        return body;
    }

    /**
     * <p> Sets the body of the article. </p>
     * @param body The body to be set for the article.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * <p> Returns the list of references for the article. </p>
     * @return A list of references.
     */
    public List<String> getReferences() {
        return new ArrayList<>(references);
    }

    /**
     * <p> Sets the references for the article. </p>
     * @param references A list of references to be set.
     */
    public void setReferences(List<String> references) {
        this.references = new ArrayList<>(references);
    }

    /**
     * <p> Returns the list of groups this article belongs to. </p>
     * @return A list of groups.
     */
    public List<String> getGroups() {
        return new ArrayList<>(groups);
    }

    /**
     * <p> Sets the groups for the article. </p>
     * @param groups A list of groups to be set.
     */
    public void setGroups(List<String> groups) {
        this.groups = new ArrayList<>(groups);
    }

    /**
     * <p> Adds a group to the article. </p>
     * @param groupName The name of the group to add.
     */
    public void addGroup(String groupName) {
        if (!groups.contains(groupName)) {
            groups.add(groupName);
        }
    }

    /**
     * <p> Removes a group from the article. </p>
     * @param groupName The name of the group to remove.
     */
    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }
    
    // --------- NEW METHODS -----------
    
    public String getAuthor() {
    	return author;
    }
    
    public void setAuthor(String author) {
    	this.author = author;
    }
    
    public boolean isSpecialAccess() {
        return isSpecialAccess;
    }

    public void setSpecialAccess(boolean isSpecialAccess) {
        this.isSpecialAccess = isSpecialAccess;
    }

    public List<String> getSpecialAccessGroups() {
        return new ArrayList<>(specialAccessGroups);
    }

    public void setSpecialAccessGroups(List<String> specialAccessGroups) {
        this.specialAccessGroups = new ArrayList<>(specialAccessGroups);
    }

    public void addSpecialAccessGroup(String groupName) {
        if (!specialAccessGroups.contains(groupName)) {
            specialAccessGroups.add(groupName);
        }
    }
    
    
}