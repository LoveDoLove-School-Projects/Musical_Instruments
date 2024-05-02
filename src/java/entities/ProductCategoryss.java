package entities;

public enum ProductCategoryss {
    PIANO("PIANO"),
    GUITAR("GUITAR"),
    DRUM("DRUM"),
    VIOLIN("VIOLIN");
    private final String category;

    ProductCategoryss(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
