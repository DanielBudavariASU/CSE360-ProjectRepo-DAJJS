package trial;

import java.security.SecureRandom;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Password class represents a user's password, including functionality for validation,
 * resetting, generating a one-time password (OTP), and setting an expiration time for the OTP.
 */
public class Password implements Serializable {
    
    private static final long serialVersionUID = 1L;  // Serialization version identifier
    private ArrayList<Character> password;  // Password stored as a list of characters
    private String OTP;  // One-time password (OTP) for temporary use
    private String formattedExpirationTime;  // Formatted expiration time for the OTP
    
    /**
     * Constructor that creates a Password object from a given string.
     * 
     * @param password The password string to be converted into a character list.
     */
    public Password(String password) {
        this.password = convertStringToCharList(password);  // Convert the password string to a list of characters
    }

    /**
     * Default constructor for the Password class.
     */
    public Password() {
        // Default constructor can be used for OTP creation or password management
    }

    /**
     * Helper method to convert a string to an ArrayList of characters.
     * 
     * @param password The password string to convert.
     * @return An ArrayList containing the characters of the password.
     */
    private ArrayList<Character> convertStringToCharList(String password) {
        ArrayList<Character> charList = new ArrayList<>();
        for (char c : password.toCharArray()) {
            charList.add(c);  // Add each character to the list
        }
        return charList;
    }

    /**
     * Helper method to convert the character list back to a string.
     * 
     * @param charList The character list to convert back into a string.
     * @return A string representation of the password.
     */
    private String convertCharListToString(ArrayList<Character> charList) {
        StringBuilder sb = new StringBuilder();
        for (char c : charList) {
            sb.append(c);  // Append each character to the string builder
        }
        return sb.toString();  // Return the concatenated string
    }

    /**
     * Validates the input password against the stored password.
     * 
     * @param inputPassword The password string to validate.
     * @return True if the input matches the stored password, false otherwise.
     */
    public boolean validatePassword(String inputPassword) {
        ArrayList<Character> inputPasswordList = convertStringToCharList(inputPassword);  // Convert input to character list
        return this.password.equals(inputPasswordList);  // Compare the two character lists
    }

    /**
     * Resets the password with a new password string.
     * 
     * @param newPassword The new password string.
     */
    public void resetPassword(String newPassword) {
        this.password = convertStringToCharList(newPassword);  // Update the password with the new value
    }

    /**
     * Returns the stored password as a string.
     * 
     * @return The password as a string.
     */
    public String getPasswordAsString() {
        return convertCharListToString(password);  // Convert the password list to a string
    }
    
    /**
     * Generates a random one-time password (OTP) with a length between 8 and 20 characters.
     * The OTP includes uppercase letters, lowercase letters, digits, and special characters.
     * 
     * @return The generated OTP as a string.
     */
    public String setOTP() {
        SecureRandom RANDOM = new SecureRandom();  // SecureRandom for strong random number generation
        String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWER = "abcdefghijklmnopqrstuvwxyz";
        String DIGITS = "0123456789";
        String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";
        String ALL_CHARACTERS = UPPER + LOWER + DIGITS + SPECIAL_CHARACTERS;  // All character pools combined
        
        int length = (int) (Math.random() * ((20 - 8) + 1)) + 8;  // Generate random length between 8 and 20
        StringBuilder result = new StringBuilder(length);  // StringBuilder to construct the OTP
        
        result.append(UPPER.charAt(RANDOM.nextInt(UPPER.length())));  // Add at least one uppercase letter
        result.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));  // Add at least one lowercase letter
        result.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));  // Add at least one digit
        result.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())));  // Add at least one special character

        // Fill the remaining characters with random choices from all categories
        for (int i = 4; i < length; i++) {
            result.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
        }

        System.out.println("Your temporary password is: " + result.toString());
        OTP = result.toString();  // Store the generated OTP
        
        return result.toString();  // Return the OTP
    }

    /**
     * Returns the currently generated OTP.
     * 
     * @return The OTP as a string.
     */
    public String getOTP() {
        return OTP;  // Return the stored OTP
    }
    
    /**
     * Sets the expiration time for the OTP, which is 30 minutes from the current time.
     * 
     * @return The expiration time as a LocalDateTime object.
     */
    public LocalDateTime setExpiration() {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);  // Set expiration to 30 minutes from now
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedExpirationTime = expirationTime.format(formatter);  // Format expiration time as a string

        System.out.println("OTP has been generated and expires in 30 minutes on " + formattedExpirationTime);
        return expirationTime;  // Return the expiration time
    }

    /**
     * Returns the formatted expiration time of the OTP.
     * 
     * @return The formatted expiration time as a string.
     */
    public String getExpiration() {
        return formattedExpirationTime;  // Return the formatted expiration time
    }
}