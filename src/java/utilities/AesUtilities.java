package utilities;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesUtilities {

    private static final Logger LOG = Logger.getLogger(AesUtilities.class.getName());

    /**
     * Encrypts the given text using AES-256 ECB encryption with the provided
     * key.
     *
     * @param text The text to be encrypted.
     * @param key The encryption key.
     * @return The encrypted text as a Base64-encoded string.
     */
    public static String aes256EcbEncrypt(String text, String key) {
        try {
            byte[] textBytes = text.getBytes();
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(textBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    /**
     * Decrypts the given text using AES-256 ECB mode with the provided key.
     *
     * @param text The text to be decrypted.
     * @param key The key used for decryption.
     * @return The decrypted text.
     */
    public static String aes256EcbDecrypt(String text, String key) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(text);
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    /**
     * Returns a Cipher object initialized with the specified mode and key.
     *
     * @param mode the cipher mode, either Cipher.ENCRYPT_MODE or
     * Cipher.DECRYPT_MODE
     * @param key the encryption/decryption key
     * @return the initialized Cipher object
     * @throws InvalidKeyException if the given key is invalid
     * @throws NoSuchAlgorithmException if the requested cryptographic algorithm
     * is not available
     * @throws NoSuchPaddingException if the requested padding scheme is not
     * available
     */
    private static Cipher getCipher(int mode, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        SecretKey secretKey = new SecretKeySpec(ConvertUtilities.szConvertKeyToBytes(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKey);
        return cipher;
    }
}
