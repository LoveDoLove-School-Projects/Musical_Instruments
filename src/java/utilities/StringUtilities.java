package utilities;

public class StringUtilities {

    public static boolean isNullOrBlank(String string) {
        return string == null || string.isBlank();
    }

    public static boolean anyNullOrBlank(String... strings) {
        for (String string : strings) {
            return isNullOrBlank(string);
        }
        return false;
    }
}
