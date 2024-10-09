package trial;
import java.security.SecureRandom;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Password implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Character> password;  // Password stored as a list of characters
    private String OTP;
    private String formattedExpirationTime; 
    // Constructor to create a password from a string
    public Password(String password) {
        this.password = convertStringToCharList(password);
    }
    
    public Password() {
        
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
    
	public String setOTP()
	{
		
		 SecureRandom RANDOM = new SecureRandom();
		 // list all possible characters
		 String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	     String LOWER = "abcdefghijklmnopqrstuvwxyz";
	     String DIGITS = "0123456789";
	     String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";
	     String ALL_CHARACTERS = UPPER + LOWER + DIGITS + SPECIAL_CHARACTERS;
	     int length = (int)(Math.random() * ((20- 8) + 1)) + 8; // generate a random length between 8 and 20
	     StringBuilder result = new StringBuilder(length); // string builder to build the password
	     result.append(UPPER.charAt(RANDOM.nextInt(UPPER.length())));           // Uppercase letter
	     result.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));           // Lowercase letter
	     result.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));         // Digit
	     result.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())));  // Special character
	     //Fill the remaining characters with random choices from all categories
	        for (int i = 4; i < length; i++) {
	            result.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
	        }
	      
	      System.out.println("Your temporary password is: " + result.toString()); 
	      OTP = result.toString();
	      return result.toString(); 
	}
	
	public String getOTP()
	{
		return OTP;
	}
	
	
	public LocalDateTime setExpiration()
	{
		LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedExpirationTime = expirationTime.format(formatter);

        System.out.println("OTP has been generated and expires in 30 minutes on " + formattedExpirationTime);
		return expirationTime;
	}
	
	public String getExpiration()
	{
		return formattedExpirationTime;
	}
	
	
	
	
}
