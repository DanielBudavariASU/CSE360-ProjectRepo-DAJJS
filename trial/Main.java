package trial;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
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
                            // Normal Student login without additional privileges
                            handleStudentActions(scanner, db, (Student) foundUser);
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
                        } else if(invitation.getRole().equals("Admin")) {
                            Admin newAdmin = new Admin(newUsername, newPassword);
                            db.addUser(newAdmin);
                        }
                        else {
                        	//Add a Student
                        	Student newStudent = new Student(newUsername, newPassword);
                            db.addStudent(newStudent);;
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
            System.out.println("5. Delete a Help Article");
            System.out.println("6. List Help Articles by Group");
            System.out.println("7. Backup Help Articles");
            System.out.println("8. Restore Help Articles");
            System.out.println("9. Add User to Special Access Group");
            System.out.println("10. Remove User from Special Access Group");
            System.out.println("11. Logout");
            System.out.print("Choose an option: ");
            int adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    // Send invitation code
                    System.out.print("Enter role for new user (e.g., 'Instructor', 'Student', 'Admin'): ");
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
                    HelpArticle newArticle = getHelpArticleInput(scanner, db);
                    if (newArticle == null) {
                    	break;
                    }
                    adminUser.createHelpArticle(db, newArticle);
                    break;

                case 5:
                    // Delete a help article
                    System.out.print("Enter the ID of the article to delete: ");
                    long deleteArticleId = scanner.nextLong();
                    adminUser.deleteHelpArticle(db, deleteArticleId);
                    break;

                case 6:
                    // List help articles by group
                    System.out.print("Enter group names (comma-separated, leave empty for all): ");
                    String groupNamesInput = scanner.nextLine();
                    List<String> groupNames = groupNamesInput.isEmpty() ? null : List.of(groupNamesInput.split(","));
                    List<HelpArticle> articles = adminUser.listHelpArticles(db, groupNames);
                    articles.forEach(article -> System.out.println(article.getArticleId() + "\t" + article.getTitle() + "\t" + article.getGroups()));
                    break;

                case 7:
                    // Backup help articles (with option to specify groups)
                    System.out.print("Enter backup file name: ");
                    String backupFile = scanner.nextLine();
                    System.out.print("Enter group names (comma-separated, leave empty for all): ");
                    String backupGroupsInput = scanner.nextLine();
                    List<String> backupGroups = backupGroupsInput.isEmpty() ? null : List.of(backupGroupsInput.split(","));

                    // Filter groups based on access permissions
                    List<String> accessibleGroups = new ArrayList<>();
                    List<String> deniedGroups = new ArrayList<>();
                    for (String group : backupGroups) {
                        List<String> singleGroup = List.of(group);
                        if (db.hasAccessToGroups(adminUser, singleGroup)) {
                            accessibleGroups.add(group);
                        } else {
                            deniedGroups.add(group);
                        }
                    }
                    
                    if (!deniedGroups.isEmpty()) {
                        System.out.println("You do not have access to the following groups: " + String.join(", ", deniedGroups));
                    }
                    adminUser.backupHelpArticles(db, backupFile, accessibleGroups);
                    break;

                case 8:
                    // Restore help articles
                    System.out.print("Enter restore file name: ");
                    String restoreFile = scanner.nextLine();
                    System.out.print("Merge with existing articles? (yes/no): ");
                    boolean merge = scanner.nextLine().equalsIgnoreCase("yes");
                    adminUser.restoreHelpArticles(db, restoreFile, merge);
                    break;

                case 9:
                    // Add user to special access group
                    System.out.print("Enter special access group name: ");
                    String addGroupName = scanner.nextLine();
                    SpecialAccessGroup addGroup = db.findSpecialAccessGroupByName(addGroupName);
                    if (addGroup == null) {
                        System.out.println("Error: Special access group does not exist.");
                    } else if (db.hasAccessToGroups(adminUser, List.of(addGroupName))) {
                        System.out.print("Enter user role ('Instructor', 'Student', 'Admin'): ");
                        String addUserRole = scanner.nextLine();
                        System.out.print("Enter username to add: ");
                        String addUsername = scanner.nextLine();
                        User userToAdd = db.findUserByUsername(addUsername);
                        if (userToAdd == null) {
                            System.out.println("Error: User not found.");
                        } else {
                            switch (addUserRole.toLowerCase()) {
                                case "instructor":
                                    if (userToAdd instanceof Instructor) {
                                        adminUser.addInstructorAdminToSpecialAccessGroup(db, addGroupName, (Instructor) userToAdd);
                                        System.out.println("Instructor added successfully to special access group.");
                                    } else {
                                        System.out.println("Error: User is not an instructor.");
                                    }
                                    break;
                                case "student":
                                    if (userToAdd instanceof Student) {
                                        adminUser.addStudentToSpecialAccessGroup(db, addGroupName, (Student) userToAdd);
                                        System.out.println("Student added successfully to special access group.");
                                    } else {
                                        System.out.println("Error: User is not a student.");
                                    }
                                    break;
                                case "admin":
                                    if (userToAdd instanceof Admin) {
                                        adminUser.addAdminToSpecialAccessGroup(db, addGroupName, (Admin) userToAdd);
                                        System.out.println("Admin added successfully to special access group.");
                                    } else {
                                        System.out.println("Error: User is not an admin.");
                                    }
                                    break;
                                default:
                                    System.out.println("Error: Invalid user role.");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Error: You do not have access to this group.");
                    }
                    break;

                case 10:
                    // Remove user from special access group
                    System.out.print("Enter special access group name: ");
                    String removeGroupName = scanner.nextLine();
                    SpecialAccessGroup removeGroup = db.findSpecialAccessGroupByName(removeGroupName);
                    if (removeGroup == null) {
                        System.out.println("Error: Special access group does not exist.");
                    } else if (db.hasAccessToGroups(adminUser, List.of(removeGroupName))) {
                        System.out.print("Enter user role ('Instructor', 'Student', 'Admin'): ");
                        String removeUserRole = scanner.nextLine();
                        System.out.print("Enter username to remove: ");
                        String removeUsername = scanner.nextLine();
                        User userToRemove = db.findUserByUsername(removeUsername);
                        if (userToRemove == null) {
                            System.out.println("Error: User not found.");
                        } else {
                            switch (removeUserRole.toLowerCase()) {
                                case "instructor":
                                    if (userToRemove instanceof Instructor) {
                                        adminUser.removeInstructorAdminFromSpecialAccessGroup(db, removeGroupName, (Instructor) userToRemove);
                                        System.out.println("Instructor removed successfully from special access group.");
                                    } else {
                                        System.out.println("Error: User is not an instructor.");
                                    }
                                    break;
                                case "student":
                                    if (userToRemove instanceof Student) {
                                        adminUser.removeStudentFromSpecialAccessGroup(db, removeGroupName, (Student) userToRemove);
                                        System.out.println("Student removed successfully from special access group.");
                                    } else {
                                        System.out.println("Error: User is not a student.");
                                    }
                                    break;
                                case "admin":
                                    if (userToRemove instanceof Admin) {
                                        adminUser.removeAdminFromSpecialAccessGroup(db, removeGroupName, (Admin) userToRemove);
                                        System.out.println("Admin removed successfully from special access group.");
                                    } else {
                                        System.out.println("Error: User is not an admin.");
                                    }
                                    break;
                                default:
                                    System.out.println("Error: Invalid user role.");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Error: You do not have access to this group.");
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


    private static void handleInstructorActions(Scanner scanner, Database db, Instructor instructorUser) {
        boolean instructorLoggedIn = true;
        while (instructorLoggedIn) {
            System.out.println("\n=== Instructor Menu ===");
            System.out.println("1. Create a Help Article");
            System.out.println("2. Update a Help Article");
            System.out.println("3. Delete a Help Article");
            System.out.println("4. List Help Articles by Group");
            System.out.println("5. Search Help Articles");
            System.out.println("6. Backup Help Articles");  // Allow backup for instructors
            System.out.println("7. Restore Help Articles");  // Allow restore for instructors
            System.out.println("8. View a Help Article by ID");
            System.out.println("9. Create Special Access Group");
            System.out.println("10. Delete Special Access Group");
            System.out.println("11. Add User to Special Access Group");
            System.out.println("12. Remove User from Special Access Group");
            System.out.println("13. List Students in Special Access Group");
            System.out.println("14. Remove Student from Help System");
            System.out.println("15. Logout");
            System.out.print("Choose an option: ");
            int instructorChoice = scanner.nextInt();
            scanner.nextLine();

            switch (instructorChoice) {
                case 1:
                    // Create a new help article
                    HelpArticle newArticle = getHelpArticleInput(scanner, db);
                    if (newArticle == null) {
                    	break;
                    }
                    instructorUser.createHelpArticle(db, newArticle);
                    break;

                case 2:
                    // Update an existing help article
                    System.out.print("Enter the ID of the article to update: ");
                    long updateArticleId = scanner.nextLong();
                    scanner.nextLine();  // Consume newline
                    
                    // Check access to the article
                    HelpArticle articleToUpdate = db.findHelpArticleById(updateArticleId);
                    if (articleToUpdate != null && db.hasAccessToArticle(instructorUser, articleToUpdate)) {
                        HelpArticle updatedArticle = getHelpArticleInput(scanner, db);
                        if (updatedArticle == null) {
                        	break;
                        }
                        updatedArticle.setArticleId(updateArticleId);
                        instructorUser.updateHelpArticle(db, updatedArticle);
                    } else {
                        System.out.println("Access Denied or Article not found.");
                    }
                    break;

                case 3:
                    // Delete a help article
                    System.out.print("Enter the ID of the article to delete: ");
                    long deleteArticleId = scanner.nextLong();
                    
                    // Check access to the article
                    HelpArticle articleToDelete = db.findHelpArticleById(deleteArticleId);
                    if (articleToDelete != null && db.hasAccessToArticle(instructorUser, articleToDelete)) {
                        instructorUser.deleteHelpArticle(db, deleteArticleId);
                    } else {
                        System.out.println("Access Denied or Article not found.");
                    }
                    break;

                case 4:
                	// List help articles by group
                	System.out.print("Enter group names (comma-separated, leave empty for all): ");
                	String groupNamesInput = scanner.nextLine();
                	List<String> groupNames = groupNamesInput.isEmpty() ? null : List.of(groupNamesInput.split(","));

                	if (groupNames == null) {
                	    groupNames = db.getallGroupNames();
                	    System.out.println("All Group Names " + groupNames);
                	}

                	// Check access to each group
                	List<String> inaccessibleGroups = new ArrayList<>();
                	Set<HelpArticle> articlesToDisplay = new HashSet<>();
                	for (String groupName : groupNames) {
                	    if (db.findGroupByName(groupName) != null || db.findSpecialAccessGroupByName(groupName) != null) {
                	        if (db.hasAccessToGroups(instructorUser, List.of(groupName))) {
                	            articlesToDisplay.addAll(instructorUser.listHelpArticles(db, List.of(groupName)));
                	        } else {
                	            inaccessibleGroups.add(groupName);
                	        }
                	    } else {
                	        System.out.println("Group " + groupName + " does not exist.");
                	    }
                	}

                	// Display inaccessible groups message if any
                	if (!inaccessibleGroups.isEmpty()) {
                	    System.out.println("Access denied to groups: " + inaccessibleGroups);
                	}

                	// Display the articles (Set will remove duplicates)
                	articlesToDisplay.forEach(article -> 
                	    System.out.println(article.getArticleId() + "\t" + article.getTitle() + "\t" + article.getGroups() + "\t" + article.getSpecialAccessGroups()));
                	
                	break;

                case 5:
                    // Search help articles (similar to student search functionality)
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    
                    System.out.print("Enter group name (or type 'all' to search all groups): ");
                    String groupName = scanner.nextLine();
                    if (groupName.equalsIgnoreCase("all")) {
                        groupName = null;  // Null to represent no group restriction
                    }

                    System.out.print("Enter level (e.g., beginner, intermediate, advanced, expert, or 'all'): ");
                    String level = scanner.nextLine();

                    // Perform search and get results
                    List<HelpArticle> searchResults = instructorUser.searchHelpArticles(db, keyword, groupName, level);
                    System.out.println("\n=== Search Results ===");
                    System.out.println(groupName == null ? "Active Group: All Groups" : "Active Group: " + groupName);

                    if (searchResults.isEmpty()) {
                        System.out.println("No articles found.");
                    } else {
                        int beginnerCount = 0, intermediateCount = 0, advancedCount = 0, expertCount = 0;

                        for (HelpArticle article : searchResults) {
                            switch (article.getLevel().toLowerCase()) {
                                case "beginner": beginnerCount++; break;
                                case "intermediate": intermediateCount++; break;
                                case "advanced": advancedCount++; break;
                                case "expert": expertCount++; break;
                            }
                        }

                        System.out.println("Number of articles by level:");
                        System.out.println("Beginner: " + beginnerCount);
                        System.out.println("Intermediate: " + intermediateCount);
                        System.out.println("Advanced: " + advancedCount);
                        System.out.println("Expert: " + expertCount);

                        System.out.println("\n=== Matching Articles ===");
                        int count = 1;
                        for (HelpArticle article : searchResults) {
                            System.out.println(count++ + ". Article ID: " + article.getArticleId() +
                                               " | Title: " + article.getTitle() +
                                               " | Author: " + article.getAuthor() +
                                               " | Short Description: " + article.getShortDescription());
                        }
                    }
                    break;

                case 6:
                    // Backup help articles (with access check)
                    System.out.print("Enter backup file name: ");
                    String backupFile = scanner.nextLine();
                    System.out.print("Enter group names (comma-separated, leave empty for all): ");
                    String backupGroupsInput = scanner.nextLine();
                    List<String> backupGroups = backupGroupsInput.isEmpty() ? null : List.of(backupGroupsInput.split(","));

                    // Check access to each group
                    List<String> deniedGroups = new ArrayList<>();
                    List<String> accessibleGroups = new ArrayList<>();
                    for (String group : backupGroups) {
                        if (db.hasAccessToGroups(instructorUser, List.of(group))) {
                            accessibleGroups.add(group);
                        } else {
                            deniedGroups.add(group);
                        }
                    }

                    if (!deniedGroups.isEmpty()) {
                        System.out.println("Access denied for the following groups: " + deniedGroups);
                    }
                    instructorUser.backupHelpArticles(db, backupFile, accessibleGroups);
                    break;

                case 7:
                    // Restore help articles
                    System.out.print("Enter restore file name: ");
                    String restoreFile = scanner.nextLine();
                    System.out.print("Merge with existing articles? (yes/no): ");
                    boolean merge = scanner.nextLine().equalsIgnoreCase("yes");
                    instructorUser.restoreHelpArticles(db, restoreFile, merge);
                    break;

                case 8:
                    // View article in detail by Article ID
                    System.out.print("Enter the Article ID to view in detail: ");
                    long articleId = scanner.nextLong();
                    scanner.nextLine();  // Consume newline

                    HelpArticle selectedArticle = db.findHelpArticleById(articleId);
                    if (selectedArticle == null) {
                        System.out.println("Invalid Article ID. Article not found.");
                    } else if (db.hasAccessToArticle(instructorUser, selectedArticle)) {
                        instructorUser.viewArticle(db, articleId);
                    } else {
                        System.out.println("Access Denied!!");
                    }
                    break;

                case 9:
                    // Create a special access group
                    System.out.print("Enter the name of the special access group: ");
                    String specialAccessGroupName = scanner.nextLine();
                    SpecialAccessGroup newGroup = new SpecialAccessGroup(specialAccessGroupName);
                    instructorUser.createGroup(db, newGroup);
                    db.addInstructorAdminToSpecialAccessGroup(newGroup.getGroupName(), instructorUser);
//                    // Adding instructor as an admin
//                    newGroup.addInstructor(instructorUser);
//                    db.saveSpecialAccessGroups();
                    break;

                case 10:
                    // Delete a special access group
                    System.out.print("Enter the name of the special access group to delete: ");
                    String deleteGroupName = scanner.nextLine();
                    instructorUser.deleteGroup(db, deleteGroupName);
                    break;

                case 11:
                    // Add user to special access group
                    System.out.print("Enter the username to add to the special access group: ");
                    String usernameToAdd = scanner.nextLine();
                    User userToAdd = db.findUserByUsername(usernameToAdd);

                    if (userToAdd != null) {
                        System.out.print("Enter the name of the special access group: ");
                        String groupNameToAdd = scanner.nextLine();
                        SpecialAccessGroup groupToAdd = db.findSpecialAccessGroupByName(groupNameToAdd);

                        if (groupToAdd != null && db.hasAccessToGroups(instructorUser, List.of(groupNameToAdd))) {
                            if (userToAdd instanceof Student) {
                                instructorUser.addStudentToSpecialAccessGroup(db, groupNameToAdd, (Student) userToAdd);
                            } else if (userToAdd instanceof Instructor) {
                                instructorUser.addInstructorAdminToSpecialAccessGroup(db, groupNameToAdd, (Instructor) userToAdd);
                            } else if (userToAdd instanceof Admin) {
                                instructorUser.addAdminToSpecialAccessGroup(db, groupNameToAdd, (Admin) userToAdd);
                            }
                        } else {
                            System.out.println("Access Denied or Group not found.");
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 12:
                    // Remove user from special access group
                    System.out.print("Enter the username to remove from the special access group: ");
                    String usernameToRemove = scanner.nextLine();
                    User userToRemove = db.findUserByUsername(usernameToRemove);

                    if (userToRemove != null) {
                        System.out.print("Enter the name of the special access group: ");
                        String groupNameToRemove = scanner.nextLine();
                        SpecialAccessGroup groupToRemove = db.findSpecialAccessGroupByName(groupNameToRemove);

                        if (groupToRemove != null && db.hasAccessToGroups(instructorUser, List.of(groupNameToRemove))) {
                            if (userToRemove instanceof Student) {
                                instructorUser.removeStudentFromSpecialAccessGroup(db, groupNameToRemove, (Student) userToRemove);
                            } else if (userToRemove instanceof Instructor) {
                                instructorUser.removeInstructorAdminFromSpecialAccessGroup(db, groupNameToRemove, (Instructor) userToRemove);
                            } else if (userToRemove instanceof Admin) {
                                instructorUser.removeAdminFromSpecialAccessGroup(db, groupNameToRemove, (Admin) userToRemove);
                            }
                        } else {
                            System.out.println("Access Denied or Group not found.");
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 13:
                    // List students in special access group
                    System.out.print("Enter the name of the special access group: ");
                    String listGroupName = scanner.nextLine();
                    List<String> studentsInGroup = instructorUser.viewStudentsInSpecialAccessGroup(db, listGroupName);

                    if (studentsInGroup != null && !studentsInGroup.isEmpty()) {
                        studentsInGroup.forEach(student -> System.out.println("Student Username: " + student));
                    } else {
                        System.out.println("No students found in the group or Group does not exist.");
                    }
                    break;

                case 14:
                    // Remove student from help system
                    System.out.print("Enter the username of the student to remove: ");
                    String studentUsername = scanner.nextLine();
                    Student studentToRemove = (Student) db.findUserByUsername(studentUsername);

                    if (studentToRemove != null) {
                        instructorUser.removeStudent(db, studentToRemove);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 15:
                    instructorLoggedIn = false;
                    System.out.println("Logged out of instructor account.");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    
    private static void handleStudentActions(Scanner scanner, Database db, Student studentUser) {
        boolean studentLoggedIn = true;

        while (studentLoggedIn) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. Search Help Articles");
            System.out.println("2. View Article Details");
            System.out.println("3. Send Generic Feedback");
            System.out.println("4. Send Specific Help Request");
            System.out.println("5. View Search History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int studentChoice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (studentChoice) {
                case 1:
                	// Search help articles
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    
                    System.out.print("Enter group name (or type 'all' to search all groups): ");
                    String groupName = scanner.nextLine();
                    if (groupName.equalsIgnoreCase("all")) {
                        groupName = null;  // Null to represent no group restriction
                    }

                    System.out.print("Enter level (e.g., beginner, intermediate, advanced, expert, or 'all'): ");
                    String level = scanner.nextLine();

                    // Perform search and get results
                    List<HelpArticle> searchResults = studentUser.searchHelpArticles(db, keyword, groupName, level);
                    
                    // Output details about the active group and search results
                    System.out.println("\n=== Search Results ===");
                    if (groupName == null) {
                        System.out.println("Active Group: All Groups");
                    } else {
                        System.out.println("Active Group: " + groupName);
                    }

                    if (searchResults.isEmpty()) {
                        System.out.println("No articles found.");
                    } else {
                        // Count articles by level
                        int beginnerCount = 0, intermediateCount = 0, advancedCount = 0, expertCount = 0;

                        for (HelpArticle article : searchResults) {
                            switch (article.getLevel().toLowerCase()) {
                                case "beginner": beginnerCount++; break;
                                case "intermediate": intermediateCount++; break;
                                case "advanced": advancedCount++; break;
                                case "expert": expertCount++; break;
                            }
                        }

                        // Display the number of articles for each level
                        System.out.println("Number of articles by level:");
                        System.out.println("Beginner: " + beginnerCount);
                        System.out.println("Intermediate: " + intermediateCount);
                        System.out.println("Advanced: " + advancedCount);
                        System.out.println("Expert: " + expertCount);

                        // Display the short form list of articles matching the search criteria
                        System.out.println("\n=== Matching Articles ===");
                        int count = 1;
                        for (HelpArticle article : searchResults) {
                        	System.out.println(count++ + ". Article ID: " + article.getArticleId() +
                                    " | Title: " + article.getTitle() +
                                    " | Author: " + article.getAuthor() +
                                    " | Short Description: " + article.getShortDescription());
                        }
                    }
                    break;

                case 2:
                	// View article in detail by Article ID
                    System.out.print("Enter the Article ID to view in detail: ");
                    long articleId = scanner.nextLong();
                    scanner.nextLine();  // Consume newline

                    HelpArticle selectedArticle = db.findHelpArticleById(articleId);
                    if (selectedArticle == null) {
                        System.out.println("Invalid Article ID. Article not found.");
                    } else {
                    	if (db.hasAccessToArticle(studentUser, selectedArticle)){
                    		studentUser.viewArticle(db, articleId);
                    	}
                    	else {
                    		System.out.println("Access Denied!!");
                    	}
                    }
                    break;

                case 3:
                    // Send generic feedback
                    System.out.print("Enter your feedback message: ");
                    String genericFeedback = scanner.nextLine();
                    studentUser.sendGenericFeedback(genericFeedback);
                    break;

                case 4:
                    // Send specific help request
                    System.out.print("Enter your specific request message: ");
                    String specificRequest = scanner.nextLine();
                    studentUser.sendSpecificRequest(specificRequest);
                    break;

                case 5:
                    // View search history
                    List<String> searchHistory = studentUser.getSearchHistory();
                    System.out.println("=== Search History ===");
                    if (searchHistory.isEmpty()) {
                        System.out.println("No search history available.");
                    } else {
                        searchHistory.forEach(System.out::println);
                    }
                    break;

                case 6:
                    studentLoggedIn = false;
                    System.out.println("Logged out of student account.");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    

    // Helper method to get help article input
//    private static HelpArticle getHelpArticleInput(Scanner scanner, Database db) {
//        System.out.print("Enter article title: ");
//        String title = scanner.nextLine();
//        
//        System.out.print("Enter article author: ");
//        String author = scanner.nextLine();
//        
//        System.out.print("Enter article level (e.g., beginner, intermediate, expert): ");
//        String level = scanner.nextLine();
//        
//        System.out.print("Enter short description: ");
//        String shortDescription = scanner.nextLine();
//        
//        System.out.print("Enter article keywords (comma-separated): ");
//        String[] keywordArray = scanner.nextLine().split(",");
//        List<String> keywords = new ArrayList<>();
//        for (String keyword : keywordArray) {
//            keywords.add(keyword.trim());
//        }
//        
//        System.out.print("Enter article body: ");
//        String body = scanner.nextLine();
//        
//        System.out.print("Enter article references (comma-separated): ");
//        String[] referenceArray = scanner.nextLine().split(",");
//        List<String> references = new ArrayList<>();
//        for (String reference : referenceArray) {
//            references.add(reference.trim());
//        }
//        
//        System.out.print("Is this article in a General access group? (yes/no): ");
//        boolean isInGeneralAccessGroup = scanner.nextLine().equalsIgnoreCase("yes");
//        List<String> groups = new ArrayList<>();
//        
//        if (isInGeneralAccessGroup) {
//	        System.out.print("Enter group names (comma-separated): ");
//	        String[] groupArray = scanner.nextLine().split(",");
//	        for (String group : groupArray) {
//	            groups.add(group.trim());
//	        }
//        }
//
//        System.out.print("Is this article in a special access group? (yes/no): ");
//        boolean isInSpecialAccessGroup = scanner.nextLine().equalsIgnoreCase("yes");
//
//        List<String> specialGroups = new ArrayList<>();
//        if (isInSpecialAccessGroup) {
//            System.out.print("Enter special access group names (comma-separated): ");
//            String[] specialGroupArray = scanner.nextLine().split(",");
//            for (String specialGroup : specialGroupArray) {
//                specialGroup = specialGroup.trim();
//                if (db.findSpecialAccessGroupByName(specialGroup) == null) {
//                    System.out.println("Special access group '" + specialGroup + "' does not exist. It needs to be created by an instructor first.");
//                } else {
//                    specialGroups.add(specialGroup);
//                }
//            }
//
//            // If no valid special access groups were provided, prompt again or set as not special
//            if (specialGroups.isEmpty()) {
//                System.out.println("No valid special access groups provided. Quitting current process..");
//                return null;
//            }
//        }
//
//		// Return a new HelpArticle object with the updated constructor
//        return new HelpArticle(title, author, level, shortDescription, keywords, body, references, groups, specialGroups, isInSpecialAccessGroup);
//    }
    
 // Helper method to get help article input
    private static HelpArticle getHelpArticleInput(Scanner scanner, Database db) {
        System.out.print("Enter article title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter article author: ");
        String author = scanner.nextLine();
        
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
        
        // Handling General Access Groups
        System.out.print("Is this article in a General access group? (yes/no): ");
        boolean isInGeneralAccessGroup = scanner.nextLine().equalsIgnoreCase("yes");
        List<String> groups = new ArrayList<>();
        
        if (isInGeneralAccessGroup) {
            System.out.print("Enter group names (comma-separated): ");
            String[] groupArray = scanner.nextLine().split(",");
            for (String group : groupArray) {
                group = group.trim();
                if (db.findGroupByName(group) == null) {
                    System.out.println("General group '" + group + "' does not exist. It needs to be created first.");
                } else {
                    groups.add(group);
                }
            }

            // If no valid general groups were provided, prompt again or set as no general group
            if (groups.isEmpty()) {
                System.out.println("No valid general access groups provided. Setting as no general access groups.");
            }
        }

        // Handling Special Access Groups
        System.out.print("Is this article in a special access group? (yes/no): ");
        boolean isInSpecialAccessGroup = scanner.nextLine().equalsIgnoreCase("yes");

        List<String> specialGroups = new ArrayList<>();
        if (isInSpecialAccessGroup) {
            System.out.print("Enter special access group names (comma-separated): ");
            String[] specialGroupArray = scanner.nextLine().split(",");
            for (String specialGroup : specialGroupArray) {
                specialGroup = specialGroup.trim();
                if (db.findSpecialAccessGroupByName(specialGroup) == null) {
                    System.out.println("Special access group '" + specialGroup + "' does not exist. It needs to be created by an instructor first.");
                } else {
                    specialGroups.add(specialGroup);
                }
            }

            // If no valid special access groups were provided, prompt again or set as not special
            if (specialGroups.isEmpty()) {
                System.out.println("No valid special access groups provided. Quitting current process..");
                return null;
            }
        }

        // Return a new HelpArticle object with the updated constructor
        return new HelpArticle(title, author, level, shortDescription, keywords, body, references, groups, specialGroups, isInSpecialAccessGroup);
    }


}