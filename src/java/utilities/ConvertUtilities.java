package utilities;

public final class ConvertUtilities {

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
