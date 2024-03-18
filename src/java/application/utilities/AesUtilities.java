package application.utilities;

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

    private static final String AES_KEY = "cdff0db1d09606c808efdb55249060207b4cdc51eb96ba5d1692a2c2f00c2948";

    public static String aes256EcbEncrypt(String text) {
        try {
            byte[] textBytes = text.getBytes();
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedBytes = cipher.doFinal(textBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            throw new RuntimeException("Error encrypting text", ex);
        }
    }

    public static String aes256EcbDecrypt(String text) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(text);
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            throw new RuntimeException("Error decrypting text", ex);
        }
    }

    private static Cipher getCipher(int mode) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        SecretKey secretKey = new SecretKeySpec(ConvertUtilities.szConvertKeyToBytes(AES_KEY), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKey);
        return cipher;
    }
}
