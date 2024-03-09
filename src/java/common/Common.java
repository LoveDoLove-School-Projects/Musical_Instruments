package common;

public class Common {

    public enum Status {
        OK(200, "OK"),
        UNAUTHORIZED(401, "Unauthorized"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        EXISTS(409, "Already Exists"),
        INVALID(422, "Invalid");

        private final int code;
        private final String message;

        private Status(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
