package application.utilities;

public class RandomUtilities {

    public static String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000 + 100000));
    }
}
