package trial;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.io.File;

public class JUnit_Database {

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

    //HOMEWORK 8 JUNIT TESTS
    @Test
    public void testHasAccessToArticle() {
    	Database mockDatabase1 = new Database();
    	System.out.println("\nStarting: testHasAccessToArticle");
    	//create user
    	Admin admin = new Admin("adminUser", "AdminPassword");
    	//add user to db
    	mockDatabase1.addUser(admin);
    	//create SAG
    	mockGroup = new SpecialAccessGroup("SpecialGroup3");
    	System.out.println(mockDatabase1.getallGroupNames());
    	//add user to SAG
    	mockGroup.addAdmin(admin);
    	System.out.println("Users in group: " + mockGroup.getAdmins());
    	//create article
    	List <String> special = Arrays.asList("SpecialGroup3");
    	List <String> key = Arrays.asList("key");
    	List <String> ref = Arrays.asList("ref");
    	List <String> group = Arrays.asList("SpecialGroup3");
    	HelpArticle helpArticle = new HelpArticle("title", "author", "level", "description", key, "body", ref, group, special, true);
    	//add article to database
    	mockDatabase1.addHelpArticle(helpArticle);
    	//confirm article is created and has correct info
    	mockDatabase1.viewArticle(helpArticle.getArticleId());
    	//add article to SAG for redundancy sake
    	mockGroup.addArticle(helpArticle);
    	System.out.println(helpArticle.getSpecialAccessGroups());
    	//check if the article is special access (added to group)
    	assertTrue(helpArticle.isSpecialAccess());
    	//check if article is added to group
    	assertTrue(mockGroup.containsArticle(helpArticle));
    	//check if admin has access to the group
    	assertTrue(mockGroup.hasAccess(admin));
    	//check if user has access to article in SAG
    	assertTrue(mockDatabase1.hasAccessToArticle(admin, helpArticle));
    }
    
    @Test
    public void testHasAccessToGroup() {
    	System.out.println("\nStarting: testHasAccessToGroup");
    	//create user
    	Admin admin = new Admin("adminUser", "AdminPassword");
    	//add user to db
    	mockDatabase.addUser(admin);
    	//create SAG
    	mockGroup = new SpecialAccessGroup("SpecialGroup");
    	List <String> special = Arrays.asList("SpecialGroup");
    	//add user to SAG
    	mockGroup.addAdmin(admin);
    	//check if user has access to SAG
    	assertTrue(mockDatabase.hasAccessToGroups(admin, special)); 
    }
    
    @Test
    public void testDeleteLastAdmin() {
    	System.out.println("\nStarting: testDeleteLastAdmin");
    	//create user
    	Admin admin = new Admin("adminUser", "AdminPassword");
    	//add to db
    	mockDatabase.addUser(admin);
    	//check if admin is present
    	assertTrue(mockDatabase.isAdminPresent());
    	//attempt to remove admin
    	assertFalse(mockDatabase.removeUser(admin));
    	
    }
    
    @Test
    public void testIsAdminPresent() {
    	System.out.println("\nStarting: testIsAdminPresent");
        // Test case where an admin exists
    	Admin admin = new Admin("adminUser", "AdminPassword");
    	mockDatabase.addUser(admin);
    	assertTrue(mockDatabase.isAdminPresent(), "Admin should be present in the database.");
    }
    

    @Test
    public void testAddUser() {
    	System.out.println("\nStarting: testAddUser");
        // Adding a new user
        User newUser = new Student("newStudent", "password123");
        mockDatabase.addUser(newUser);
        assertEquals(newUser.getUsername(), mockDatabase.findUserByUsername("newStudent").getUsername(), "Newly added user should exist in the database.");
        User newUser1 = new Student("newStudent", "password123");
        assertFalse(mockDatabase.addUser(newUser1), "Should not add Duplicate Student");
    }

    @Test
    public void testFindUserByUsername() {
    	System.out.println("\nStarting: testFindUserByUsername");
    	User newUser = new Student("studentUser", "password123");
        mockDatabase.addUser(newUser);
    	User foundUser = mockDatabase.findUserByUsername("studentUser");
        assertNotNull(foundUser, "User should be found.");
        assertEquals("studentUser", foundUser.getUsername(), "Found user should match the searched username.");
        
        User notFoundUser = mockDatabase.findUserByUsername("nonExistentUser");
        assertNull(notFoundUser, "Non-existent user should not be found.");
    }

    @Test
    public void testListAllUsersDatabase() {
    	System.out.println("\nStarting: testListAllUsers");
    	Admin admin = new Admin("adminUser", "AdminPassword");
        Student student = new Student("studentUser", "StudentPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");
        mockDatabase.addUser(admin);
        mockDatabase.addUser(student);
        mockDatabase.addUser(instructor);
    	String userList = mockDatabase.listAllUsers();
        assertTrue(userList.contains("adminUser"), "Admin should be listed.");
        assertTrue(userList.contains("studentUser"), "Student should be listed.");
        assertTrue(userList.contains("instructorUser"), "Instructor should be listed.");
    }

    @Test
    public void testFindStudentByUsername() {
    	System.out.println("\nStarting: testFindStudentByUsername");
    	Student student = new Student("studentUser", "StudentPassword");
    	mockDatabase.addStudent(student);
    	Student foundStudent = mockDatabase.findStudentByUsername("studentUser");
    	assertNotNull(foundStudent, "User should be found.");
    	assertEquals("studentUser", foundStudent.getUsername(), "Found student should match the searched username.");
        Student notFoundStudent = mockDatabase.findStudentByUsername("nonExistentStudent");
        assertNull(notFoundStudent, "Non-existent student should not be found.");
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

}
