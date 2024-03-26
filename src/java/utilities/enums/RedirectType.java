package utilities.enums;

public enum RedirectType {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    LIGHT("light"),
    DARK("dark");

    private final String redirectType;

    private RedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public String getRedirectType() {
        return redirectType;
    }
}
