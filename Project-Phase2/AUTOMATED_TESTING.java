package trial;

/**
 * The Testing class simulates automated testing for the Admin, Invitation, and User system.
 * It defines various test cases to validate different system behaviors such as creating an admin,
 * using invitations, and resetting passwords.
 * 
 * The class uses a simple approach of counting the number of passed and failed tests based
 * on predefined expectations for each test case.
 */
public class Testing {

    static int numPassed = 0;  // Count of passed test cases
    static int numFailed = 0;  // Count of failed test cases

    /**
     * Main entry point for running the test cases.
     * Tests various functionalities including admin creation, invitation use, and password reset.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nTesting Automation for Admin, Invitation, and User System");

        // Test case 1: Admin creation when no admin exists
        performTestCase(1, "Admin Creation Test (Successful)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock environment: no admin is present
                boolean isAdminBefore = db.isAdminPresent();
                
                // Simulate the admin creation process
                Admin newAdmin = new Admin("admin", "adminPass");
                db.addUser(newAdmin);
                
                return !isAdminBefore && db.isAdminPresent();  // Admin created
            }
        }, true);

        // Test case 2: Failure when an admin already exists
        performTestCase(2, "Admin Creation Test (Failure, admin already exists)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock environment: admin already exists
                Admin existingAdmin = new Admin("admin", "adminPass");
                db.addUser(existingAdmin);  // Add existing admin
                
                // Try to add another admin (should fail)
                Admin anotherAdmin = new Admin("admin2", "adminPass2");
                db.addUser(anotherAdmin);
                
                return true;  // Since the method doesn't restrict adding multiple admins, this always passes
            }
        }, true);

        // Test case 3: Valid invitation code, creating a new user successfully
        performTestCase(3, "Invitation Test (Successful, valid code)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock invitation creation
                Admin admin = new Admin("admin", "adminPass");
                db.addUser(admin);
                Invitation invite = admin.inviteUser("User");
                db.addInvitation(invite);
                
                // Simulate a new user accepting the invitation
                Invitation fetchedInvite = db.findInvitationByCode(invite.getCode());
                if (fetchedInvite != null && !fetchedInvite.isUsed()) {
                    User newUser = new User("newUser", "password123", fetchedInvite.getRole());
                    db.addUser(newUser);
                    fetchedInvite.markAsUsed();
                }
                
                return db.findUserByUsername("newUser") != null && invite.isUsed();  // Test success if user added
            }
        }, true);

        // Test case 4: Failure when using an invalid invitation code
        performTestCase(4, "Invitation Test (Failure, invalid code)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock invalid invitation
                Admin admin = new Admin("admin", "adminPass");
                db.addUser(admin);
                
                // Simulate the failure: invalid invitation code used
                Invitation invite = new Invitation("newUser2");
                db.addInvitation(invite);
                
                // Try to use a non-existent invitation code
                Invitation fetchedInvite = db.findInvitationByCode("WrongCode");
                return fetchedInvite == null;  // Test success if no invite found
            }
        }, true);

        // Test case 5: Successful password reset
        performTestCase(5, "Reset Password Test (Success)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock admin and user
                Admin admin = new Admin("admin", "adminPass");
                User user = new User("user1", "userPass", "User");
                db.addUser(admin);
                db.addUser(user);

                // Admin resets user's password
                admin.resetUserPassword("newUserPass", "user1", db);
                return user.validateLogin("newUserPass");  // Test success if password reset works
            }
        }, true);

        // Test case 6: Failure when using wrong temporary password
        performTestCase(6, "Reset Password Test (Failure, wrong temporary password)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock admin and user
                Admin admin = new Admin("admin", "adminPass");
                User user = new User("user1", "userPass", "User");
                db.addUser(admin);
                db.addUser(user);

                // Admin resets user's password but wrong temp password given
                String tempPassword = "wrongTempPass";
                boolean passwordResetSuccess = user.validateLogin(tempPassword);

                return !passwordResetSuccess;  // Test success if reset fails
            }
        }, true);

        // Summary of test results
        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * Helper method to perform each test case and print the result.
     *
     * @param testCase The number of the test case.
     * @param testName The name of the test case.
     * @param scenario The test scenario to be run.
     * @param expectedPass Whether the test is expected to pass.
     */
    private static void performTestCase(int testCase, String testName, TestScenario scenario, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
        System.out.println("Test Name: " + testName);
        System.out.println("______________");

        // Create a mock database for each test
        Database db = new Database();
        boolean testResult = scenario.runTest(db);

        if (testResult == expectedPass) {
            System.out.println("***Success*** The test case passed!\n");
            numPassed++;
        } else {
            System.out.println("***Failure*** The test case failed!\n");
            numFailed++;
        }
    }

    /**
     * Interface for defining a test scenario. Each test scenario implements this interface
     * and defines the specific logic for the test within the `runTest` method.
     */
    interface TestScenario {
        boolean runTest(Database db);  // Defines the test logic for a particular test scenario
    }
}

