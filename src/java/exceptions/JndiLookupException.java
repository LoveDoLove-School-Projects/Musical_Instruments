package exceptions;

public class JndiLookupException extends RuntimeException {

    public JndiLookupException(String message) {
        super(message);
    }

    public JndiLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
