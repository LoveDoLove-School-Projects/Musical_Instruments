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

    public enum PRODUCT_CATEGORIES {
        PIANO("PIANO"),
        GUITAR("GUITAR");
        private final String category;

        PRODUCT_CATEGORIES(String category) {
            this.category = category;
        }

        public String getCategory() {
            return category;
        }
    }
}
