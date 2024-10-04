package trial;

import java.util.Scanner;

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
            System.out.println("=== Main Menu ===");
            System.out.println("1. Login");
            System.out.println("2. Login with Invitation Code");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Normal login (for admins or users)
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();

                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();

                    User foundUser = db.findUserByUsername(loginUsername);

                    if (foundUser != null && foundUser.validateLogin(loginPassword)) {
                        System.out.println("Login successful. Welcome, " + foundUser.getUsername());

                        if (foundUser instanceof Admin) {
                            Admin adminUser = (Admin) foundUser;
                            boolean adminLoggedIn = true;
                            while (adminLoggedIn) {
                                System.out.println("\n=== Admin Menu ===");
                                System.out.println("1. Send Invitation Code");
                                System.out.println("2. List Users");
                                System.out.println("3. Delete a User");
                                System.out.println("4. Logout");
                                System.out.print("Choose an option: ");
                                int adminChoice = scanner.nextInt();
                                scanner.nextLine();

                                switch (adminChoice) {
                                    case 1:
                                        // Send invitation code
                                        System.out.print("Enter role for new user (e.g., 'User', 'Admin'): ");
                                        String role = scanner.nextLine();
                                        Invitation invitation = adminUser.inviteUser(role);
                                        db.addInvitation(invitation);
                                        System.out.println("Invitation code generated: " + invitation.getCode());
                                        break;

                                    case 2:
                                        // List users
                                        adminUser.listUsers(db);
                                        break;

                                    case 3:
                                        // Delete a user
                                        System.out.print("Enter username to delete: ");
                                        String deleteUsername = scanner.nextLine();
                                        adminUser.deleteUser(db, deleteUsername);
                                        break;

                                    case 4:
                                        // Logout
                                        adminLoggedIn = false;
                                        System.out.println("Logged out of admin account.");
                                        break;

                                    default:
                                        System.out.println("Invalid option. Try again.");
                                }
                            }
                        } else {
                            // Normal user login
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
                        User newUser = new User(newUsername, newPassword, invitation.getRole());
                        db.addUser(newUser);
                        invitation.markAsUsed();  // Mark the invitation as used
                        db.saveInvitations();     // Save the state of invitations
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
}
