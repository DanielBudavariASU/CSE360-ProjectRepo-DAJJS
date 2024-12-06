package Encryption;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * <p> Title: EncryptionUtils Class. </p>
 * 
 * <p> Description: Utility class for handling conversions between byte arrays and character arrays,
 *                  as well as generating initialization vectors (IV) for encryption purposes. </p>
 * 
 * @author Anish Pravin Kulkarni (Code provided by Prof. Lynn Robert Carter)
 */

public class EncryptionUtils {
	// Constant defining the size of the Initialization Vector (IV) used in encryption (16 bytes)
	private static int IV_SIZE = 16;
	
	/**
	 * <p> Converts a byte array to a character array. </p>
	 * 
	 * @param bytes The byte array to be converted.
	 * @return The resulting character array after conversion.
	 */
	public static char[] toCharArray(byte[] bytes) {		
        CharBuffer charBuffer = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes));
        return Arrays.copyOf(charBuffer.array(), charBuffer.limit());
	}
	
	/**
	 * <p> Converts a character array to a byte array. </p>
	 * 
	 * @param chars The character array to be converted.
	 * @return The resulting byte array after conversion.
	 */
	public static byte[] toByteArray(char[] chars) {		
        ByteBuffer byteBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(chars));
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
	}
		
	/**
	 * <p> Generates an Initialization Vector (IV) from the input character array. </p>
	 * 
	 * <p> The IV is used in encryption to ensure that the same plaintext encrypted multiple times
	 *      results in different ciphertexts. </p>
	 * 
	 * @param text The input character array from which to generate the IV.
	 * @return A byte array representing the Initialization Vector (IV).
	 */
	public static byte[] getInitializationVector(char[] text) {
		char iv[] = new char[IV_SIZE];
		
		int textPointer = 0;
		int ivPointer = 0;
		while(ivPointer < IV_SIZE) {
			iv[ivPointer++] = text[textPointer++ % text.length];
		}
		
		return toByteArray(iv);
	}
	
	/**
	 * <p> Prints a character array to the console. </p>
	 * 
	 * @param chars The character array to be printed.
	 */
	public static void printCharArray(char[] chars) {
		for(char c : chars) {
			System.out.print(c);
		}
	}
}