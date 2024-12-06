package trial;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class JUnit_InvitationLogin {
    private Database mockDatabase;
    private Admin admin;
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
        // Create an admin user for inviting new users
        admin = new Admin("adminUser", "AdminPassword");
        mockDatabase.addUser(admin);
    }
    @Test
    public void testAdminInviteNewInstructor() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testAdminInviteNewInstructor");
        // Admin sends an invitation for an Instructor
        Invitation invitation = admin.inviteUser("Instructor");
        // Verify that the invitation is generated correctly
        assertNotNull(invitation, "Invitation should not be null.");
        assertEquals("Instructor", invitation.getRole(), "Role should be 'Instructor'.");
        assertFalse(invitation.isUsed(), "Invitation should not be marked as used initially.");
        // Add the invitation to the mock database
        mockDatabase.addInvitation(invitation);
        assertNotNull(mockDatabase.findInvitationByCode(invitation.getCode()), "Invitation should be added to the database.");
        System.out.println("Invitation for instructor generated and added to the database successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testSuccessfulAccountCreationWithInvitation() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testSuccessfulAccountCreationWithInvitation");
        // Admin sends an invitation for an Instructor
        Invitation invitation = admin.inviteUser("Instructor");
        mockDatabase.addInvitation(invitation);
        // Use the invitation code to create an account
        String invitationCode = invitation.getCode();
        String username = "newInstructor";
        String password = "password123";
        Invitation foundInvitation = mockDatabase.findInvitationByCode(invitationCode);
        assertNotNull(foundInvitation, "Invitation should be found in the database.");
        assertFalse(foundInvitation.isUsed(), "Invitation should not be used yet.");
        // Create new user with invitation
        Instructor newInstructor = new Instructor(username, password);
        mockDatabase.addUser(newInstructor);
        foundInvitation.markAsUsed();
        mockDatabase.saveInvitations();  // Save changes to the database
        // Verify account creation
        assertNotNull(mockDatabase.findUserByUsername(username), "The new user should be added to the database.");
        assertTrue(foundInvitation.isUsed(), "Invitation should be marked as used after account creation.");
        System.out.println("Account creation using valid invitation was successful.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testAccountCreationWithInvalidInvitationCode() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testAccountCreationWithInvalidInvitationCode");
        // Attempt to create an account with an invalid invitation code
        String invalidInvitationCode = "INVALID-CODE";
        Invitation foundInvitation = mockDatabase.findInvitationByCode(invalidInvitationCode);
        // Verify that no invitation is found
        assertNull(foundInvitation, "No invitation should be found for an invalid code.");
        System.out.println("Account creation failed as expected with an invalid invitation code.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testReusingUsedInvitationCode() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testReusingUsedInvitationCode");
        // Admin sends an invitation for an Instructor
        Invitation invitation = admin.inviteUser("Instructor");
        mockDatabase.addInvitation(invitation);
        // Use the invitation code once to create an account
        String invitationCode = invitation.getCode();
        String username = "instructor1";
        String password = "password123";
        Instructor newInstructor = new Instructor(username, password);
        mockDatabase.addUser(newInstructor);
        invitation.markAsUsed();
        // Attempt to reuse the same invitation code
        Invitation foundInvitation = mockDatabase.findInvitationByCode(invitationCode);
        assertNull(foundInvitation, "Invitation should not be found.");
        System.out.println("Reusing a used invitation code was prevented successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
    @Test
    public void testInvitationForDifferentRoles() {
        System.out.println("\n-----------------   TEST   --------------------");
        System.out.println("Starting: testInvitationForDifferentRoles");
        // Admin sends invitations for different roles
        Invitation instructorInvitation = admin.inviteUser("Instructor");
        Invitation adminInvitation = admin.inviteUser("Admin");
        Invitation studentInvitation = admin.inviteUser("Student");
        mockDatabase.addInvitation(instructorInvitation);
        mockDatabase.addInvitation(adminInvitation);
        mockDatabase.addInvitation(studentInvitation);
        // Verify that each invitation is stored correctly
        assertEquals("Instructor", mockDatabase.findInvitationByCode(instructorInvitation.getCode()).getRole(), "Role should be 'Instructor'.");
        assertEquals("Admin", mockDatabase.findInvitationByCode(adminInvitation.getCode()).getRole(), "Role should be 'Admin'.");
        assertEquals("Student", mockDatabase.findInvitationByCode(studentInvitation.getCode()).getRole(), "Role should be 'Student'.");
        System.out.println("Invitations for different roles created and verified successfully.");
        System.out.println("\n-----------------   TEST END  --------------------");
    }
}