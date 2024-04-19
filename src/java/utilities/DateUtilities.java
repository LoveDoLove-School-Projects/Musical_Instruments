package utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import static domain.common.Constants.DATE_PATTERN;
import static domain.common.Constants.DATE_TIME_PATTERN;

public class DateUtilities {

    /**
     * Checks if the provided date is valid.
     *
     * @param day   the day of the month
     * @param month the month of the year
     * @param year  the year
     * @return true if the date is valid, false otherwise
     */
    public static boolean isValidDate(int day,
            int month,
            int year) {
        // Create a LocalDate object with the provided year, month, and day
        LocalDate inputDate = LocalDate.of(year,
                month,
                day);
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Check if the inputDate is valid
        return !inputDate.isAfter(currentDate);
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date());
    }

    public static String getCurrentDate(boolean isNeedTime) {
        if (isNeedTime) {
            return new SimpleDateFormat(DATE_TIME_PATTERN).format(new Date());
        }
        return getCurrentDate();
    }
}
