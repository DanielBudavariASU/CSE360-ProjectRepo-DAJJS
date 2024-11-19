package trial;

import java.io.Serializable;
import java.util.UUID;

/**
 * The Invitation class represents an invitation that can be sent to users for account creation.
 * Each invitation is associated with a specific role (e.g., admin, user) and includes a unique code.
 * The invitation can also track whether it has been used or not.
 */
public class Invitation implements Serializable {
    
    private static final long serialVersionUID = 1L; // Serialization version identifier
    private String code;  // Unique invitation code
    private String role;  // Role associated with the invitation (e.g., admin, user)
    private boolean isUsed;  // Status flag indicating whether the invitation has been used

    /**
     * Constructs an Invitation with the specified role.
     * A unique code is generated automatically for the invitation, and its used status is initially set to false.
     * 
     * @param role The role associated with this invitation.
     */
    public Invitation(String role) {
        this.code = generateCode(); // Generate a unique invitation code
        this.role = role; // Assign the role to the invitation
        this.isUsed = false; // Initialize the invitation as unused
    }

    /**
     * Generates a random unique code using UUID.
     * This method ensures that each invitation has a distinct code.
     * 
     * @return A randomly generated UUID as a String.
     */
    private String generateCode() {
        return UUID.randomUUID().toString(); // Generate and return a random UUID string
    }

    /**
     * Returns the unique invitation code.
     * 
     * @return The invitation code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the role associated with the invitation.
     * 
     * @return The role for the invitation.
     */
    public String getRole() {
        return role;
    }

    /**
     * Checks if the invitation has been used.
     * 
     * @return True if the invitation is used, false otherwise.
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Marks the invitation as used. Once marked as used, the invitation cannot be used again.
     */
    public void markAsUsed() {
        this.isUsed = true;
    }

    /**
     * Provides a string representation of the Invitation object, including the code, role, and usage status.
     * 
     * @return A string representation of the invitation details.
     */
    @Override
    public String toString() {
        return "Invitation Code: " + code + ", Role: " + role + ", Used: " + isUsed;
    }
}