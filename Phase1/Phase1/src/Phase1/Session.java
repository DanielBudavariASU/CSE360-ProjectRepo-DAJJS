package Phase1;

public class Session {
    private User currentUser;

    public Session(User user) {
        this.currentUser = user;
    }

    public void start() {
        if (currentUser.isLoggedIn()) {
            // go to home page based on the user's role
        }
    }

    public void end() {
        currentUser.logout();
        // End the session and return to the login 
    }
}
