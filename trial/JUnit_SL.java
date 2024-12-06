package trial;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JUnit_SL {
	
	private Database mockDatabase;
	private SpecialAccessGroup mockGroup;
	
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
       
    	//mockDatabase = new Database();
    }

	//--------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testListAllUsers() {
		System.out.println("\n____________________________________");
    	System.out.println("\nStarting: testListAllUsers...");
    	Admin admin = new Admin("adminUser", "AdminPassword");
        Student student = new Student("studentUser", "StudentPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");
        mockDatabase.addUser(admin);
        mockDatabase.addUser(student);
        mockDatabase.addUser(instructor);
    	String userList = mockDatabase.listAllUsers();
        assertTrue(userList.contains("adminUser"));
        assertTrue(userList.contains("studentUser"));
        assertTrue(userList.contains("instructorUser"));
        System.out.println("____________________________________");
    }
	
    @Test
    public void testFindSpecialAccessGroupByName() {
 		System.out.println("\n____________________________________");
 		System.out.println("\nStarting: testFindSpecialAccessGroupByName...");
    	SpecialAccessGroup group = new SpecialAccessGroup("SpecialGroup");
    	
    	assertTrue(mockDatabase.addSpecialAccessGroup(group));
    	
    	//Verify the group can be found
    	SpecialAccessGroup foundGroup = mockDatabase.findSpecialAccessGroupByName("SpecialGroup");
        assertNotNull(foundGroup);
        assertEquals(foundGroup.getGroupName(), "Found group name should match.");
        // Verify that a non-existent group returns null
        SpecialAccessGroup notFoundGroup = mockDatabase.findSpecialAccessGroupByName("NonExistentGroup");
        assertNull(notFoundGroup);
        System.out.println("____________________________________");
    }
    
	@Test
	public void testFindGroupByName() {
		System.out.println("\n____________________________________");
	    System.out.println("\nStarting: testFindGroupByName...");
	    // Create and add a special access group
	    SpecialAccessGroup specialGroup = new SpecialAccessGroup("SpecialGroup");
	    boolean groupAdded = mockDatabase.addSpecialAccessGroup(specialGroup);
	    // Debugging
	    System.out.println("Group added? " + groupAdded);
	    // Assert the group was added
	    assertTrue(groupAdded);
	    SpecialAccessGroup foundGroup = mockDatabase.findSpecialAccessGroupByName("SpecialGroup");
	    System.out.println("Found group: " + (foundGroup != null ? foundGroup.getGroupName() : "null"));
	    // Assert the group is found and matches
	    assertNotNull(foundGroup);
	    assertEquals("SpecialGroup", foundGroup.getGroupName(), "The group name should match.");
	    // Test for a non-existent group
	    SpecialAccessGroup notFoundGroup = mockDatabase.findSpecialAccessGroupByName("NonExistentGroup");
	    // Debugging
	    System.out.println("Non-existent group: " + (notFoundGroup != null ? notFoundGroup.getGroupName() : "null"));
	    assertNull(notFoundGroup, "Non-existent group should not be found.");
	    System.out.println("\n____________________________________");
	}
	
	@Test
    public void testGetAllGroupNames() {		
		System.out.println("____________________________________");
		System.out.println("\nStarting: testGetAllGroupNames...");
        HelpGroup group1 =  new HelpGroup("Group1");
        HelpGroup group2 =  new HelpGroup("Group2");
        HelpGroup group3 =  new HelpGroup("Group3");
        mockDatabase.addHelpGroup(group1);
        mockDatabase.addHelpGroup(group2);
        mockDatabase.addHelpGroup(group3);
		List<String> groupList = mockDatabase.getallGroupNames();
		System.out.println(groupList);
        assertTrue(groupList.contains("GROUP1"));
        assertTrue(groupList.contains("GROUP2"));
        assertTrue(groupList.contains("GROUP3"));
        System.out.println("____________________________________");
    }

	//--------------------------------------------------------------------------------------------------------------------------------------------


}