package trial;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JUnit_BackupHelpArticles {
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
        // Create users: Admin, Instructor, and Student
        Admin adminWithAccess = new Admin("adminUser", "AdminPassword");
        Admin adminWithoutAccess = new Admin("adminWithoutAccess", "AdminPassword");
        Instructor instructor = new Instructor("instructorUser", "InstructorPassword");
        Student student = new Student("studentUser", "StudentPassword");
        mockDatabase.addUser(adminWithAccess);
        mockDatabase.addUser(adminWithoutAccess);
        mockDatabase.addUser(instructor);
        mockDatabase.addUser(student);
        // Create help groups
        HelpGroup generalGroup = new HelpGroup("GeneralGroup");
        SpecialAccessGroup specialGroup = new SpecialAccessGroup("SpecialGroup");
        
        // Assign users to groups
        specialGroup.addInstructor(instructor);
        specialGroup.addStudent(student);
        specialGroup.addAdmin(adminWithAccess);
        
        // Add articles
        List<String> keywords = new ArrayList<>();
        List<String> references = new ArrayList<>();
        List<String> generalGroups = new ArrayList<>();
        List<String> specialGroups = new ArrayList<>();
        
        generalGroups.add("GeneralGroup");
        HelpArticle generalArticle = new HelpArticle("General Article", "author1", "beginner", "A general help article.", keywords, "This is the body of a general article.", references, generalGroups, new ArrayList<>(), false);
        
        specialGroups.add("SpecialGroup");
        HelpArticle specialArticle = new HelpArticle("Special Article", "author2", "expert", "A special access help article.", keywords, "This is the body of a special article.", references, new ArrayList<>(), specialGroups, true);
        
        mockDatabase.addHelpGroup(generalGroup);
        mockDatabase.addSpecialAccessGroup(specialGroup);
        mockDatabase.addHelpArticle(generalArticle);
        mockDatabase.addHelpArticle(specialArticle);
        System.out.println("\nSetup completed successfully!");
        System.out.println("-----------------------------------------");
        System.out.println("-----------------------------------------");
    }
    @Test
    public void testBackupHelpArticles_InstructorWithAccess() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testBackupHelpArticles_InstructorWithAccess");
        String backupFileName = "temp_backup_instructor_with_access.ser";
        File backupFile = new File(backupFileName);
        try {
            // Backup help articles accessible by the instructor
            List<String> groups = List.of("GeneralGroup", "SpecialGroup");
            Instructor instructor = (Instructor) mockDatabase.findUserByUsername("instructorUser");
            mockDatabase.backupHelpArticles(backupFileName, groups, instructor);
            // Verify the backup file exists
            assertTrue(backupFile.exists(), "Backup file should exist.");
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(backupFileName))) {
                List<HelpArticle> backedUpArticles = (List<HelpArticle>) in.readObject();
                assertEquals(2, backedUpArticles.size(), "The number of backed-up articles should be 2.");
                boolean hasGeneralArticle = false;
                boolean hasSpecialArticle = false;
                
                for (HelpArticle article : backedUpArticles) {
                    System.out.println("Article found from backup file: " + article.getTitle());
                    if (article.getTitle().equals("General Article")) {
                        hasGeneralArticle = true;
                    } else if (article.getTitle().equals("Special Article")) {
                        hasSpecialArticle = true;
                    }
                }
                assertTrue(hasGeneralArticle, "General Article should be backed up.");
                assertTrue(hasSpecialArticle, "Special Article should be backed up.");
                System.out.println("Instructor backup test passed successfully.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            fail("Exception occurred during backup or reading backup: " + e.getMessage());
        } finally {
            if (backupFile.exists()) {
                backupFile.delete();
            }
        }
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testBackupHelpArticles_AdminWithoutAccessToSpecialGroup() {
    	System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testBackupHelpArticles_AdminWithoutAccessToSpecialGroup");
        String backupFileName = "temp_backup_admin_no_special_access.ser";
        File backupFile = new File(backupFileName);
        try {
            List<String> groups = List.of("GeneralGroup", "SpecialGroup");
            Admin adminWithoutAccess = (Admin) mockDatabase.findUserByUsername("adminWithoutAccess");
            mockDatabase.backupHelpArticles(backupFileName, groups, adminWithoutAccess);
            // Verify the backup file exists
            assertTrue(backupFile.exists(), "Backup file should exist.");
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(backupFileName))) {
                List<HelpArticle> backedUpArticles = (List<HelpArticle>) in.readObject();
                // Since adminWithoutAccess has no access to "SpecialGroup", only the general article should be backed up
                assertEquals(1, backedUpArticles.size(), "The number of backed-up articles should be 1.");
                HelpArticle backedUpArticle = backedUpArticles.get(0);
                assertEquals("General Article", backedUpArticle.getTitle(), "Only the general article should be backed up.");
                System.out.println("Admin without access to special group test passed successfully.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            fail("Exception occurred during backup or reading backup: " + e.getMessage());
        } finally {
            if (backupFile.exists()) {
                backupFile.delete();
            }
        }
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testBackupHelpArticles_Student() throws IOException {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testBackupHelpArticles_Student");
        String backupFileName = "temp_backup_student.ser";
        File backupFile = new File(backupFileName);
        try {
            List<String> groups = List.of("GeneralGroup", "SpecialGroup");
            Student student = (Student) mockDatabase.findUserByUsername("studentUser");
            // Perform the backup attempt by student
            mockDatabase.backupHelpArticles(backupFileName, groups, student);
            // Verify the backup file exists
            assertTrue(backupFile.exists(), "Backup file should exist for students (even if it is empty).");
            // Read back the articles from the backup file
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(backupFileName))) {
                List<HelpArticle> backedUpArticles = (List<HelpArticle>) in.readObject();
                // Verify the number of articles backed up is zero
                assertEquals(0, backedUpArticles.size(), "No articles should be backed up for students.");
                System.out.println("Student backup test passed successfully, backup file contains zero articles as expected.");
            } catch (EOFException e) {
                // This is expected if the file is empty
                System.out.println("EOFException caught: No articles backed up as expected for students.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                fail("Exception occurred during backup or reading backup: " + e.getMessage());
            }
        } finally {
            // Clean up: Delete the temporary backup file after test
            if (backupFile.exists()) {
                backupFile.delete();
            }
        }
        System.out.println("\n-----------------   TEST END  --------------------");
    }
}