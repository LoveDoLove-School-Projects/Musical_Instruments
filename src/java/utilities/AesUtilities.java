package utilities;

import exceptions.AesException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesUtilities {

    public static String aes256EcbEncrypt(String text, String key) {
        try {
            byte[] textBytes = text.getBytes();
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(textBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            throw new AesException("Error encrypting text", ex);
        }
    }

    public static String aes256EcbDecrypt(String text, String key) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(text);
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            throw new AesException("Error decrypting text", ex);
        }
    }

    private static Cipher getCipher(int mode, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        SecretKey secretKey = new SecretKeySpec(ConvertUtilities.szConvertKeyToBytes(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKey);
        return cipher;
    }
}
