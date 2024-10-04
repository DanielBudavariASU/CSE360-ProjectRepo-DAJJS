package trial;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<User> users;
    private List<Invitation> invitations;  // Store invitations
    private final String filename = "users.ser";
    private final String invitationFilename = "invitations.ser";
    // Store any other files required later on in the project

    public Database() {
        this.users = new ArrayList<>();
        this.invitations = new ArrayList<>();
        loadUsers();        // Load existing users if they exist
        loadInvitations();  // Load existing invitations if they exist
    }
    
    // Check if an admin exists
    public boolean isAdminPresent() {
        for (User user : users) {
            if (user.getRole().hasRole("Admin")) {
                return true;
            }
        }
        return false;
    }

    // Add a user and save to the file
    public void addUser(User user) {
        if (findUserByUsername(user.getUsername()) == null) {
            users.add(user);
            saveUsers();
            System.out.println("User added successfully!");
        } else {
            System.out.println("Error: A user with this username already exists.");
        }
    }

    // Find a user by username
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Remove a user
    public void removeUser(User user) {
        users.remove(user);
        saveUsers();
    }

    // List all users
    public void listAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users in the system.");
        } else {
            for (User user : users) {
                System.out.println(user.getUsername());
                System.out.println(user.getRole().getRoles());
            }
        }
    }

    // Add an invitation and save it to file
    public void addInvitation(Invitation invitation) {
        invitations.add(invitation);
        saveInvitations();
    }

    // Find an invitation by code
    public Invitation findInvitationByCode(String code) {
        for (Invitation invitation : invitations) {
            if (invitation.getCode().equals(code) && !invitation.isUsed()) {
                return invitation;
            }
        }
        return null;
    }

    // Save users to file
    private void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Load users from file
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            users = (List<User>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous users found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // Save invitations to file
    void saveInvitations() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(invitationFilename))) {
            out.writeObject(invitations);
        } catch (IOException e) {
            System.out.println("Error saving invitations: " + e.getMessage());
        }
    }

    // Load invitations from file
    @SuppressWarnings("unchecked")
    private void loadInvitations() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(invitationFilename))) {
            invitations = (List<Invitation>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous invitations found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading invitations: " + e.getMessage());
        }
    }
}
