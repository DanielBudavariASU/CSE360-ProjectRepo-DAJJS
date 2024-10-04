package Phase1;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

public class Password {
	protected char[] password;
	
	//constructor
	public Password() {
        
    }
	
	/* When creating a password, the password must be entered two times, and they must match.
	 * 
	 * The user will call this method and they will be prompted to enter a string and it will be validated
	 * if it is validated then they will be prompted again 
	 * 
	 * Then set password prints out whether is was successfully generated or not
	 * 
	 * */  
	public void setPassword() {
		
		Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter a password
        System.out.print("Enter your password: ");
        String input = scanner.nextLine();  // Read the input as a String

        // Convert the password string to a char array
        password = input.toCharArray();
        
        //reprompts user until they enter a valid password. 
        while(validate(password) != true)
        {
        	System.out.print("The password entered did not match the requirments, please try again: ");
            input = scanner.nextLine();  
            password = input.toCharArray();
        }
        
        
        System.out.println("Enter your password again: ");
        input = scanner.nextLine();  
        System.out.println(password);
        
        while(!Arrays.equals(password, input.toCharArray()))
        {
       	  System.out.println("Passwords do not match, please enter again: ");
          input = scanner.nextLine(); 
        }
   	
        System.out.println("Password for user was generated successfully!");
        
	}
	
	// this returns the password as a char array
	// be careful when using char arrays, if you want to display it you have to convert it to a string first so it's readable
	// you must call the setPassword function first before assigning the user password with the value returned from getPassword
	public char[] getPassword() {
	    // Check if the password was not set
	    if (password == null || password.length == 0) {
	        return new char[0];  
	    }
	    // Otherwise, return the password
	    return password;
	}
	
	public boolean validate(char[] passwordArr) {
		
		boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumericDigit = false;
        boolean foundSpecialChar = false;
        boolean foundLongEnough = false;
        boolean running = true;

        int currentCharNdx = 0;
        int passwordIndexofError = -1;
        char currentChar = passwordArr[0];  // Initialize with the first character

        // Goes through the char array and verifies each character
        while (running) {
            //displayInputState(foundUpperCase, foundLowerCase, foundNumericDigit, foundSpecialChar, foundLongEnough);

            // Check for uppercase letter
            if (currentChar >= 'A' && currentChar <= 'Z') {
                foundUpperCase = true;
            }
            // Check for lowercase letter
            else if (currentChar >= 'a' && currentChar <= 'z') {
                foundLowerCase = true;
            }
            // Check for numeric digit
            else if (currentChar >= '0' && currentChar <= '9') {
                foundNumericDigit = true;
            }
            // Check for special characters
            else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
                foundSpecialChar = true;
            }
            // Invalid character found
            else {
                passwordIndexofError = currentCharNdx;
                System.out.println( "*** Error *** An invalid character has been found at index " + passwordIndexofError);
                return false;
            }

            // Check if password length is at least 8 characters
            if (currentCharNdx >= 7) {
                foundLongEnough = true;
            }

            // Move to the next character 
            currentCharNdx++;
            if (currentCharNdx >= passwordArr.length)
                running = false;
            else
                currentChar = passwordArr[currentCharNdx];

            System.out.println();  // Line break for clarity
        }
        
        if(foundUpperCase) {System.out.println("Upper case letter found"); } else {System.out.println("Upper case letter NOT found");}
        if(foundLowerCase) {System.out.println("Lower case letter found"); } else {System.out.println("Lower case letter NOT found");}
        if(foundNumericDigit) {System.out.println("Numeric digit found"); } else {System.out.println("Numeric Digit NOT found");}
        if(foundSpecialChar) {System.out.println("Special found"); } else {System.out.println("Special char NOT found");}
        if(foundLongEnough) {System.out.println("Length requirement met"); } else {System.out.println("Length requirement NOT met");}
        
        // Final validation result
        if (foundUpperCase && foundLowerCase && foundNumericDigit && foundSpecialChar && foundLongEnough) {
            return true;
        } 
		return false;
	}
	
	/* A one-time password and an expiration date and time is set.  The next time the user tries 
	 * to log in, they must use the one-time password, and the only action possible is to set up a new password.
	 */
	public String setOTP()
	{
		 SecureRandom RANDOM = new SecureRandom();
		 // list all password requaiments 
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
	  // Fill the remaining characters with random choices from all categories
	        for (int i = 4; i < length; i++) {
	            result.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
	        }
	        //make the string into an array of charaters 
	        char[] passArray = result.toString().toCharArray();
	        //validate the Password
	        if(validate(passArray)) {
	        	return result.toString();
	        }
	        return "Invild Pasword";
	}
	
	//this is just so you can run this as a separate program in a dump project
	public static void main(String[] args) {
        Password test = new Password();
        test.setPassword();
        System.out.println("Generated password: " + new String(test.getPassword()));
    }
}
