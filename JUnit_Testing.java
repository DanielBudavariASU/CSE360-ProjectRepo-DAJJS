package Phase1;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class JUnit_Testing {
	
	private Database mockDatabase;
	private SpecialAccessGroup mockGroup;
	
	@BeforeEach
    public void setUp() {
        mockDatabase = new Database();
        mockGroup = new SpecialAccessGroup("SpecialGroup");

        // Mock users and groups for testing
        Admin admin = new Admin("adminUser", "AdminPassword");
        Student student = new Student("studentUser", "StudentPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");

        mockDatabase.addUser(admin);
        mockDatabase.addUser(student);
        mockDatabase.addUser(instructor);

        mockGroup.addAdmin(admin);
        mockGroup.addStudent(student);
        mockGroup.addInstructor(instructor);
    }

	@Test
    public void testListAllUsers() {
		mockDatabase = new Database();
        mockGroup = new SpecialAccessGroup("SpecialGroup");

        // Mock users and groups for testing
        Admin admin = new Admin("adminUser", "AdminPassword");
        Student student = new Student("studentUser", "StudentPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");

        mockDatabase.addUser(admin);
        mockDatabase.addUser(student);
        mockDatabase.addUser(instructor);

        mockGroup.addAdmin(admin);
        mockGroup.addStudent(student);
        mockGroup.addInstructor(instructor);
        
    	System.out.println("\nStarting: testListAllUsers...");
        String userList = mockDatabase.listAllUsers();
        assertTrue("Admin should be listed.", userList.contains("adminUser"));
        assertTrue("Student should be listed.", userList.contains("studentUser"));
        assertTrue("Instructor should be listed.", userList.contains("instructorUser"));
    }
	
	@Test
	public void testFindGroupByName() { //NOT DONE!!!!!!!!!!!!!!!!!!!!!!!!!
		mockDatabase = new Database();
        mockGroup = new SpecialAccessGroup("SpecialGroup");

        // Mock users and groups for testing
        Admin admin = new Admin("adminUser", "AdminPassword");
        Student student = new Student("studentUser", "StudentPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");

        mockDatabase.addUser(admin);
        mockDatabase.addUser(student);
        mockDatabase.addUser(instructor);

        mockGroup.addAdmin(admin);
        mockGroup.addStudent(student);
        mockGroup.addInstructor(instructor);
		
		System.out.println("\nStarting: testFindGroupByName...");
		// fill in some stuff
		HelpGroup group = new HelpGroup("NormalGroup");
		//assertTrue("Group should be added", mockDatabase.addSpecialAccessGroup(group));
		
	}
	
	@Test
    public void testGetAllGroupNames() {
		mockDatabase = new Database();
        mockGroup = new SpecialAccessGroup("SpecialGroup");

        // Mock users and groups for testing
        Admin admin = new Admin("adminUser", "AdminPassword");
        Student student = new Student("studentUser", "StudentPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");

        mockDatabase.addUser(admin);
        mockDatabase.addUser(student);
        mockDatabase.addUser(instructor);

        mockGroup.addAdmin(admin);
        mockGroup.addStudent(student);
        mockGroup.addInstructor(instructor);
		
		System.out.println("\nStarting: testGetAllGroupNames...");
        String userList = mockDatabase.listAllUsers();
        assertTrue("Admin should be listed.", userList.contains("adminUser"));
        assertTrue( "Student should be listed.", userList.contains("studentUser"));
        assertTrue("Instructor should be listed.", userList.contains("instructorUser"));
    }
	
	@Test
    public void testFindSpecialAccessGroupByName() {
		mockDatabase = new Database();
        mockGroup = new SpecialAccessGroup("SpecialGroup");

        // Mock users and groups for testing
        Admin admin = new Admin("adminUser", "AdminPassword");
        Student student = new Student("studentUser", "StudentPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");

        mockDatabase.addUser(admin);
        mockDatabase.addUser(student);
        mockDatabase.addUser(instructor);

        mockGroup.addAdmin(admin);
        mockGroup.addStudent(student);
        mockGroup.addInstructor(instructor);
		
		System.out.println("\nStarting: testFindSpecialAccessGroupByName...");
    	SpecialAccessGroup group = new SpecialAccessGroup("SpecialGroup");
    	
    	assertTrue("Group should be added", mockDatabase.addSpecialAccessGroup(group));
    	
    	//Verify the group can be found
    	SpecialAccessGroup foundGroup = mockDatabase.findSpecialAccessGroupByName("SpecialGroup");
        assertNotNull("Special access group should be found.", foundGroup);
        assertEquals("SpecialGroup", foundGroup.getGroupName(), "Found group name should match.");

        // Verify that a non-existent group returns null
        SpecialAccessGroup notFoundGroup = mockDatabase.findSpecialAccessGroupByName("NonExistentGroup");
        assertNull("Non-existent group should not be found", notFoundGroup);
    }


}