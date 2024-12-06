package trial;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JUnit_SearchArticles {
    private Database mockDatabase;
    private Admin admin;
    private Instructor instructorWithAccess;
    private Instructor instructorWithoutAccess;
    private Student student;
    @BeforeEach
    public void setUp() {
    	
    	// Delete all `.ser` files to ensure a fresh setup
        File folder = new File(".");
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".ser")) {
                file.delete();
            }
        }
    	
        // Set up a fresh mock database
        mockDatabase = new Database();
        // Create users: Admin, Instructor, Student
        admin = new Admin("adminUser", "AdminPassword");
        instructorWithAccess = new Instructor("instructorWithAccess", "InstructorPassword");
        instructorWithoutAccess = new Instructor("instructorWithoutAccess", "InstructorPassword");
        student = new Student("studentUser", "StudentPassword");
        mockDatabase.addUser(admin);
        mockDatabase.addUser(instructorWithAccess);
        mockDatabase.addUser(instructorWithoutAccess);
        mockDatabase.addUser(student);
        // Create help groups and special access groups
        HelpGroup generalGroup = new HelpGroup("GeneralGroup");
        SpecialAccessGroup specialGroup = new SpecialAccessGroup("SpecialGroup");
        // Assign users to groups
        specialGroup.addInstructor(instructorWithAccess);
        // Add groups to the database
        mockDatabase.addHelpGroup(generalGroup);
        mockDatabase.addSpecialAccessGroup(specialGroup);
        // Create articles
        List<String> keywords = new ArrayList<>();
        keywords.add("keyword1");
        keywords.add("keyword2");
        List<String> references = new ArrayList<>();
        List<String> generalGroups = new ArrayList<>();
        generalGroups.add("GeneralGroup");
        HelpArticle generalArticle = new HelpArticle("General Article", "author1", "beginner", "This is a general article", keywords, "This is a general article body", references, generalGroups, new ArrayList<>(), false);
        List<String> specialGroups = new ArrayList<>();
        specialGroups.add("SpecialGroup");
        HelpArticle specialArticle = new HelpArticle("Special Article", "author2", "expert", "This is a special access article", keywords, "This is a special access article body", references, new ArrayList<>(), specialGroups, true);
        // Add articles to the database
        mockDatabase.addHelpArticle(generalArticle);
        mockDatabase.addHelpArticle(specialArticle);
    }
    @Test
    public void testSearchByKeyword_GeneralAccess() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testSearchByKeyword_GeneralAccess");
        // Search articles with a keyword in the title or description
        List<HelpArticle> foundArticles = mockDatabase.searchArticles(student, "general", null, "all");
        // Verify that the search returns only the general article
        assertNotNull(foundArticles, "The returned list should not be null.");
        assertEquals(1, foundArticles.size(), "There should be 1 article found.");
        assertEquals("General Article", foundArticles.get(0).getTitle(), "The found article should be the General Article.");
        System.out.println("Test passed successfully for searching general articles by keyword.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testSearchSpecialAccess_InstructorWithAccess() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testSearchSpecialAccess_InstructorWithAccess");
        // Search articles in a special access group by an instructor with access
        List<HelpArticle> foundArticles = mockDatabase.searchArticles(instructorWithAccess, "special", "SpecialGroup", "all");
        // Verify that the instructor with access can see the special access article
        assertNotNull(foundArticles, "The returned list should not be null.");
        assertEquals(1, foundArticles.size(), "There should be 1 article found.");
        assertEquals("Special Article", foundArticles.get(0).getTitle(), "The found article should be the Special Article.");
        System.out.println("Test passed successfully for searching special access articles with access.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testSearchSpecialAccess_InstructorWithoutAccess() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testSearchSpecialAccess_InstructorWithoutAccess");
        // Search articles in a special access group by an instructor without access
        List<HelpArticle> foundArticles = mockDatabase.searchArticles(instructorWithoutAccess, "special", "SpecialGroup", "all");
        // Verify that the instructor without access cannot see the special access article
        assertNotNull(foundArticles, "The returned list should not be null.");
        assertEquals(0, foundArticles.size(), "No articles should be found as the instructor doesn't have access.");
        System.out.println("Test passed successfully for restricted access to special articles.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testSearchWithLevelAndGroup() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testSearchWithLevelAndGroup");
        // Search for beginner-level articles in the general group
        List<HelpArticle> foundArticles = mockDatabase.searchArticles(admin, "", "GeneralGroup", "beginner");
        // Verify that only the general beginner-level article is found
        assertNotNull(foundArticles, "The returned list should not be null.");
        assertEquals(1, foundArticles.size(), "There should be 1 article found.");
        assertEquals("General Article", foundArticles.get(0).getTitle(), "The found article should be the General Article.");
        System.out.println("Test passed successfully for searching articles by level and group.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
}
