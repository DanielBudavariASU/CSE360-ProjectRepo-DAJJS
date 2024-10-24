package trial;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Database class serves as a storage mechanism for users and invitations.
 * It provides methods to add, find, remove, and list users, as well as manage invitations.
 * The user and invitation data are serialized to and from files for persistence.
 */
public class Database {
    private List<User> users;
    private List<Invitation> invitations;  // Store invitations
    private List<HelpArticle> helpArticles;
    private List<HelpGroup> helpGroups;

    private final String filename = "users.ser";  // Filename for user data
    private final String invitationFilename = "invitations.ser";  // Filename for invitation data
    private final String helpArticleFilename = "helpArticles.ser";
    private final String helpGroupFilename = "helpGroups.ser";

    private long articleIdCounter = 1;  // Initialize article ID counter


    /**
     * Constructor for the Database class.
     * Initializes the user and invitation lists and loads existing data from files.
     */
    public Database() {
        this.users = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.helpArticles = new ArrayList<>();    // New arraylist for help articles
        this.helpGroups = new ArrayList<>();      // New arraylist for groups

        loadUsers();        // Load existing users if they exist
        loadInvitations();  // Load existing invitations if they exist
        loadHelpArticles(); // Load existing help articles
        loadHelpGroups();   // Load existing help groups
        
        if (!helpArticles.isEmpty()) {
            // Set the articleIdCounter to be one greater than the highest existing articleId
            articleIdCounter = helpArticles.stream().mapToLong(HelpArticle::getArticleId).max().orElse(0) + 1;
        }
    }

    
    /**
     * Checks if an admin user exists in the database.
     * 
     * @return true if an admin exists, false otherwise.
     */
    public boolean isAdminPresent() {
        for (User user : users) {
            if (user.getRole().hasRole("Admin")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a user to the database and saves the updated user list to a file.
     * If the username already exists, an error message is displayed.
     * 
     * @param user The user to be added.
     */
    public void addUser(User user) {
        if (findUserByUsername(user.getUsername()) == null) {
            users.add(user);
            saveUsers();  // Save users to file
            System.out.println("User added successfully!");
        } else {
            System.out.println("Error: A user with this username already exists.");
        }
    }

    /**
     * Finds a user by their username.
     * 
     * @param username The username of the user to find.
     * @return The User object if found, null otherwise.
     */
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;  // Return found user
            }
        }
        return null;  // User not found
    }

    /**
     * Removes a user from the database and saves the updated user list to a file.
     * 
     * @param user The user to be removed.
     */
    public void removeUser(User user) {
        users.remove(user);  // Remove user from the list
        saveUsers();  // Save updated user list to file
    }

    /**
     * Lists all users in the database.
     * 
     * @return A string representation of all users and their roles.
     */
    public String listAllUsers() {
        StringBuilder userList = new StringBuilder();

        if (users.isEmpty()) {
            userList.append("No users in the system.\n");
        } else {
            for (User user : users) {
                userList.append(user.getUsername())
                        .append(" - Role: ")
                        .append(user.getRole().getRoles())
                        .append("\n");
            }
        }
        return userList.toString();  // Return the user list as a single string
    }
    
    /**
     * Adds an invitation to the database and saves the updated invitation list to a file.
     * 
     * @param invitation The invitation to be added.
     */
    public void addInvitation(Invitation invitation) {
        invitations.add(invitation);  // Add invitation to the list
        saveInvitations();  // Save invitations to file
    }

    /**
     * Finds an invitation by its code.
     * 
     * @param code The code of the invitation to find.
     * @return The Invitation object if found and not used, null otherwise.
     */
    public Invitation findInvitationByCode(String code) {
        for (Invitation invitation : invitations) {
            if (invitation.getCode().equals(code) && !invitation.isUsed()) {
                return invitation;  // Return found invitation
            }
        }
        return null;  // Invitation not found
    }

    /**
     * Saves the list of users to a file using serialization.
     */
    private void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(users);  // Serialize and save users
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Loads the list of users from a file using deserialization.
     */
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            users = (List<User>) in.readObject();  // Deserialize users
        } catch (FileNotFoundException e) {
            System.out.println("No previous users found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    /**
     * Saves the list of invitations to a file using serialization.
     */
    void saveInvitations() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(invitationFilename))) {
            out.writeObject(invitations);  // Serialize and save invitations
        } catch (IOException e) {
            System.out.println("Error saving invitations: " + e.getMessage());
        }
    }

    /**
     * Loads the list of invitations from a file using deserialization.
     */
    @SuppressWarnings("unchecked")
    private void loadInvitations() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(invitationFilename))) {
            invitations = (List<Invitation>) in.readObject();  // Deserialize invitations
        } catch (FileNotFoundException e) {
            System.out.println("No previous invitations found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading invitations: " + e.getMessage());
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------------------
    
    public void addHelpArticle(HelpArticle article) {
        // Ensure no duplicates based on articleId
    	article.setArticleId(articleIdCounter++);
    	
        if (helpArticles.stream().noneMatch(a -> a.getArticleId() == article.getArticleId())) {
            helpArticles.add(article);
            saveHelpArticles();  // Persist the updated list
            
            // Update groups with the new article
            for (String groupName : article.getGroups()) {
                HelpGroup group = findOrCreateGroup(groupName);  // Find or create the group
                group.addArticle(article);  // Add the article to the group
            }
            
            saveHelpGroups();  // Persist the updated group list
            System.out.println("Help article added successfully!");
        } else {
            System.out.println("Error: A help article with this ID already exists.");
        }
    }


    public void removeHelpArticle(long articleId) {
        HelpArticle articleToRemove = helpArticles.stream()
                .filter(article -> article.getArticleId() == articleId)
                .findFirst()
                .orElse(null);

        if (articleToRemove != null) {
            // Remove the article from the helpArticles list
            helpArticles.remove(articleToRemove);
            saveHelpArticles();  // Persist the updated list

            // Remove the article from all its associated groups
            for (String groupName : articleToRemove.getGroups()) {
                HelpGroup group = findGroupByName(groupName);
                if (group != null) {
                    group.removeArticle(articleToRemove);  // Remove article from group
                    if (group.getArticles().isEmpty()) {
                        helpGroups.remove(group);  // Remove group if it has no articles left
                    }
                }
            }

            saveHelpGroups();  // Persist the updated group list
            System.out.println("Help article removed successfully!");
        } else {
            System.out.println("Error: Help article not found.");
        }
    }
    
    public void updateHelpArticle(HelpArticle updatedArticle) {
        for (int i = 0; i < helpArticles.size(); i++) {
            if (helpArticles.get(i).getArticleId() == updatedArticle.getArticleId()) {
                HelpArticle oldArticle = helpArticles.get(i);

                // Update the helpArticles list with the new article
                helpArticles.set(i, updatedArticle);
                saveHelpArticles();  // Persist the updated list

                // Remove old article from its previous groups
                for (String oldGroupName : oldArticle.getGroups()) {
                    HelpGroup oldGroup = findGroupByName(oldGroupName);
                    if (oldGroup != null) {
                        oldGroup.removeArticle(oldArticle);
                        if (oldGroup.getArticles().isEmpty()) {
                            helpGroups.remove(oldGroup);  // Remove group if empty
                        }
                    }
                }

                // Add updated article to the new/updated groups
                for (String newGroupName : updatedArticle.getGroups()) {
                    HelpGroup newGroup = findOrCreateGroup(newGroupName);  // Find or create the group
                    newGroup.addArticle(updatedArticle);  // Add article to the new group
                }

                saveHelpGroups();  // Persist the updated group list
                System.out.println("Help article updated successfully!");
                return;
            }
        }
        System.out.println("Error: Help article not found.");
    }
    
    private HelpGroup findOrCreateGroup(String groupName) {
        for (HelpGroup group : helpGroups) {
            if (group.getGroupName().equals(groupName)) {
                return group;  // Group found, return it
            }
        }
        
        // Group doesn't exist, create a new one
        HelpGroup newGroup = new HelpGroup(groupName);
        helpGroups.add(newGroup);
        return newGroup;
    }

    private HelpGroup findGroupByName(String groupName) {
        for (HelpGroup group : helpGroups) {
            if (group.getGroupName().equals(groupName)) {
                return group;
            }
        }
        return null;  // Group not found
    }

    public HelpArticle findHelpArticleById(long articleId) {
        return helpArticles.stream()
            .filter(article -> article.getArticleId() == articleId)
            .findFirst()
            .orElse(null);
    }

    public List<HelpArticle> listHelpArticles(List<String> groupNames) {
        // If the argument list is empty or null, return all articles
        if (groupNames == null || groupNames.isEmpty()) {
            return helpArticles;  // Return all articles if no group is specified
        }

        List<HelpArticle> filteredArticles = new ArrayList<>();

        // Iterate over all the help groups
        for (HelpGroup group : helpGroups) {
            if (groupNames.contains(group.getGroupName())) {
                // Add all articles in this group to the filteredArticles list
                for (HelpArticle article : group.getArticles()) {
                    if (!filteredArticles.contains(article)) {
                        filteredArticles.add(article);  // Avoid duplicates
                    }
                }
            }
        }
        
        return filteredArticles;
    }
    
    public void addHelpGroup(HelpGroup group) {
        helpGroups.add(group);
        saveHelpGroups();  // Persist the updated group list
        System.out.println("Help group added successfully!");
    }

    public void removeHelpGroup(String groupName) {
        helpGroups.removeIf(group -> group.getGroupName().equals(groupName));
        saveHelpGroups();  // Persist the updated group list
        System.out.println("Help group removed successfully!");
    }

    public void backupHelpArticles(String fileName, List<String> groupNames) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            List<HelpArticle> articlesToBackup;

            if (groupNames == null || groupNames.isEmpty()) {
                // Backup all articles if no group is specified
                articlesToBackup = helpArticles;
            } else {
                // Filter articles by the specified groups
                articlesToBackup = new ArrayList<>();
                for (String groupName : groupNames) {
                    HelpGroup group = findGroupByName(groupName);
                    if (group != null) {
                        for (HelpArticle article : group.getArticles()) {
                            if (!articlesToBackup.contains(article)) {
                                articlesToBackup.add(article);  // Avoid duplicates
                            }
                        }
                    }
                }
            }

            out.writeObject(articlesToBackup);
            System.out.println("Help articles backed up successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error backing up help articles: " + e.getMessage());
        }
    }


    public void restoreHelpArticles(String fileName, boolean merge) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            List<HelpArticle> restoredArticles = (List<HelpArticle>) in.readObject();
            
            if (merge) {
                // Merge restored articles with current list (without duplicates)
                for (HelpArticle article : restoredArticles) {
                    if (findHelpArticleById(article.getArticleId()) == null) {
                        helpArticles.add(article);

                        // Update helpGroups to reflect the restored article's group memberships
                        for (String groupName : article.getGroups()) {
                            HelpGroup group = findOrCreateGroup(groupName);
                            group.addArticle(article);
                        }
                    }
                }
            } else {
                // Replace the current list
                helpArticles = restoredArticles;

                // Clear current groups and rebuild them based on restored articles
                helpGroups.clear();
                for (HelpArticle article : restoredArticles) {
                    for (String groupName : article.getGroups()) {
                        HelpGroup group = findOrCreateGroup(groupName);
                        group.addArticle(article);
                    }
                }
            }

            saveHelpArticles();  // Persist the updated articles list
            saveHelpGroups();    // Persist the updated groups list
            System.out.println("Help articles restored successfully from " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error restoring help articles: " + e.getMessage());
        }
    }


    private void saveHelpArticles() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(helpArticleFilename))) {
            out.writeObject(helpArticles);
            out.writeLong(articleIdCounter);
        } catch (IOException e) {
            System.out.println("Error saving help articles: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadHelpArticles() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(helpArticleFilename))) {
            helpArticles = (List<HelpArticle>) in.readObject();
            articleIdCounter = in.readLong();
        } catch (FileNotFoundException e) {
            System.out.println("No previous help articles found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading help articles: " + e.getMessage());
        }
    }

    private void saveHelpGroups() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(helpGroupFilename))) {
            out.writeObject(helpGroups);
        } catch (IOException e) {
            System.out.println("Error saving help groups: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadHelpGroups() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(helpGroupFilename))) {
            helpGroups = (List<HelpGroup>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous help groups found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading help groups: " + e.getMessage());
        }
    }
    
}
