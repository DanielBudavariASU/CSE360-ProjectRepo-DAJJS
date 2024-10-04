package trial;

import java.io.Serializable;
import java.util.ArrayList;

public class Password implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Character> password;  // Password stored as a list of characters

    // Constructor to create a password from a string
    public Password(String password) {
        this.password = convertStringToCharList(password);
    }

    // Helper method to convert a string to a list of characters
    private ArrayList<Character> convertStringToCharList(String password) {
        ArrayList<Character> charList = new ArrayList<>();
        for (char c : password.toCharArray()) {
            charList.add(c);
        }
        return charList;
    }

    // Helper method to convert the character list back to a string
    private String convertCharListToString(ArrayList<Character> charList) {
        StringBuilder sb = new StringBuilder();
        for (char c : charList) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Validate password: check if the input matches the stored password
    public boolean validatePassword(String inputPassword) {
        ArrayList<Character> inputPasswordList = convertStringToCharList(inputPassword);
        return this.password.equals(inputPasswordList);
    }

    // Reset the password
    public void resetPassword(String newPassword) {
        this.password = convertStringToCharList(newPassword);
    }

    // Method to get the password as a string
    public String getPasswordAsString() {
        return convertCharListToString(password);
    }
}
