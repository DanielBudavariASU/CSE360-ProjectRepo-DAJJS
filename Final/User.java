package trial;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The User class represents a user in the system. It includes user-related attributes such as
 * username, email, and password, along with roles and OTP (one-time password) management.
 * The class supports basic registration, role assignment, password reset functionality, 
 * and OTP validation.
 */
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
    private boolean otpFlag;  // Indicates whether an OTP is required
    private LocalDateTime otpExpiration;  // Stores the expiration time of OTP
    private boolean needsReset;  // Tracks if the user needs to reset the password

    /**
     * Constructor for basic user registration.
     * Initializes the user with a username, password, and role.
     *
     * @param username The username of the user.
     * @param password The user's password.
     * @param role     The initial role assigned to the user.
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = new Password(password);  // Encapsulates password logic
        this.role = new Role();
        this.role.addRole(role);
    }

    /**
     * Completes the user profile by setting additional attributes such as
     * email, first name, middle name, last name, and preferred name.
     *
     * @param email         The user's email.
     * @param firstName     The user's first name.
     * @param middleName    The user's middle name.
     * @param lastName      The user's last name.
     * @param preferredName The user's preferred name.
     */
    public void finishProfileSetup(String email, String firstName, String middleName, String lastName, String preferredName) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredName = preferredName;
    }

    /**
     * Checks if the user's profile setup is complete by verifying that
     * essential fields like email, first name, and last name are set.
     *
     * @return true if profile setup is complete, false otherwise.
     */
    public boolean isProfileComplete() {
        return (this.email != null && !this.email.isEmpty()) &&
               (this.firstName != null && !this.firstName.isEmpty()) &&
               (this.lastName != null && !this.lastName.isEmpty());
    }

    // OTP-related methods

    /**
     * Sets the expiration time for the user's OTP.
     *
     * @param expirationDate The expiration time for the OTP.
     */
    public void setExpiration(LocalDateTime expirationDate) {
        this.otpExpiration = expirationDate;
    }

    /**
     * Validates whether the OTP is still within the valid time frame.
     *
     * @return true if the OTP is still valid, false otherwise.
     */
    public boolean validateExipration() {
        return LocalDateTime.now().isBefore(this.otpExpiration);
    }

    /**
     * Checks if the OTP has expired.
     *
     * @return true if the OTP has expired, false otherwise.
     */
    public boolean isOtpExpired() {
        return LocalDateTime.now().isAfter(this.otpExpiration);
    }

    // Role management methods

    /**
     * Checks if the user has an "Admin" role.
     *
     * @return true if the user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return this.role.hasRole("Admin");
    }

    /**
     * Adds a role to the user.
     *
     * @param roleName The name of the role to be added.
     */
    public void addRole(String roleName) {
        this.role.addRole(roleName);
    }

    /**
     * Removes a role from the user.
     *
     * @param roleName The name of the role to be removed.
     */
    public void removeRole(String roleName) {
        this.role.removeRole(roleName);
    }

    // Password reset methods

    /**
     * Resets the user's password to a new value.
     *
     * @param newPassword The new password to set.
     */
    public void resetPassword(String newPassword) {
        this.password.resetPassword(newPassword);
        System.out.println("The new password is this: " + this.password.getPasswordAsString()); 
    }

    /**
     * Validates whether the provided password matches the user's stored password.
     *
     * @param password The password to validate.
     * @return true if the password is correct, false otherwise.
     */
    public boolean validateLogin(String password) {
        return this.password.validatePassword(password);
    }

    // Getters for user attributes

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

    /**
     * Provides a string representation of the user.
     *
     * @return A string containing the user's username and roles.
     */
    @Override
    public String toString() {
        return "Username: " + username + ", Role: " + role;
    }

    // Password reset flag management

    /**
     * Sets whether the user is required to reset their password.
     *
     * @param yesOrNo A boolean indicating whether the user needs to reset their password.
     */
    public void passwordNeedReset(Boolean yesOrNo) {
        this.needsReset = yesOrNo;
    }

    /**
     * Checks if the user needs to reset their password.
     *
     * @return true if the user needs to reset their password, false otherwise.
     */
    public Boolean doesPasswordNeedReset() {
        return needsReset;
    }
}
