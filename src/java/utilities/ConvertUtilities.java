package utilities;

public class ConvertUtilities {

    /**
     * Converts a string key to a byte array.
     * 
     * @param szKey the string key to convert
     * @return the byte array representation of the key
     */
    public static byte[] szConvertKeyToBytes(String szKey) {
        byte[] keyArray;
        if (szKey.length() == 16 || szKey.length() == 32 || szKey.length() == 48) {
            keyArray = szKey.getBytes();
        } else {
            keyArray = new byte[szKey.length() / 2];
            for (int i = 0; i < keyArray.length; i++) {
                keyArray[i] = (byte) Integer.parseInt(szKey.substring(i * 2, i * 2 + 2), 16);
            }
        }
        return keyArray;
    }
}
