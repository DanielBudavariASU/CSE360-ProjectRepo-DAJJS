package Phase1;
import java.util.ArrayList;
import java.util.List;

public class Role {
    private List<String> roleNames;

    public Role() {
        this.roleNames = new ArrayList<>();
    }

    // Add a new role if it doesn't already exist
    public void addRole(String roleName) {
        if (!roleNames.contains(roleName)) {
            roleNames.add(roleName);
        } 
    }

    // Remove the specified role, if it exists
    public void removeRole(String roleName) {
        if (roleNames.contains(roleName)) {
            roleNames.remove(roleName);
        }
    }

    // Check if the user has a specific role
    public boolean hasRole(String roleName) {
        return roleNames.contains(roleName);
    }

    // Get a list of all roles for this user
    public List<String> getRoles() {
        return new ArrayList<>(roleNames);
    }
}
