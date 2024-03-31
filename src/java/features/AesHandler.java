package features;

import domain.common.Enviroment;
import utilities.AesUtilities;

public class AesHandler {

    public static String aes256EcbEncrypt(String text) {
        return AesUtilities.aes256EcbEncrypt(text, Enviroment.AES_KEY);
    }

    public static String aes256EcbDecrypt(String text) {
        return AesUtilities.aes256EcbDecrypt(text, Enviroment.AES_KEY);
    }
}
