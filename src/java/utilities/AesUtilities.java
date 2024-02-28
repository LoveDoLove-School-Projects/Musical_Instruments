package utilities;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesUtilities {

    private static final String AES_KEY = "cdff0db1d09606c808efdb55249060207b4cdc51eb96ba5d1692a2c2f00c2948";

    public static String aes256EcbEncrypt(String text) {
        try {
            byte[] textBytes = text.getBytes();
            SecretKey secretKey = new SecretKeySpec(ConvertUtilities.szConvertKeyToBytes(AES_KEY), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(textBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            LoggerUtilities.logSevere(ex);
            return ex.getMessage();
        }
    }

    public static String aes256EcbDecrypt(String text) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(text);
            SecretKey secretKey = new SecretKeySpec(ConvertUtilities.szConvertKeyToBytes(AES_KEY), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception ex) {
            LoggerUtilities.logSevere(ex);
            return ex.getMessage();
        }
    }
}
