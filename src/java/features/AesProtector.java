package features;

import entities.Enviroment;
import utilities.AesUtilities;

public final class AesProtector {

    /**
     * Encrypts the given text using AES-256 ECB encryption.
     *
     * @param text The text to be encrypted.
     * @return The encrypted text.
     */
    public static String aes256EcbEncrypt(String text) {
        return AesUtilities.aes256EcbEncrypt(text, Enviroment.AES_KEY);
    }

    /**
     * Decrypts the given text using AES-256 ECB mode.
     *
     * @param text The text to be decrypted.
     * @return The decrypted text.
     */
    public static String aes256EcbDecrypt(String text) {
        return AesUtilities.aes256EcbDecrypt(text, Enviroment.AES_KEY);
    }
}
