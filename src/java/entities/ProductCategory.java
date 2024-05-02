package entities;

public enum ProductCategory {
    PIANO("PIANO"),
    GUITAR("GUITAR"),
    DRUM("DRUM"),
    VIOLIN("VIOLIN");
    private final String category;

    ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
