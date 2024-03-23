package domain.common;

public class Common {

    public enum Status {
        OK(200, "OK"),
        FAILED(400, "Failed"),
        UNAUTHORIZED(401, "Unauthorized"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        EXISTS(409, "Already Exists"),
        INVALID(422, "Invalid"),
        NOT_ACTIVATED(423, "Not Activated");

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

    public enum Role {
        ADMIN("admin"),
        CUSTOMER("customer"),
        UNKNOWN("unknown");

        private final String role;

        private Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }
}
