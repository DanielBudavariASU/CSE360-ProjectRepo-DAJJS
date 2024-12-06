package Encryption;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <p> Title: EncryptionHelper Class. </p>
 * 
 * <p> Description: Provides encryption and decryption functionality using AES with CBC mode and PKCS5 padding. 
 *                  This class uses the BouncyCastle library for cryptographic operations. </p>
 * 
 * @author Anish Pravin Kulkarni (Code provided by Prof. Lynn Robert Carter)
 */

public class EncryptionHelper {

	// Constant identifier for the BouncyCastle provider
	private static String BOUNCY_CASTLE_PROVIDER_IDENTIFIER = "BC";	
	private Cipher cipher;  // Cipher instance for encryption and decryption operations
	
	// AES key represented as a byte array
	byte[] keyBytes = new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
	private SecretKey key = new SecretKeySpec(keyBytes, "AES");    // SecretKey used for AES encryption/decryption
	
	/**
	 * <p> Constructor for EncryptionHelper class. </p>
	 * 
	 * <p> Initializes the encryption/decryption cipher with AES/CBC/PKCS5Padding mode. 
	 *      Adds the BouncyCastle security provider. </p>
	 * 
	 * @throws Exception If an error occurs while initializing the cipher.
	 */
	public EncryptionHelper() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", BOUNCY_CASTLE_PROVIDER_IDENTIFIER);		
	}
	
	/**
	 * <p> Encrypts the input plaintext using the AES algorithm in CBC mode. </p>
	 * 
	 * @param plainText The plaintext data to be encrypted, as a byte array.
	 * @param initializationVector The initialization vector (IV) used in the CBC mode.
	 * @return The encrypted data (ciphertext) as a byte array.
	 * @throws Exception if an error occurs during the encryption process.
	 */
	public byte[] encrypt(byte[] plainText, byte[] initializationVector) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initializationVector));
		return cipher.doFinal(plainText);
	}
	
	/**
	 * <p> Decrypts the input ciphertext using the AES algorithm in CBC mode. </p>
	 * 
	 * @param cipherText The encrypted data (ciphertext) to be decrypted, as a byte array.
	 * @param initializationVector The initialization vector (IV) used for the decryption.
	 * @return The decrypted data (plaintext) as a byte array.
	 * @throws Exception if an error occurs during the decryption process.
	 */
	public byte[] decrypt(byte[] cipherText, byte[] initializationVector) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initializationVector));
		return cipher.doFinal(cipherText);
	}
	
}