package trial;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class JUnit_Database {

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
    	User foundUser = mockDatabase.findUserByUsername("studentUser");
        assertNotNull(foundUser, "User should be found.");
        assertEquals("studentUser", foundUser.getUsername(), "Found user should match the searched username.");
        
        User notFoundUser = mockDatabase.findUserByUsername("nonExistentUser");
        assertNull(notFoundUser, "Non-existent user should not be found.");
    }

    @Test
    public void testListAllUsers() {
    	System.out.println("\nStarting: testListAllUsers");
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
    	System.out.println("\nStarting: testFindSpecialAccessGroupByName");
    	SpecialAccessGroup group = new SpecialAccessGroup("SpecialGroup");
    	
    	assertTrue(mockDatabase.addSpecialAccessGroup(group), "Group should be added");
    	
    	mockDatabase.addSpecialAccessGroup(group);
        SpecialAccessGroup foundGroup = mockDatabase.findSpecialAccessGroupByName("SpecialGroup");
        
        assertNotNull(foundGroup, "Special access group should be found.");
        assertEquals("SpecialGroup", foundGroup.getGroupName(), "Found group name should match.");
        SpecialAccessGroup notFoundGroup = mockDatabase.findSpecialAccessGroupByName("NonExistentGroup");
        assertNull(notFoundGroup, "Non-existent group should not be found.");
    }

    @Test
    public void testHasAccessToArticle() {
    	System.out.println("\nStarting: testHasAccessToArticle");
    	List<String> keywords = new ArrayList<>();
    	List<String> references = new ArrayList<>();
    	List<String> groups = new ArrayList<>();
    	List<String> specialAccessGroups = new ArrayList<>();
    	HelpArticle article = new HelpArticle("title", "author", "level", "shortDescription", keywords, "body", references, groups, specialAccessGroups, true);
        article.addSpecialAccessGroup("SpecialGroup");
        mockDatabase.addHelpArticle(article);

        // Test for access
        User admin = mockDatabase.findUserByUsername("adminUser");
        assertTrue(mockDatabase.hasAccessToArticle(admin, article), "Admin should have access to the article.");

        User student = mockDatabase.findUserByUsername("studentUser");
        assertTrue(mockDatabase.hasAccessToArticle(student, article), "Student should have access to the article.");

        // Test for no access
        User unauthorizedUser = new Student("unauthorizedUser", "password123");
        assertFalse(mockDatabase.hasAccessToArticle(unauthorizedUser, article), "Unauthorized user should not have access.");
    }

    @Test
    public void testHasAccessToGroups() {
    	System.out.println("\nStarting: testHasAccessToGroups");
    	List<String> groupNames = new ArrayList<>();
        groupNames.add("SpecialGroup");

        User admin = mockDatabase.findUserByUsername("adminUser");
        assertTrue(mockDatabase.hasAccessToGroups(admin, groupNames), "Admin should have access to the group.");

        User student = mockDatabase.findUserByUsername("studentUser");
        assertTrue(mockDatabase.hasAccessToGroups(student, groupNames), "Student should have access to the group.");

        User unauthorizedUser = new Student("unauthorizedUser", "password123");
        assertFalse(mockDatabase.hasAccessToGroups(unauthorizedUser, groupNames), "Unauthorized user should not have access.");
    }
}
