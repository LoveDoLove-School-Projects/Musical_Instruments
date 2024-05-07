package entities;

public enum Role {
    STAFF("Staffs"),
    CUSTOMER("Customers");
    private final String role;

    private Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Role{");
        sb.append("role=").append(role);
        sb.append('}');
        return sb.toString();
    }
}
