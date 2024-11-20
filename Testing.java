package Phase1;
public class Testing {

    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nTesting Automation for Admin, Invitation, and User System");

        performTestCase(1, "Admin Creation Test (Successful)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock environment: no admin is present
                // Start with no users
                boolean isAdminBefore = db.isAdminPresent();
                
                // Simulate the admin creation process
                Admin newAdmin = new Admin("admin", "adminPass");
                db.addUser(newAdmin);
                
                return !isAdminBefore && db.isAdminPresent(); // Admin created
            }
        }, true);

        performTestCase(2, "Admin Creation Test (Failure, admin already exists)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock environment: admin already exists
                
                Admin existingAdmin = new Admin("admin", "adminPass");
                db.addUser(existingAdmin);  // Add existing admin
                
                // Try to add another admin (should fail)
                Admin anotherAdmin = new Admin("admin2", "adminPass2");
                db.addUser(anotherAdmin);
                
                return false; // Only 1 admin should exist
            }
        }, true);

        performTestCase(3, "Invitation Test (Successful, valid code)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock invitation creation
                // Clear previous invitations
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
                
                return db.findUserByUsername("newUser") != null && invite.isUsed(); // Test success if user added
            }
        }, true);

        performTestCase(4, "Invitation Test (Failure, invalid code)", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Mock invalid invitation
                // Clear previous invitations
                Admin admin = new Admin("admin", "adminPass");
                db.addUser(admin);
                
                // Simulate the failure: invalid invitation code used
                Invitation invite = new Invitation("user");
                db.addInvitation(invite);
                
                // Try to use a non-existent invitation code
                Invitation fetchedInvite = db.findInvitationByCode("WrongCode");
                return fetchedInvite == null;  // Test success if no invite found
            }
        }, true);

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
                return user.validateLogin("newUserPass"); // Test success if password reset works
            }
        }, true);

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

                return !passwordResetSuccess; // Test success if reset fails
            }
        }, true);
        //newly added below 
        performTestCase(7, "Find Special Access Group Test", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Create and add special access group
                SpecialAccessGroup group = new SpecialAccessGroup("SpecialGroup");
                db.addSpecialAccessGroup(group);

                // Find the group
                SpecialAccessGroup foundGroup = db.findSpecialAccessGroupByName("SpecialGroup");
                SpecialAccessGroup notFoundGroup = db.findSpecialAccessGroupByName("NonExistentGroup");

                return foundGroup != null && "SpecialGroup".equals(foundGroup.getGroupName()) && notFoundGroup == null;
            }
        }, true);
        
        performTestCase(8, "Has Access to Article Test", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Add users
                User admin = new Admin("adminUser", "adminPass");
                User student = new Student("studentUser", "password123");
                db.addUser(admin);
                db.addUser(student);

                // Add article with special access group
                HelpArticle article = new HelpArticle("title", "author", "level", "description",
                        new ArrayList<>(), "body", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true);
                article.addSpecialAccessGroup("SpecialGroup");
                db.addHelpArticle(article);

                // Test access
                return db.hasAccessToArticle(admin, article) && db.hasAccessToArticle(student, article);
            }
        }, true);

        performTestCase(8, "Has Access to Article Test", new TestScenario() {
            @Override
            public boolean runTest(Database db) {
                // Add users
                User admin = new Admin("adminUser", "adminPass");
                User student = new Student("studentUser", "password123");
                db.addUser(admin);
                db.addUser(student);

                // Add article with special access group
                HelpArticle article = new HelpArticle("title", "author", "level", "description",
                        new ArrayList<>(), "body", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true);
                article.addSpecialAccessGroup("SpecialGroup");
                db.addHelpArticle(article);

                // Test access
                return db.hasAccessToArticle(admin, article) && db.hasAccessToArticle(student, article);
            }
        }, true);

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    // Helper method to run each test
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

    // Interface for test scenarios
    interface TestScenario {
        boolean runTest(Database db);
    }
}