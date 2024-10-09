package trial;

import java.io.Serializable;
import java.util.UUID;

public class Invitation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String role;  // Role associated with the invitation
    private boolean isUsed;

    public Invitation(String role) {
        this.code = generateCode();
        this.role = role;
        this.isUsed = false;
    }

    // Generates a random invitation code
    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    public String getCode() {
        return code;
    }

    public String getRole() {
        return role;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void markAsUsed() {
        this.isUsed = true;
    }

    @Override
    public String toString() {
        return "Invitation Code: " + code + ", Role: " + role + ", Used: " + isUsed;
    }
}
