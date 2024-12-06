package trial;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JUnit_AddSpecialAccessGroup {
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
        
        System.out.println("\nSetup completed successfully!");
        System.out.println("-----------------------------------------");
        System.out.println("-----------------------------------------");
    }
    @Test
    public void testAddNewSpecialAccessGroup() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testAddNewSpecialAccessGroup");
        // Create a new special access group
        SpecialAccessGroup newSpecialGroup = new SpecialAccessGroup("SpecialGroup1");
        // Add the group to the database
        boolean isAdded = mockDatabase.addSpecialAccessGroup(newSpecialGroup);
        // Verify that the group is added successfully
        assertTrue(isAdded, "Special access group 'SpecialGroup1' should be added successfully.");
        // Verify that the group can be found in the database
        assertNotNull(mockDatabase.findSpecialAccessGroupByName("SpecialGroup1"), "Special access group 'SpecialGroup1' should be found in the database.");
        System.out.println("Special access group 'SpecialGroup1' was added successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testAddDuplicateSpecialAccessGroup() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testAddDuplicateSpecialAccessGroup");
        // Create and add a new special access group
        SpecialAccessGroup specialGroup1 = new SpecialAccessGroup("SpecialGroup1");
        mockDatabase.addSpecialAccessGroup(specialGroup1);
        // Attempt to add the same group again
        boolean isAddedAgain = mockDatabase.addSpecialAccessGroup(specialGroup1);
        // Verify that the group is not added again
        assertFalse(isAddedAgain, "Duplicate special access group 'SpecialGroup1' should not be added.");
        System.out.println("Attempt to add duplicate special access group was correctly denied.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testAddMultipleSpecialAccessGroups() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testAddMultipleSpecialAccessGroups");
        // Create multiple special access groups
        SpecialAccessGroup specialGroup1 = new SpecialAccessGroup("SpecialGroup1");
        SpecialAccessGroup specialGroup2 = new SpecialAccessGroup("SpecialGroup2");
        SpecialAccessGroup specialGroup3 = new SpecialAccessGroup("SpecialGroup3");
        // Add all groups to the database
        boolean addedGroup1 = mockDatabase.addSpecialAccessGroup(specialGroup1);
        boolean addedGroup2 = mockDatabase.addSpecialAccessGroup(specialGroup2);
        boolean addedGroup3 = mockDatabase.addSpecialAccessGroup(specialGroup3);
        // Verify that all groups are added successfully
        assertTrue(addedGroup1, "Special access group 'SpecialGroup1' should be added successfully.");
        assertTrue(addedGroup2, "Special access group 'SpecialGroup2' should be added successfully.");
        assertTrue(addedGroup3, "Special access group 'SpecialGroup3' should be added successfully.");
        // Verify that all groups can be found in the database
        assertNotNull(mockDatabase.findSpecialAccessGroupByName("SpecialGroup1"), "Special access group 'SpecialGroup1' should be found in the database.");
        assertNotNull(mockDatabase.findSpecialAccessGroupByName("SpecialGroup2"), "Special access group 'SpecialGroup2' should be found in the database.");
        assertNotNull(mockDatabase.findSpecialAccessGroupByName("SpecialGroup3"), "Special access group 'SpecialGroup3' should be found in the database.");
        System.out.println("Multiple special access groups were added successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
}