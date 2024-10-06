package trial;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredName;
    private Password password;
    private Role role;
    private boolean otpFlag;
    private LocalDate otpExpiration;

    // Constructor for basic registration
    public User(String username, String password, String role) {
        this.username = username;
        this.password = new Password(password);
        this.role = new Role();
        this.role.addRole(role);
    }

    // Full profile setup
    public void finishProfileSetup(String email, String firstName, String middleName, String lastName, String preferredName) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredName = preferredName;
    }

    // Check if profile setup is complete
    public boolean isProfileComplete() {
        return (this.email != null && !this.email.isEmpty()) &&
               (this.firstName != null && !this.firstName.isEmpty()) &&
               (this.lastName != null && !this.lastName.isEmpty());
    }

    // OTP-related methods
    public void setOtp(String otp, LocalDate expirationDate) {
        this.password = new Password(otp);
        this.otpFlag = true;
        this.otpExpiration = expirationDate;
    }

    public boolean validateOtp(String otp) {
        if (otpFlag && otpExpiration.isAfter(LocalDate.now()) && this.password.validatePassword(otp)) {
            this.otpFlag = false;
            return true;
        }
        return false;
    }

    public boolean isOtpExpired() {
        return LocalDate.now().isAfter(this.otpExpiration);
    }

    // Role management
    public boolean isAdmin() {
        return this.role.hasRole("Admin");
    }

    public void addRole(String roleName) {
        this.role.addRole(roleName);
    }

    public void removeRole(String roleName) {
        this.role.removeRole(roleName);
    }

    // Password reset
    public void resetPassword(String newPassword) {
        this.password = null;
        this.password.resetPassword(newPassword);
        System.out.println("The new password is this: " + this.password); 
    }

    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public Role getRole() {
    	return role;
    }

    public boolean validateLogin(String password) {
        return this.password.validatePassword(password);
    }

    public String toString() {
        return "Username: " + username + ", Role: " + role;
    }
}
