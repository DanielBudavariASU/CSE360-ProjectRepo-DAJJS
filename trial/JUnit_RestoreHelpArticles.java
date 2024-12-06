package trial;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class JUnit_RestoreHelpArticles {
    private Database mockDatabase;
    private String backupFileName = "articles_backup.ser";
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
        // Create users
        Admin adminWithFullAccess = new Admin("adminFullAccess", "password");
        Instructor instructorFullAccess = new Instructor("instructorFullAccess", "password");
        Instructor instructorOnlySAG1 = new Instructor("instructorOnlySAG1", "password");
        Instructor instructorNoSpecialAccess = new Instructor("instructorNoSpecialAccess", "password");
        Student studentWithSAG1 = new Student("studentWithSAG1", "password");
        // Add users to the database
        mockDatabase.addUser(adminWithFullAccess);
        mockDatabase.addUser(instructorFullAccess);
        mockDatabase.addUser(instructorOnlySAG1);
        mockDatabase.addUser(instructorNoSpecialAccess);
        mockDatabase.addUser(studentWithSAG1);
        // Create help groups
        HelpGroup generalGroup = new HelpGroup("GeneralGroup");
        SpecialAccessGroup specialGroup1 = new SpecialAccessGroup("SAG1");
        SpecialAccessGroup specialGroup2 = new SpecialAccessGroup("SAG2");
        // Add users to special groups
        specialGroup1.addInstructorAdmin(instructorFullAccess);
        specialGroup1.addInstructorAdmin(instructorOnlySAG1);
        specialGroup1.addStudent(studentWithSAG1);
        specialGroup1.addAdmin(adminWithFullAccess);
        specialGroup2.addInstructorAdmin(instructorFullAccess);
        specialGroup2.addAdmin(adminWithFullAccess);
        // Add groups to the database
        mockDatabase.addHelpGroup(generalGroup);
        mockDatabase.addSpecialAccessGroup(specialGroup1);
        mockDatabase.addSpecialAccessGroup(specialGroup2);
        // Create articles
        List<String> keywords = new ArrayList<>();
        List<String> references = new ArrayList<>();
        
        List<String> generalGroups = new ArrayList<>();
        generalGroups.add("GeneralGroup");
        HelpArticle articleGeneral = new HelpArticle("General Article", "author1", "beginner", "General article description", keywords, "This is a general article body.", references, generalGroups, new ArrayList<>(), false);
        List<String> specialGroups1 = new ArrayList<>();
        specialGroups1.add("SAG1");
        HelpArticle articleSAG1 = new HelpArticle("SAG1 Article", "author2", "intermediate", "SAG1 article description", keywords, "This is a SAG1 article body.", references, new ArrayList<>(), specialGroups1, true);
        List<String> specialGroups2 = new ArrayList<>();
        specialGroups2.add("SAG2");
        HelpArticle articleSAG2 = new HelpArticle("SAG2 Article", "author3", "expert", "SAG2 article description", keywords, "This is a SAG2 article body.", references, new ArrayList<>(), specialGroups2, true);
        // Add articles to the database
        mockDatabase.addHelpArticle(articleGeneral);
        mockDatabase.addHelpArticle(articleSAG1);
        mockDatabase.addHelpArticle(articleSAG2);
        // Backup all articles with the admin having access to all groups
        mockDatabase.backupHelpArticles(backupFileName, List.of("GeneralGroup", "SAG1", "SAG2"), adminWithFullAccess);
        
        System.out.println("\nSetup completed successfully!");
        System.out.println("-----------------------------------------");
        System.out.println("-----------------------------------------");
    }
    
    @Test
    public void testRestoreArticles_InstructorWithFullAccess() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRestoreArticles_InstructorWithFullAccess");
       
        // Get the instructor with full access
        Instructor instructorFullAccess = (Instructor) mockDatabase.findUserByUsername("instructorFullAccess");
        // Restore articles
        List<HelpArticle> restoredArticles = mockDatabase.restoreHelpArticles(backupFileName, true, instructorFullAccess);
        // Verify all articles are restored
        assertNotNull(restoredArticles, "Restored articles list should not be null.");
        assertEquals(3, restoredArticles.size(), "All three articles should be restored.");
        System.out.println("All articles restored successfully for instructor with full access.");
        // Additional verification for special access groups
        boolean hasSAG1Article = false;
        boolean hasSAG2Article = false;
        for (HelpArticle article : restoredArticles) {
            System.out.println("Restored Article Title: " + article.getTitle() + ", Special Access Groups: " + article.getSpecialAccessGroups());
            
            if (article.getSpecialAccessGroups().contains("SAG1")) {
                hasSAG1Article = true;
            }
            if (article.getSpecialAccessGroups().contains("SAG2")) {
                hasSAG2Article = true;
            }
        }
        // Verify that articles from each special access group are restored
        assertTrue(hasSAG1Article, "There should be at least one article from SAG1 restored.");
        assertTrue(hasSAG2Article, "There should be at least one article from SAG2 restored.");
        System.out.println("All articles from SAG1, and SAG2 have been restored successfully for instructor with full access.");
        System.out.println("\n-----------------   TEST END  --------------------"); 
    }
    @Test
    public void testRestoreArticles_InstructorWithOnlySAG1Access() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRestoreArticles_InstructorWithOnlySAG1Access");
        
        Instructor instructorOnlySAG1 = (Instructor) mockDatabase.findUserByUsername("instructorOnlySAG1");
        
        // Restore articles
        List<HelpArticle> restoredArticles = mockDatabase.restoreHelpArticles(backupFileName, true, instructorOnlySAG1);
        // Verify only articles in SAG1 and general group are restored
        assertNotNull(restoredArticles, "Restored articles list should not be null.");
        assertEquals(2, restoredArticles.size(), "Only two articles should be restored (General and SAG1).");
        
        boolean hasGeneralArticle = false;
        boolean hasSAG1Article = false;
        for (HelpArticle article : restoredArticles) {
            if (article.getTitle().equals("General Article")) {
                hasGeneralArticle = true;
            } else if (article.getTitle().equals("SAG1 Article")) {
                hasSAG1Article = true;
            }
        }
        assertTrue(hasGeneralArticle, "General Article should be restored.");
        assertTrue(hasSAG1Article, "SAG1 Article should be restored.");
        System.out.println("Articles in SAG1 and General groups restored successfully for instructor with only SAG1 access.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testRestoreArticles_InstructorWithNoSpecialAccess() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRestoreArticles_InstructorWithNoSpecialAccess");
        
        mockDatabase.clearAllHelpArticlesAndGroups();
        Instructor instructorNoSpecialAccess = (Instructor) mockDatabase.findUserByUsername("instructorNoSpecialAccess");
        // Restore articles
        List<HelpArticle> restoredArticles = mockDatabase.restoreHelpArticles(backupFileName, true, instructorNoSpecialAccess);
        // Verify only the general group article is restored
        assertNotNull(restoredArticles, "Restored articles list should not be null.");
        assertEquals(1, restoredArticles.size(), "Only one article should be restored (General group).");
        
        HelpArticle restoredArticle = restoredArticles.get(0);
        assertEquals("General Article", restoredArticle.getTitle(), "Only General Article should be restored.");
        System.out.println("General group article restored successfully for instructor with no special access.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    
    @Test
    public void testRestoreArticles_Student() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testRestoreArticles_Student");
        // Get the student user
        Student student = (Student) mockDatabase.findUserByUsername("studentWithSAG1");
        // Attempt to restore articles
        List<HelpArticle> restoredArticles = mockDatabase.restoreHelpArticles(backupFileName, true, student);
        // Verify that the restored articles list is null, as students cannot restore articles
        assertNull(restoredArticles, "Restored articles list should be null since students cannot restore articles.");
        System.out.println("Student attempted to restore articles and was denied successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
}