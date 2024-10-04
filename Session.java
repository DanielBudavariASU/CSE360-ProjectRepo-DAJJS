package Phase1;

public class Session {
    private User currentUser;

    public Session(User user) {
        this.currentUser = user;
    }
    
    public void login(String password) {
        if (currentUser.password.equals(password)) {
        	currentUser.isLoggedIn = true;
            if(currentUser.hasRole("Admin")) {
            	// sessionStart(user)
            	// go to admin page
            } else {            	
            	// sessionStart(user)
                // go to user home page
            }
        } else {
            // Show error message
        	// password incorrect
        }
    }

    public void logout() {
        currentUser.isLoggedIn = false;
        // go to login page
    }

    public boolean isLoggedIn() {
        return currentUser.isLoggedIn;
    }
}
