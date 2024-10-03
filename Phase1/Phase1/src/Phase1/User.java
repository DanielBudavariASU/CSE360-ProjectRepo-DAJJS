package Phase1;

public class User {
	protected String username;
    protected String password;
    protected Role role;
    protected boolean isLoggedIn;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isLoggedIn = false;
    }

    public void login(String password) {
        if (this.password.equals(password)) {
            this.isLoggedIn = true;
            // go to user home page
        } else {
            // Show error message
        }
    }

    public void logout() {
        this.isLoggedIn = false;
        // go to login page
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    // Abstract methods that will be overridden by Admin if needed
    public void viewProfile() {
        // View user profile
    }
    
    public void finishProfileSetup() {
        // Set up profile (i.e. first, middle, last name)
    }
}
