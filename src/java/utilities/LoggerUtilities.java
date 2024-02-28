package utilities;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtilities {

    private static final Logger logger = Logger.getLogger(LoggerUtilities.class.getName());

    public static void logInfo(String message) {
        StackTraceElement caller = getCaller();
        String currentDateTime = DateUtilities.getCurrentDate(true);
        logger.log(Level.INFO, "{0} [{1}::{2}] {3}", new Object[]{currentDateTime, caller.getClassName(), caller.getMethodName(), message});
    }

    public static void logWarning(String message) {
        StackTraceElement caller = getCaller();
        String currentDateTime = DateUtilities.getCurrentDate(true);
        logger.log(Level.WARNING, "{0} [{1}::{2}] {3}", new Object[]{currentDateTime, caller.getClassName(), caller.getMethodName(), message});
    }

    public static void logSevere(Throwable throwable) {
        StackTraceElement caller = getCaller();
        String currentDateTime = DateUtilities.getCurrentDate(true);
        logger.log(Level.SEVERE, currentDateTime + " [" + caller.getClassName() + "::" + caller.getMethodName() + "]", throwable);
    }

    private static StackTraceElement getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 4) {
            return null;
        }
        return stackTrace[3];
    }
}
