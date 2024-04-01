package domain.common;

public final class Common {

    public enum Status {
        OK,
        FAILED,
        UNAUTHORIZED,
        NOT_FOUND,
        INTERNAL_SERVER_ERROR,
        EXISTS,
        INVALID,
        NOT_ACTIVATED,
        EXPIRED
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
