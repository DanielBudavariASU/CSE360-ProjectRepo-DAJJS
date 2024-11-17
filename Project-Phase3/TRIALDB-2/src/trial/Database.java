package trial;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

/**
 * The Database class serves as a storage mechanism for users and invitations.
 * It provides methods to add, find, remove, and list users, as well as manage invitations.
 * The user and invitation data are serialized to and from files for persistence.
 */
public class Database {
	private List<User> users;
    private List<Invitation> invitations;
    private List<HelpArticle> helpArticles;
    private List<HelpGroup> helpGroups;
    private List<Student> students;
    private List<SpecialAccessGroup> specialAccessGroups;  // Store special access groups

    private final String filename = "users.ser";  // Filename for user data
    private final String invitationFilename = "invitations.ser";  // Filename for invitation data
    private final String helpArticleFilename = "helpArticles.ser";
    private final String helpGroupFilename = "helpGroups.ser";
    private final String studentsFilename = "students.ser";  // Filename for student data
    private final String specialAccessGroupsFilename = "specialAccessGroups.ser";  // Filename for special access group data

    private long articleIdCounter = 1;  // Initialize article ID counter
    
    // Encryption helper instance
    private EncryptionHelper encryptionHelper;
    
    /**
     * Constructor for the Database class.
     * Initializes the user and invitation lists and loads existing data from files.
     */
    public Database() {
    	this.users = new ArrayList<>();
        this.students = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.helpArticles = new ArrayList<>();
        this.helpGroups = new ArrayList<>();
        this.specialAccessGroups = new ArrayList<>();
        
        try {
            encryptionHelper = new EncryptionHelper(); // Initialize the encryption helper
        } catch (Exception e) {
            System.out.println("Error initializing encryption helper: " + e.getMessage());
        }

        loadUsers();        // Load existing users if they exist
        loadStudents();     // Load existing students if they exist
        loadInvitations();  // Load existing invitations if they exist
        loadHelpArticles(); // Load existing help articles
        loadHelpGroups();   // Load existing help groups
        loadSpecialAccessGroups(); // Load existing special access groups

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
    
    // -------------------------------------------------PHASE 2---------------------------------------------------------------
    
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

    public HelpGroup findGroupByName(String groupName) {
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
    
    public void addHelpGroup(HelpGroup group) {
        helpGroups.add(group);
        saveHelpGroups();  // Persist the updated group list
        System.out.println("Help group added successfully!");
    }

//    public void backupHelpArticles(String fileName, List<String> groupNames) {
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
//            List<HelpArticle> articlesToBackup;
//
//            if (groupNames == null || groupNames.isEmpty()) {
//                // Backup all articles if no group is specified
//                articlesToBackup = helpArticles;
//            } else {
//                // Filter articles by the specified groups
//                articlesToBackup = new ArrayList<>();
//                for (String groupName : groupNames) {
//                    HelpGroup group = findGroupByName(groupName);
//                    if (group != null) {
//                        for (HelpArticle article : group.getArticles()) {
//                            if (!articlesToBackup.contains(article)) {
//                                articlesToBackup.add(article);  // Avoid duplicates
//                            }
//                        }
//                    }
//                }
//            }
//
//            out.writeObject(articlesToBackup);
//            System.out.println("Help articles backed up successfully to " + fileName);
//        } catch (IOException e) {
//            System.out.println("Error backing up help articles: " + e.getMessage());
//        }
//    }
//
//
//    public void restoreHelpArticles(String fileName, boolean merge) {
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
//            List<HelpArticle> restoredArticles = (List<HelpArticle>) in.readObject();
//            
//            if (merge) {
//                // Merge restored articles with current list (without duplicates)
//                for (HelpArticle article : restoredArticles) {
//                    if (findHelpArticleById(article.getArticleId()) == null) {
//                        helpArticles.add(article);
//
//                        // Update helpGroups to reflect the restored article's group memberships
//                        for (String groupName : article.getGroups()) {
//                            HelpGroup group = findOrCreateGroup(groupName);
//                            group.addArticle(article);
//                        }
//                    }
//                }
//            } else {
//                // Replace the current list
//                helpArticles = restoredArticles;
//
//                // Clear current groups and rebuild them based on restored articles
//                helpGroups.clear();
//                for (HelpArticle article : restoredArticles) {
//                    for (String groupName : article.getGroups()) {
//                        HelpGroup group = findOrCreateGroup(groupName);
//                        group.addArticle(article);
//                    }
//                }
//            }
//
//            saveHelpArticles();  // Persist the updated articles list
//            saveHelpGroups();    // Persist the updated groups list
//            System.out.println("Help articles restored successfully from " + fileName);
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Error restoring help articles: " + e.getMessage());
//        }
//    }


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
    
    
    // -------------------- PHASE 3 -----------------------------------------------------------
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Return a copy of the students list
    }
    
    
    /**
     * Saves the list of students to a file using serialization.
     */
    private void saveStudents() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(studentsFilename))) {
            out.writeObject(students);  // Serialize and save students
            System.out.println("Students saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }
    
    /**
     * Loads the list of students from a file using deserialization.
     */
    @SuppressWarnings("unchecked")
    private void loadStudents() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(studentsFilename))) {
            students = (List<Student>) in.readObject();  // Deserialize students
            System.out.println("Students loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No previous students found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
    
    /**
     * Adds a student to the database and saves the updated student list to a file.
     * If the username already exists, an error message is displayed.
     *
     * @param student The student to be added.
     */
    public void addStudent(Student student) {
        if (findStudentByUsername(student.getUsername()) == null) {
            students.add(student);
            users.add(student);
            saveStudents();  // Save students to file
            saveUsers();
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Error: A student with this username already exists.");
        }
    }
    
    /**
     * Removes a student from the help system.
     * This includes removing the student from both the 'students' list and the 'users' list.
     * 
     * @param student The student to be removed.
     */
    public void removeStudent(Student student) {
        if (students.contains(student) && users.contains(student)) {
            students.remove(student);
            users.remove(student);

            // Update the serialized files
            saveStudents();  // Persist the updated students list to the file
            saveUsers();     // Persist the updated users list to the file

            // Also remove the student from any special access groups they might be part of
            for (SpecialAccessGroup group : specialAccessGroups) {
                if (group.getStudents().contains(student)) {
                    group.removeStudent(student);
                }
            }
            saveSpecialAccessGroups(); // Persist changes to special access groups

            System.out.println("Student removed successfully from the help system.");
        } else {
            System.out.println("Error: The student is not part of the help system.");
        }
    }
    
    /**
     * Finds a student by their username.
     *
     * @param username The username of the student to find.
     * @return The Student object if found, null otherwise.
     */
    public Student findStudentByUsername(String username) {
        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                return student;  // Return found student
            }
        }
        return null;  // Student not found
    }
        
    /**
     * Adds a special access group to the database.
     * If a group with the same name already exists, it won't add a duplicate.
     *
     * @param group The SpecialAccessGroup to be added.
     */
    public void addSpecialAccessGroup(SpecialAccessGroup group) {
        if (findSpecialAccessGroupByName(group.getGroupName()) == null) {
            specialAccessGroups.add(group);
            saveSpecialAccessGroups();  // Save the updated special access groups list
            System.out.println("Special access group added successfully!");
        } else {
            System.out.println("Error: A special access group with this name already exists.");
        }
    }

    /**
     * Finds a special access group by its name.
     *
     * @param groupName The name of the special access group to find.
     * @return The SpecialAccessGroup object if found, null otherwise.
     */
    public SpecialAccessGroup findSpecialAccessGroupByName(String groupName) {
        for (SpecialAccessGroup group : specialAccessGroups) {
            if (group.getGroupName().equals(groupName)) {
                return group;  // Return found group
            }
        }
        return null;  // Group not found
    }

    /**
     * Adds an instructor to a special access group.
     *
     * @param groupName The name of the special access group.
     * @param instructor The instructor to be added.
     */
    public void addInstructorToSpecialAccessGroup(String groupName, Instructor instructor) {
        SpecialAccessGroup group = findSpecialAccessGroupByName(groupName);
        if (group != null) {
            group.addInstructor(instructor);
            saveSpecialAccessGroups();  // Save changes
            System.out.println("Instructor added successfully to the special access group: " + groupName);
        } else {
            System.out.println("Error: Special access group not found.");
        }
    }

    /**
     * Adds an admin to a special access group.
     *
     * @param groupName The name of the special access group.
     * @param admin The admin to be added.
     */
    public void addAdminToSpecialAccessGroup(String groupName, Admin admin) {
        SpecialAccessGroup group = findSpecialAccessGroupByName(groupName);
        if (group != null) {
            group.addAdmin(admin);
            saveSpecialAccessGroups();  // Save changes
            System.out.println("Admin added successfully to the special access group: " + groupName);
        } else {
            System.out.println("Error: Special access group not found.");
        }
    }
    
    /**
     * Removes an admin from a special access group.
     *
     * @param groupName The name of the special access group.
     * @param admin The admin to be removed.
     */
    public void removeAdminFromSpecialAccessGroup(String groupName, Admin admin) {
        SpecialAccessGroup group = findSpecialAccessGroupByName(groupName);
        if (group != null) {
            if (group.isAdmin(admin)) {
                group.removeAdmin(admin);
                saveSpecialAccessGroups();  // Save changes
                System.out.println("Admin removed successfully from the special access group: " + groupName);
            } else {
                System.out.println("Error: Admin is not part of this special access group.");
            }
        } else {
            System.out.println("Error: Special access group not found.");
        }
    }

    /**
     * Adds an instructor as an admin to a special access group.
     *
     * @param groupName The name of the special access group.
     * @param instructor The instructor to be added as admin.
     */
    public void addInstructorAdminToSpecialAccessGroup(String groupName, Instructor instructor) {
        SpecialAccessGroup group = findSpecialAccessGroupByName(groupName);
        if (group != null) {
            if (!group.isInstructorAdmin(instructor)) {
                group.addInstructorAdmin(instructor);
                saveSpecialAccessGroups();  // Save changes
                System.out.println("Instructor added successfully as an admin to the special access group: " + groupName);
            } else {
                System.out.println("Error: Instructor is already an admin in this special access group.");
            }
        } else {
            System.out.println("Error: Special access group not found.");
        }
    }

    /**
     * Removes an instructor admin from a special access group.
     *
     * @param groupName The name of the special access group.
     * @param instructor The instructor to be removed from admin.
     */
    public void removeInstructorAdminFromSpecialAccessGroup(String groupName, Instructor instructor) {
        SpecialAccessGroup group = findSpecialAccessGroupByName(groupName);
        if (group != null) {
            if (group.isAdmin(instructor)) {
                group.removeInstructor(instructor);
                saveSpecialAccessGroups();  // Save changes
                System.out.println("Instructor removed successfully from admin in the special access group: " + groupName);
            } else {
                System.out.println("Error: Instructor is not an admin in this special access group.");
            }
        } else {
            System.out.println("Error: Special access group not found.");
        }
    }
    
    /**
     * Adds a student to a special access group.
     *
     * @param groupName The name of the special access group.
     * @param student The student to be added.
     */
    public void addStudentToSpecialAccessGroup(String groupName, Student student) {
        SpecialAccessGroup group = findSpecialAccessGroupByName(groupName);
        if (group != null) {
            group.addStudent(student);
            saveSpecialAccessGroups();  // Save changes
            System.out.println("Student added successfully to the special access group: " + groupName);
        } else {
            System.out.println("Error: Special access group not found.");
        }
    }
    
    /**
     * Removes a student from the specified special access group.
     * 
     * @param groupName The name of the special access group.
     * @param student The student to be removed from the group.
     */
    public void removeStudentFromSpecialAccessGroup(String groupName, Student student) {
        SpecialAccessGroup group = findSpecialAccessGroupByName(groupName);
        if (group != null) {
            if (group.getStudents().contains(student)) {
                group.removeStudent(student);
                saveSpecialAccessGroups(); // Persist changes to file
                System.out.println("Student removed successfully from the special access group: " + groupName);
            } else {
                System.out.println("Error: The student is not part of the special access group: " + groupName);
            }
        } else {
            System.out.println("Error: Special access group not found.");
        }
    }

    /**
     * Saves the list of special access groups to a file using serialization.
     */
    public void saveSpecialAccessGroups() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(specialAccessGroupsFilename))) {
            out.writeObject(specialAccessGroups);
            System.out.println("Special access groups saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving special access groups: " + e.getMessage());
        }
    }

    /**
     * Loads the list of special access groups from a file using deserialization.
     */
    @SuppressWarnings("unchecked")
    private void loadSpecialAccessGroups() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(specialAccessGroupsFilename))) {
            specialAccessGroups = (List<SpecialAccessGroup>) in.readObject();
            System.out.println("Special access groups loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No previous special access groups found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading special access groups: " + e.getMessage());
        }
    }

    
    public void addHelpArticle(HelpArticle article) {
        article.setArticleId(articleIdCounter++);

        if (helpArticles.stream().noneMatch(a -> a.getArticleId() == article.getArticleId())) {
            // Encrypt if the article belongs to a special access group
            if (article.isSpecialAccess()) {
                try {
                    byte[] iv = EncryptionUtils.getInitializationVector(article.getTitle().toCharArray());

                    // Encrypt the body
                    byte[] encryptedBody = encryptionHelper.encrypt(EncryptionUtils.toByteArray(article.getBody().toCharArray()), iv);
                    article.setBody(Base64.getEncoder().encodeToString(encryptedBody));

                    // Encrypt references
                    List<String> encryptedReferences = new ArrayList<>();
                    for (String reference : article.getReferences()) {
                        byte[] encryptedReference = encryptionHelper.encrypt(EncryptionUtils.toByteArray(reference.toCharArray()), iv);
                        encryptedReferences.add(Base64.getEncoder().encodeToString(encryptedReference));
                    }
                    article.setReferences(encryptedReferences);
                } catch (Exception e) {
                    System.out.println("Error encrypting article: " + e.getMessage());
                    return; // If encryption fails, do not add the article
                }
            }

            helpArticles.add(article);
            saveHelpArticles();  // Persist the updated list

            // Update groups based on whether they are special access or normal groups
            if (article.isSpecialAccess()) {
                for (String groupName : article.getSpecialAccessGroups()) {
                    SpecialAccessGroup specialGroup = findOrCreateSpecialAccessGroup(groupName);
                    specialGroup.addArticle(article);
                }
                saveSpecialAccessGroups();  // Persist the updated special access groups list
            } else {
                for (String groupName : article.getGroups()) {
                    HelpGroup group = findOrCreateGroup(groupName);
                    group.addArticle(article);
                }
                saveHelpGroups();  // Persist the updated help group list
            }

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

            // Remove the article from all its associated general groups
            for (String groupName : articleToRemove.getGroups()) {
                HelpGroup group = findGroupByName(groupName);
                if (group != null) {
                    group.removeArticle(articleToRemove);  // Remove article from group
                    if (group.getArticles().isEmpty()) {
                        helpGroups.remove(group);  // Remove group if it has no articles left
                    }
                }
            }

            // Remove the article from all its associated special access groups
            for (String specialGroupName : articleToRemove.getSpecialAccessGroups()) {
                SpecialAccessGroup specialGroup = findSpecialAccessGroupByName(specialGroupName);
                if (specialGroup != null) {
                    specialGroup.removeArticle(articleToRemove);  // Remove article from the special group
                    if (specialGroup.getArticles().isEmpty()) {
                        specialAccessGroups.remove(specialGroup);  // Remove the special group if it has no articles left
                    }
                }
            }

            // Persist the updated group lists
            saveHelpGroups();  // Persist the updated general group list
            saveSpecialAccessGroups();  // Persist the updated special access group list

            System.out.println("Help article removed successfully!");
        } else {
            System.out.println("Error: Help article not found.");
        }
    }
    
    public void updateHelpArticle(HelpArticle updatedArticle) {
        for (int i = 0; i < helpArticles.size(); i++) {
            if (helpArticles.get(i).getArticleId() == updatedArticle.getArticleId()) {
                HelpArticle oldArticle = helpArticles.get(i);

                // Encrypt the content if it belongs to a special access group
                if (updatedArticle.isSpecialAccess()) {
                    try {
                        // Generate IV using the title
                        byte[] iv = EncryptionUtils.getInitializationVector(updatedArticle.getTitle().toCharArray());

                        // Encrypt the body
                        byte[] encryptedBody = encryptionHelper.encrypt(EncryptionUtils.toByteArray(updatedArticle.getBody().toCharArray()), iv);
                        updatedArticle.setBody(Base64.getEncoder().encodeToString(encryptedBody));

                        // Encrypt references
                        List<String> encryptedReferences = new ArrayList<>();
                        for (String reference : updatedArticle.getReferences()) {
                            byte[] encryptedReference = encryptionHelper.encrypt(EncryptionUtils.toByteArray(reference.toCharArray()), iv);
                            encryptedReferences.add(Base64.getEncoder().encodeToString(encryptedReference));
                        }
                        updatedArticle.setReferences(encryptedReferences);
                    } catch (Exception e) {
                        System.out.println("Error encrypting updated article: " + e.getMessage());
                        return; // Do not proceed if encryption fails
                    }
                }

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

                // Handle special access groups similarly
                for (String oldSpecialGroupName : oldArticle.getSpecialAccessGroups()) {
                    SpecialAccessGroup oldSpecialGroup = findSpecialAccessGroupByName(oldSpecialGroupName);
                    if (oldSpecialGroup != null) {
                        oldSpecialGroup.removeArticle(oldArticle);
                        if (oldSpecialGroup.getArticles().isEmpty()) {
                            specialAccessGroups.remove(oldSpecialGroup);  // Remove group if empty
                        }
                    }
                }

                // Add updated article to the new/updated special access groups
                for (String newSpecialGroupName : updatedArticle.getSpecialAccessGroups()) {
                    SpecialAccessGroup newSpecialGroup = findOrCreateSpecialAccessGroup(newSpecialGroupName);  // Find or create the special access group
                    newSpecialGroup.addArticle(updatedArticle);  // Add article to the new special access group
                }

                saveHelpGroups();  // Persist the updated group list
                saveSpecialAccessGroups();  // Persist the updated special access group list
                System.out.println("Help article updated successfully!");
                return;
            }
        }
        System.out.println("Error: Help article not found.");
    }
    


    private SpecialAccessGroup findOrCreateSpecialAccessGroup(String groupName) {
        for (SpecialAccessGroup group : specialAccessGroups) {
            if (group.getGroupName().equals(groupName)) {
                return group;  // Group found, return it
            }
        }

        // Group doesn't exist, create a new one
        SpecialAccessGroup newGroup = new SpecialAccessGroup(groupName);
        specialAccessGroups.add(newGroup);
        return newGroup;
    }
    
    
    public List<HelpArticle> searchArticles(User user, String keyword, String group, String level) {
        List<HelpArticle> filteredArticles = new ArrayList<>();

        for (HelpArticle article : helpArticles) {
            // Check if the article matches the group, level, and contains the keyword in title, author, or abstract.
            boolean matchesGroup = (group == null) || article.getGroups().contains(group) || article.getSpecialAccessGroups().contains(group);
            boolean matchesLevel = level.equalsIgnoreCase("all") || article.getLevel().equalsIgnoreCase(level);
            boolean matchesKeyword = article.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    article.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                    article.getShortDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                    article.getKeywords().contains(keyword);

            // If article matches the group, level, and keyword
            if (matchesGroup && matchesLevel && matchesKeyword) {
                if (article.isSpecialAccess()) {
                    boolean hasAccess = false;

                    // Verify if the user has access to any of the article's special access groups
                    for (String specialGroupName : article.getSpecialAccessGroups()) {
                        SpecialAccessGroup specialAccessGroup = findSpecialAccessGroupByName(specialGroupName);
                        if (specialAccessGroup != null) {
                            if (specialAccessGroup.hasAccess(user)) {
                                hasAccess = true;
                                break;
                            }
                        }
                    }

                    if (!hasAccess) {
                        // User does not have access to this special access article, skip it
                        continue;
                    }

                    // User has access, create a copy and decrypt the article's contents
                    HelpArticle decryptedArticle = new HelpArticle(article);
                    try {
                        byte[] iv = EncryptionUtils.getInitializationVector(decryptedArticle.getTitle().toCharArray());

                        // Decrypt the body
                        byte[] encryptedBody = Base64.getDecoder().decode(decryptedArticle.getBody());
                        String decryptedBody = new String(encryptionHelper.decrypt(encryptedBody, iv));
                        decryptedArticle.setBody(decryptedBody);

                        // Decrypt references
                        List<String> decryptedReferences = new ArrayList<>();
                        for (String encryptedReference : decryptedArticle.getReferences()) {
                            byte[] encryptedRefBytes = Base64.getDecoder().decode(encryptedReference);
                            String decryptedReference = new String(encryptionHelper.decrypt(encryptedRefBytes, iv));
                            decryptedReferences.add(decryptedReference);
                        }
                        decryptedArticle.setReferences(decryptedReferences);

                    } catch (Exception e) {
                        System.out.println("Error decrypting article: " + e.getMessage());
                        continue; // Skip this article if decryption fails
                    }
                    filteredArticles.add(decryptedArticle);
                } else {
                    // If the article is not in a special access group, add it directly
                    filteredArticles.add(new HelpArticle(article));
                }
            }
        }
        return filteredArticles;
    }

   
    
    /**
     * Backs up articles and groups to a file.
     * Admins and Instructors can only back up articles from groups they have access to.
     *
     * @param fileName   The filename where the backup will be saved.
     * @param groupNames List of group names to filter articles for backup.
     * @param user       The user (Admin or Instructor) requesting the backup.
     */
    public void backupHelpArticles(String fileName, List<String> groupNames, User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            List<HelpArticle> articlesToBackup = new ArrayList<>();

            if (groupNames == null || groupNames.isEmpty()) {
                // If group names are not specified, backup all articles, filtering based on user's permissions.
                for (HelpArticle article : helpArticles) {
                    if (hasAccessToArticle(user, article)) {
                        articlesToBackup.add(article);
                    }
                }
            } else {
                // If specific groups are provided, only backup articles from those groups.
                for (String groupName : groupNames) {
                    // First, find the group in the general groups.
                    HelpGroup group = findGroupByName(groupName);
                    if (group != null) {
                        // Add articles from general group if user has access.
                        for (HelpArticle article : group.getArticles()) {
                            if (!articlesToBackup.contains(article) && hasAccessToArticle(user, article)) {
                                articlesToBackup.add(article);
                            }
                        }
                    }

                    // Then, look in special access groups.
                    SpecialAccessGroup specialGroup = findSpecialAccessGroupByName(groupName);
                    if (specialGroup != null) {
                        if (specialGroup.hasAccess(user)) {
                            // Add articles from special access group if user has access.
                            for (HelpArticle article : specialGroup.getArticles()) {
                                if (!articlesToBackup.contains(article)) {
                                    articlesToBackup.add(article);
                                }
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

    /**
     * Restores help articles from a backup file.
     * Admins and Instructors can only restore articles they have rights to.
     *
     * @param fileName The filename where the backup is saved.
     * @param merge    Indicates if the restored articles should merge with or replace the current list.
     * @param user     The user (Admin or Instructor) requesting the restoration.
     */
    public void restoreHelpArticles(String fileName, boolean merge, User user) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            List<HelpArticle> restoredArticles = (List<HelpArticle>) in.readObject();

            if (merge) {
                // Merge restored articles with current list (without duplicates)
                for (HelpArticle article : restoredArticles) {
                    if (findHelpArticleById(article.getArticleId()) == null && hasAccessToArticle(user, article)) {
                        helpArticles.add(article);

                        // Update helpGroups and specialGroups to reflect the restored article's group memberships
                        for (String groupName : article.getGroups()) {
                            HelpGroup group = findOrCreateGroup(groupName);
                            group.addArticle(article);
                        }

                        for (String specialGroupName : article.getSpecialAccessGroups()) {
                            SpecialAccessGroup specialGroup = findSpecialAccessGroupByName(specialGroupName);
                            if (specialGroup == null) {
                                specialGroup = new SpecialAccessGroup(specialGroupName);
                                specialAccessGroups.add(specialGroup);
                            }
                            specialGroup.addArticle(article);
                        }
                    }
                }
            } else {
                // Replace the current list entirely if allowed.
                if (user instanceof Admin) {
                    // Check if Admin has access to all the articles they are trying to restore
                    boolean hasAccess = restoredArticles.stream().allMatch(article -> hasAccessToArticle(user, article));
                    if (!hasAccess) {
                        System.out.println("Error: Admin does not have access to all articles being restored.");
                        return;
                    }

                    // Replace current articles list if admin has access
                    helpArticles = restoredArticles;

                    // Clear current groups and rebuild them based on restored articles
                    helpGroups.clear();
                    specialAccessGroups.clear();
                    for (HelpArticle article : restoredArticles) {
                        for (String groupName : article.getGroups()) {
                            HelpGroup group = findOrCreateGroup(groupName);
                            group.addArticle(article);
                        }

                        for (String specialGroupName : article.getSpecialAccessGroups()) {
                            SpecialAccessGroup specialGroup = findSpecialAccessGroupByName(specialGroupName);
                            if (specialGroup == null) {
                                specialGroup = new SpecialAccessGroup(specialGroupName);
                                specialAccessGroups.add(specialGroup);
                            }
                            specialGroup.addArticle(article);
                        }
                    }
                } else {
                    System.out.println("Error: Only Admins can fully replace the help articles list.");
                }
            }

            saveHelpArticles();  // Persist the updated articles list
            saveHelpGroups();    // Persist the updated groups list
            saveSpecialAccessGroups(); // Persist the updated special groups list
            System.out.println("Help articles restored successfully from " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error restoring help articles: " + e.getMessage());
        }
    }

    /**
     * Helper method to determine if a user can access a given help article.
     * This method will check if the user has appropriate access rights for special access groups.
     *
     * @param user    The user to check access for.
     * @param article The help article to check.
     * @return True if the user can access the article, false otherwise.
     */
    public boolean hasAccessToArticle(User user, HelpArticle article) {
        if (article.isSpecialAccess()) {
            // For special access articles, check if the user has access using the group.
            for (String specialGroupName : article.getSpecialAccessGroups()) {
                SpecialAccessGroup specialGroup = findSpecialAccessGroupByName(specialGroupName);
                if (specialGroup != null && specialGroup.hasAccess(user)) {
                    return true;  // User has access to this special group, hence to the article.
                }
            }
            return false;  // User doesn't have access to any of the special access groups for this article.
        }
        
        // If it's not a special access article, everyone has access.
        return true;
    }
    
    /**
     * Helper method to determine if a user has access to all specified groups.
     * 
     * Everyone has access to general groups by default, but for special access groups,
     * the method checks if the user has access to each of those groups.
     *
     * @param user       The user to check access for.
     * @param groupNames A list of group names to check.
     * @return True if the user has access to all the groups, false otherwise.
     */
    public boolean hasAccessToGroups(User user, List<String> groupNames) {
        for (String groupName : groupNames) {
            // Check if the group is a special access group
            SpecialAccessGroup specialGroup = findSpecialAccessGroupByName(groupName);

            if (specialGroup != null) {
                // This is a special access group, so we need to check if the user has access
                if (!specialGroup.hasAccess(user)) {
                    return false;  // User doesn't have access to this special group
                }
            }
            
            // If the group is not found in special access groups, it is a general group or a special access group which is still not created.
            // No access checks are needed for general groups as everyone has access.
        }

        // If all groups were checked and no issues were found, return true
        return true;
    }
    
    
    public List<HelpArticle> listHelpArticles(List<String> groupNames, User user) {
        // If the argument list is empty or null, return all articles
        if (groupNames == null || groupNames.isEmpty()) {
            return helpArticles;  // Return all articles if no group is specified
        }

        List<HelpArticle> filteredArticles = new ArrayList<>();

        // Iterate over all the general help groups
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

        // Iterate over all the special access groups
        for (SpecialAccessGroup specialGroup : specialAccessGroups) {
            if (groupNames.contains(specialGroup.getGroupName()) && specialGroup.hasAccess(user)) {
                // Add all articles in this group to the filteredArticles list if user has access
                for (HelpArticle article : specialGroup.getArticles()) {
                    if (!filteredArticles.contains(article)) {
                        filteredArticles.add(article);  // Avoid duplicates
                    }
                }
            }
        }

        return filteredArticles;
    }

    public void removeHelpGroup(String groupName) {
        boolean removed = false;

        // Remove from general help groups
        if (helpGroups.removeIf(group -> group.getGroupName().equals(groupName))) {
            removed = true;
        }

        // Remove from special access groups
        if (specialAccessGroups.removeIf(group -> group.getGroupName().equals(groupName))) {
            removed = true;
        }

        // Save changes if anything was removed
        if (removed) {
            saveHelpGroups(); // Save updated help groups list
            saveSpecialAccessGroups(); // Save updated special access groups list
            System.out.println("Help group removed successfully!");
        } else {
            System.out.println("Error: Help group not found.");
        }
    }
    
    public List<String> getallGroupNames(){
    	List<String> allGroupNames = new ArrayList<>();

        // Add names of all general help groups to the list
        for (HelpGroup group : helpGroups) {
            allGroupNames.add(group.getGroupName());
        }

        // Add names of all special access groups to the list
        for (SpecialAccessGroup group : specialAccessGroups) {
            allGroupNames.add(group.getGroupName());
        }

        // Return the combined list of group names
        return allGroupNames;
    }
    
    public void viewArticle(long articleId) {
        HelpArticle article = findHelpArticleById(articleId);
        if (article == null) {
            System.out.println("Article not found.");
        } else {
            HelpArticle articleToDisplay;

            // If the article is a special access article, we need to create a decrypted copy
            if (article.isSpecialAccess()) {
                // Create a copy of the article and decrypt its content
                articleToDisplay = new HelpArticle(article);
                try {
                    byte[] iv = EncryptionUtils.getInitializationVector(articleToDisplay.getTitle().toCharArray());

                    // Decrypt the body
                    byte[] encryptedBody = Base64.getDecoder().decode(articleToDisplay.getBody());
                    String decryptedBody = new String(encryptionHelper.decrypt(encryptedBody, iv));
                    articleToDisplay.setBody(decryptedBody);

                    // Decrypt references
                    List<String> decryptedReferences = new ArrayList<>();
                    for (String encryptedReference : articleToDisplay.getReferences()) {
                        byte[] encryptedRefBytes = Base64.getDecoder().decode(encryptedReference);
                        String decryptedReference = new String(encryptionHelper.decrypt(encryptedRefBytes, iv));
                        decryptedReferences.add(decryptedReference);
                    }
                    articleToDisplay.setReferences(decryptedReferences);

                } catch (Exception e) {
                    System.out.println("Error decrypting article: " + e.getMessage());
                    return; // Exit if decryption fails
                }
            } else {
                // If the article is not a special access article, we can use it directly
                articleToDisplay = article;
            }

            // Display article details
            System.out.println("=== Article Details ===");
            System.out.println("Title: " + articleToDisplay.getTitle());
            System.out.println("Author: " + articleToDisplay.getAuthor());
            System.out.println("Level: " + articleToDisplay.getLevel());
            System.out.println("Keywords: " + String.join(", ", articleToDisplay.getKeywords()));
            System.out.println("Body: " + articleToDisplay.getBody());
            System.out.println("References: " + String.join(", ", articleToDisplay.getReferences()));
        }
    }

}
    

