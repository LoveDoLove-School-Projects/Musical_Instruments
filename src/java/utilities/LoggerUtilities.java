package utilities;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtilities {

    private static final Logger logger = Logger.getLogger(LoggerUtilities.class.getName());

    public static void logInfo(String message) {
        log(Level.INFO, message, null);
    }

    public static void logWarning(String message) {
        log(Level.WARNING, message, null);
    }

    public static void logSevere(String message, Throwable throwable) {
        log(Level.SEVERE, message, throwable);
    }

    private static void log(Level level, String message, Throwable throwable) {
        StackTraceElement caller = getCaller();
        String currentDateTime = DateUtilities.getCurrentDate(true);
        logger.log(level, "{0} [{1}::{2}] {3}", new Object[]{currentDateTime, caller.getClassName(), caller.getMethodName(), message, throwable});
    }

    private static StackTraceElement getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 4) {
            return null;
        }
        return stackTrace[3];
    }
}
