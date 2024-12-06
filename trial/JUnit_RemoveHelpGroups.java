package trial;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class JUnit_RemoveHelpGroups {
    private Database mockDatabase;
    @BeforeEach
    public void setUp() {
    	
    	 // Delete all `.ser` files to ensure a fresh setup
        File folder = new File(".");
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".ser")) {
                file.delete();
            }
        }
        
        System.out.println("\n-----------------------------------------");
        System.out.println("-----------------------------------------");
        System.out.println("Starting Setup for Each Test\n");
    	
        // Setup mock database
        mockDatabase = new Database();
        // Create users: Admin
        Admin admin = new Admin("adminUser", "AdminPassword");
        mockDatabase.addUser(admin);
        // Create help groups
        HelpGroup generalGroup1 = new HelpGroup("GeneralGroup1");
        HelpGroup generalGroup2 = new HelpGroup("GeneralGroup2");
        SpecialAccessGroup specialGroup1 = new SpecialAccessGroup("SpecialGroup1");
        SpecialAccessGroup specialGroup2 = new SpecialAccessGroup("SpecialGroup2");
        // Add groups to the database
        mockDatabase.addHelpGroup(generalGroup1);
        mockDatabase.addHelpGroup(generalGroup2);
        mockDatabase.addSpecialAccessGroup(specialGroup1);
        mockDatabase.addSpecialAccessGroup(specialGroup2);
        
        System.out.println("\nSetup completed successfully!");
        System.out.println("-----------------------------------------");
        System.out.println("-----------------------------------------");
    }
    @Test
    public void testRemoveExistingGeneralHelpGroup() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRemoveExistingGeneralHelpGroup");
        // Remove an existing general group
        boolean removed = mockDatabase.removeHelpGroup("GeneralGroup1");
        // Assert that the group was removed successfully
        assertTrue(removed, "General group 'GeneralGroup1' should be removed successfully.");
        // Verify that the group no longer exists
        assertNull(mockDatabase.findGroupByName("GeneralGroup1"), "General group 'GeneralGroup1' should not be found.");
        System.out.println("General group removal test passed successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testRemoveExistingSpecialAccessGroup() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRemoveExistingSpecialAccessGroup");
        // Remove an existing special access group
        boolean removed = mockDatabase.removeHelpGroup("SpecialGroup1");
        // Assert that the group was removed successfully
        assertTrue(removed, "Special access group 'SpecialGroup1' should be removed successfully.");
        // Verify that the group no longer exists
        assertNull(mockDatabase.findSpecialAccessGroupByName("SpecialGroup1"), "Special access group 'SpecialGroup1' should not be found.");
        System.out.println("Special access group removal test passed successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testRemoveNonExistentGeneralHelpGroup() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRemoveNonExistentGeneralHelpGroup");
        // Attempt to remove a non-existent general group
        boolean removed = mockDatabase.removeHelpGroup("NonExistentGeneralGroup");
        // Assert that the group was not removed
        assertFalse(removed, "Non-existent general group 'NonExistentGeneralGroup' should not be removed.");
        System.out.println("Non-existent general group removal test passed successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testRemoveNonExistentSpecialAccessGroup() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRemoveNonExistentSpecialAccessGroup");
        // Attempt to remove a non-existent special access group
        boolean removed = mockDatabase.removeHelpGroup("NonExistentSpecialAccessGroup");
        // Assert that the group was not removed
        assertFalse(removed, "Non-existent special access group 'NonExistentSpecialAccessGroup' should not be removed.");
        System.out.println("Non-existent special access group removal test passed successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
}