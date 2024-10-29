package trial;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();

        // Check if any admin exists. If not, prompt to create the first admin.
        if (!db.isAdminPresent()) {
            System.out.println("No admin found. Creating first admin account.");
            System.out.print("Enter admin username: ");
            String adminUsername = scanner.nextLine();
            System.out.print("Enter admin password: ");
            String adminPassword = scanner.nextLine();
            Admin admin = new Admin(adminUsername, adminPassword);
            db.addUser(admin);
        }

        boolean running = true;
        while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Login");
            System.out.println("2. Login with Invitation Code");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Normal login (for admins, instructors, or users)
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    
                    User foundUser = db.findUserByUsername(loginUsername);
                    if (foundUser != null && foundUser.validateLogin(loginPassword)) {
                        System.out.println("Login successful. Welcome, " + foundUser.getUsername());
                        
                        if (foundUser instanceof Admin) {
                            handleAdminActions(scanner, db, (Admin) foundUser);
                        } else if (foundUser instanceof Instructor) {
                            handleInstructorActions(scanner, db, (Instructor) foundUser);
                        } else {
                            // Normal user login without additional privileges
                            System.out.println("Welcome, " + foundUser.getUsername());
                        }
                    } else {
                        System.out.println("Login failed. Invalid credentials.");
                    }
                    break;

                case 2:
                    // Invitation-based login for new users
                    System.out.print("Enter invitation code: ");
                    String invitationCode = scanner.nextLine();
                    Invitation invitation = db.findInvitationByCode(invitationCode);
                    if (invitation != null && !invitation.isUsed()) {
                        System.out.print("Enter your new username: ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Enter your new password: ");
                        String newPassword = scanner.nextLine();

                        // Create a new user based on the invitation's role
                        if (invitation.getRole().equals("Instructor")) {
                            Instructor newInstructor = new Instructor(newUsername, newPassword);
                            db.addUser(newInstructor);
                        } else {
                            User newUser = new User(newUsername, newPassword, invitation.getRole());
                            db.addUser(newUser);
                        }
                        invitation.markAsUsed();
                        db.saveInvitations();  // Save the state of invitations
                        System.out.println("Account created successfully with role: " + invitation.getRole());
                    } else {
                        System.out.println("Invalid or expired invitation code.");
                    }
                    break;

                case 3:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    private static void handleAdminActions(Scanner scanner, Database db, Admin adminUser) {
        boolean adminLoggedIn = true;
        while (adminLoggedIn) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Send Invitation Code");
            System.out.println("2. List Users");
            System.out.println("3. Delete a User");
            System.out.println("4. Create a Help Article");
            System.out.println("5. Update a Help Article");
            System.out.println("6. Delete a Help Article");
            System.out.println("7. List Help Articles by Group");
            System.out.println("8. Backup Help Articles");
            System.out.println("9. Restore Help Articles");
            System.out.println("10. View a Help Article by ID");  // New option to view article
            System.out.println("11. Logout");
            System.out.print("Choose an option: ");
            int adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    // Send invitation code
                    System.out.print("Enter role for new user (e.g., 'Instructor', 'User', 'Admin'): ");
                    String role = scanner.nextLine();
                    Invitation invitation = adminUser.inviteUser(role);
                    db.addInvitation(invitation);
                    System.out.println("Invitation code generated: " + invitation.getCode());
                    break;

                case 2:
                    // List users
                    System.out.println(db.listAllUsers());
                    break;

                case 3:
                    // Delete a user
                    System.out.print("Enter username to delete: ");
                    String deleteUsername = scanner.nextLine();
                    adminUser.deleteUser(db, deleteUsername);
                    break;

                case 4:
                    // Create a new help article
                    HelpArticle newArticle = getHelpArticleInput(scanner);
                    adminUser.createHelpArticle(db, newArticle);
                    System.out.println("Case 4: " + newArticle.getGroups());
                    break;

                case 5:
                    // Update an existing help article
                    System.out.print("Enter the ID of the article to update: ");
                    long articleId = scanner.nextLong();
                    scanner.nextLine();  // Consume newline
                    HelpArticle updatedArticle = getHelpArticleInput(scanner);
                    updatedArticle.setArticleId(articleId);
                    adminUser.updateHelpArticle(db, updatedArticle);
                    break;

                case 6:
                    // Delete a help article
                    System.out.print("Enter the ID of the article to delete: ");
                    long deleteArticleId = scanner.nextLong();
                    adminUser.deleteHelpArticle(db, deleteArticleId);
                    break;

                case 7:
                    // List help articles by group
                    System.out.print("Enter group names (comma-separated, leave empty for all): ");
                    String groupNamesInput = scanner.nextLine();
                    List<String> groupNames = groupNamesInput.isEmpty() ? null : List.of(groupNamesInput.split(","));
                    List<HelpArticle> articles = adminUser.listHelpArticles(db, groupNames);
                    articles.forEach(article -> System.out.println("ID: " + article.getArticleId() + "\t" 
                    + "Title: " + article.getTitle() + "\t" + "Groups: " + article.getGroups()));
                    break;

                case 8:
                    // Backup help articles (with option to specify groups)
                    System.out.print("Enter backup file name: ");
                    String backupFile = scanner.nextLine();
                    System.out.print("Enter group names (comma-separated, leave empty for all): ");
                    String backupGroupsInput = scanner.nextLine();
                    List<String> backupGroups = backupGroupsInput.isEmpty() ? null : List.of(backupGroupsInput.split(","));
                    adminUser.backupHelpArticles(db, backupFile, backupGroups);  // Backup based on groups or all if empty
                    break;

                case 9:
                    // Restore help articles
                    System.out.print("Enter restore file name: ");
                    String restoreFile = scanner.nextLine();
                    System.out.print("Merge with existing articles? (yes/no): ");
                    boolean merge = scanner.nextLine().equalsIgnoreCase("yes");
                    adminUser.restoreHelpArticles(db, restoreFile, merge);
                    break;

                case 10:
                    // View a help article by ID
                    System.out.print("Enter the ID of the article to view: ");
                    long viewArticleId = scanner.nextLong();
                    HelpArticle article = adminUser.viewHelpArticle(db, viewArticleId);
                    if (article != null) {
                        System.out.println("ID: " + article.getArticleId());
                        System.out.println("Title: " + article.getTitle());
                        System.out.println("Level: " + article.getLevel());
                        System.out.println("Short Description: " + article.getShortDescription());
                        System.out.println("Keywords: " + article.getKeywords());
                        System.out.println("Body: " + article.getBody());
                        System.out.println("References: " + article.getReferences());
                        System.out.println("Groups: " + article.getGroups());
                    } else {
                        System.out.println("Article not found.");
                    }
                    break;

                case 11:
                    adminLoggedIn = false;
                    System.out.println("Logged out of admin account.");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Instructor actions, with similar changes as Admin
    private static void handleInstructorActions(Scanner scanner, Database db, Instructor instructorUser) {
        boolean instructorLoggedIn = true;
        while (instructorLoggedIn) {
            System.out.println("\n=== Instructor Menu ===");
            System.out.println("1. Create a Help Article");
            System.out.println("2. Update a Help Article");
            System.out.println("3. Delete a Help Article");
            System.out.println("4. List Help Articles by Group");
            System.out.println("5. Backup Help Articles");  // Allow backup for instructors
            System.out.println("6. Restore Help Articles");  // Allow restore for instructors
            System.out.println("7. View a Help Article by ID");  // New view option
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");
            int instructorChoice = scanner.nextInt();
            scanner.nextLine();

            switch (instructorChoice) {
                case 1:
                    // Create a new help article
                    HelpArticle newArticle = getHelpArticleInput(scanner);
                    instructorUser.createHelpArticle(db, newArticle);
                    break;

                case 2:
                    // Update an existing help article
                    System.out.print("Enter the ID of the article to update: ");
                    long articleId = scanner.nextLong();
                    scanner.nextLine();  // Consume newline
                    HelpArticle updatedArticle = getHelpArticleInput(scanner);
                    updatedArticle.setArticleId(articleId);
                    instructorUser.updateHelpArticle(db, updatedArticle);
                    break;

                case 3:
                    // Delete a help article
                    System.out.print("Enter the ID of the article to delete: ");
                    long deleteArticleId = scanner.nextLong();
                    instructorUser.deleteHelpArticle(db, deleteArticleId);
                    break;

                case 4:
                    // List help articles by group
                    System.out.print("Enter group names (comma-separated, leave empty for all): ");
                    String groupNamesInput = scanner.nextLine();
                    List<String> groupNames = groupNamesInput.isEmpty() ? null : List.of(groupNamesInput.split(","));
                    List<HelpArticle> articles = instructorUser.listHelpArticles(db, groupNames);
                    articles.forEach(article -> System.out.println("ID: " + article.getArticleId() + "\t" 
                    + "Title: " + article.getTitle() + "\t" + "Groups: " + article.getGroups()));
                    break;

                case 5:
                    // Backup help articles (with option to specify groups)
                    System.out.print("Enter backup file name: ");
                    String backupFile = scanner.nextLine();
                    System.out.print("Enter group names (comma-separated, leave empty for all): ");
                    String backupGroupsInput = scanner.nextLine();
                    List<String> backupGroups = backupGroupsInput.isEmpty() ? null : List.of(backupGroupsInput.split(","));
                    instructorUser.backupHelpArticles(db, backupFile, backupGroups);  // Backup based on groups or all if empty
                    break;

                case 6:
                    // Restore help articles
                    System.out.print("Enter restore file name: ");
                    String restoreFile = scanner.nextLine();
                    System.out.print("Merge with existing articles? (yes/no): ");
                    boolean merge = scanner.nextLine().equalsIgnoreCase("yes");
                    instructorUser.restoreHelpArticles(db, restoreFile, merge);
                    break;

                case 7:
                    // View a help article by ID
                    System.out.print("Enter the ID of the article to view: ");
                    long viewArticleId = scanner.nextLong();
                    HelpArticle article = instructorUser.viewHelpArticle(db, viewArticleId);
                    if (article != null) {
                        System.out.println("ID: " + article.getArticleId());
                        System.out.println("Title: " + article.getTitle());
                        System.out.println("Level: " + article.getLevel());
                        System.out.println("Short Description: " + article.getShortDescription());
                        System.out.println("Keywords: " + article.getKeywords());
                        System.out.println("Body: " + article.getBody());
                        System.out.println("References: " + article.getReferences());
                        System.out.println("Groups: " + article.getGroups());
                    } else {
                        System.out.println("Article not found.");
                    }
                    break;

                case 8:
                    instructorLoggedIn = false;
                    System.out.println("Logged out of instructor account.");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

 // Helper method to get help article input
    private static HelpArticle getHelpArticleInput(Scanner scanner) {
        System.out.print("Enter article title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter article level (e.g., beginner, intermediate, expert): ");
        String level = scanner.nextLine();
        
        System.out.print("Enter short description: ");
        String shortDescription = scanner.nextLine();
        
        System.out.print("Enter article keywords (comma-separated): ");
        String[] keywordArray = scanner.nextLine().split(",");
        List<String> keywords = new ArrayList<>();
        for (String keyword : keywordArray) {
            keywords.add(keyword.trim());
        }
        
        System.out.print("Enter article body: ");
        String body = scanner.nextLine();
        
        System.out.print("Enter article references (comma-separated): ");
        String[] referenceArray = scanner.nextLine().split(",");
        List<String> references = new ArrayList<>();
        for (String reference : referenceArray) {
            references.add(reference.trim());
        }
        
        System.out.print("Enter group names (comma-separated): ");
        String[] groupArray = scanner.nextLine().split(",");
        List<String> groups = new ArrayList<>();
        for (String group : groupArray) {
            groups.add(group.trim());
        }
        
        return new HelpArticle(title, level, shortDescription, keywords, body, references, groups);
    }

}
