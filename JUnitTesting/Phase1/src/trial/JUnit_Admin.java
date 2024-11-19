package trial;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trial.Database;
import trial.User;
import trial.Admin;
import trial.Password;


public class JUnit_Admin {
    private Admin admin;
    private Database tempDatabase;

    @BeforeEach
    public void setUp() {
        // Initialize the admin and temporary database
        admin = new Admin("admin", "adminPassword");
        tempDatabase = new Database();

        // Add a test user to the database
        User testUser = new User("testUser", "testPassword", "Regular");
        tempDatabase.addUser(testUser);
    }

    @Test
    public void testResetUserPassword_UserExists() {
        // Act
    	System.out.println("\nStarting: testResetUserPassword UserExists");
        boolean result = admin.resetUserPassword("newPassword", "testUser", tempDatabase);

        // Assert
        assertTrue(result, "Password reset should return true for an existing user.");
        User updatedUser = tempDatabase.findUserByUsername("testUser");
        assertNotNull(updatedUser, "User should still exist in the database.");
        assertTrue(updatedUser.validateLogin("newPassword"), "Password should be validated with the new password.");
    }

    @Test
    public void testResetUserPassword_UserDoesNotExist() {
        // Act
    	System.out.println("\nStarting: testResetUserPassword User Does Not Exist");
        boolean result = admin.resetUserPassword("newPassword", "nonexistentUser", tempDatabase);

        // Assert
        assertFalse(result, "Password reset should return false for a non-existent user.");
    }

    @Test
    public void testDeleteUser_UserExists() {
        // Act
    	System.out.println("\nStarting: testDeleteUser UserExists");
        boolean result = admin.deleteUser(tempDatabase, "testUser");

        // Assert
        assertTrue(result, "Delete should return true for an existing user.");
        assertNull(tempDatabase.findUserByUsername("testUser"), "User should no longer exist in the database.");
    }

    @Test
    public void testDeleteUser_UserDoesNotExist() {
        // Act
    	System.out.println("\nStarting: testDeleteUser User Does Not Exist");
        boolean result = admin.deleteUser(tempDatabase, "nonexistentUser");

        // Assert
        assertFalse(result, "Delete should return false for a non-existent user.");
    }
}
