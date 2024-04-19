package utilities;

public class RandomUtilities {

    /**
     * Generates a random OTP (One-Time Password) as a string.
     *
     * @return the generated OTP as a string
     */
    public static String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000 + 100000));
    }
}
