package utilities;

import entities.Environment;
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

public final class AesUtilities {

    private static final Logger LOG = Logger.getLogger(AesUtilities.class.getName());

    public static String aes256EcbEncrypt(String text) {
        try {
            byte[] textBytes = text.getBytes();
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, Environment.AES_KEY);
            byte[] encryptedBytes = cipher.doFinal(textBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    public static String aes256EcbDecrypt(String text) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(text);
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, Environment.AES_KEY);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    private static Cipher getCipher(int mode, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        SecretKey secretKey = new SecretKeySpec(ConvertUtilities.szConvertKeyToBytes(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKey);
        return cipher;
    }
}
