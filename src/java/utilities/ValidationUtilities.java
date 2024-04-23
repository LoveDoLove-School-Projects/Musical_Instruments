package utilities;

public class ValidationUtilities {

    public static boolean compareValues(String value1, String value2) {
        return value1.equals(value2);
    }

    public static boolean comparePasswords(String password, String confirmPassword) {
        return compareValues(password, confirmPassword);
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        for (char ch : phoneNumber.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }
}
