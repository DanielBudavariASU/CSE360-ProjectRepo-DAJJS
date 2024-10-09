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
    private final String filename = "users.ser";  // Filename for user data
    private final String invitationFilename = "invitations.ser";  // Filename for invitation data

    /**
     * Constructor for the Database class.
     * Initializes the user and invitation lists and loads existing data from files.
     */
    public Database() {
        this.users = new ArrayList<>();
        this.invitations = new ArrayList<>();
        loadUsers();        // Load existing users if they exist
        loadInvitations();  // Load existing invitations if they exist
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
}
