package utilities;

public class StringUtilities {

    /**
     * Checks if a string is null or blank.
     *
     * @param string the string to check
     * @return true if the string is null or blank, false otherwise
     */
    public static boolean isNullOrBlank(String string) {
        return string == null || string.isBlank();
    }

    /**
     * Checks if any of the given strings is null or blank.
     *
     * @param strings the strings to check
     * @return true if any of the strings is null or blank, false otherwise
     */
    public static boolean anyNullOrBlank(String... strings) {
        for (String string : strings) {
            if (isNullOrBlank(string)) {
                return true;
            }
        }
        return false;
    }
}
