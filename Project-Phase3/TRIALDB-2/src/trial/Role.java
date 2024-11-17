package trial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Role class represents a collection of roles assigned to a user.
 * It provides methods to add, remove, and check for specific roles.
 * Implements Serializable to allow objects of this class to be serialized.
 */
public class Role implements Serializable {
    
    private static final long serialVersionUID = 1L;  // Version control for serialization
    private List<String> roleNames;  // List to store role names

    /**
     * Default constructor initializes an empty list of roles.
     */
    public Role() {
        this.roleNames = new ArrayList<>();  // Initialize an empty list
    }

    /**
     * Adds a new role to the list if it doesn't already exist.
     *
     * @param roleName The name of the role to add.
     */
    public void addRole(String roleName) {
        if (!roleNames.contains(roleName)) {  // Add role only if it doesn't already exist
            roleNames.add(roleName);
        }
    }

    /**
     * Removes the specified role from the list, if it exists.
     *
     * @param roleName The name of the role to remove.
     */
    public void removeRole(String roleName) {
        if (roleNames.contains(roleName)) {  // Remove role only if it exists
            roleNames.remove(roleName);
        }
    }

    /**
     * Checks if a specific role is assigned to the user.
     *
     * @param roleName The name of the role to check for.
     * @return true if the role exists, false otherwise.
     */
    public boolean hasRole(String roleName) {
        return roleNames.contains(roleName);  // Return true if the role is present in the list
    }

    /**
     * Returns a list of all roles assigned to the user.
     *
     * @return A new list containing all role names.
     */
    public List<String> getRoles() {
        return new ArrayList<>(roleNames);  // Return a copy of the list to avoid external modification
    }
}
