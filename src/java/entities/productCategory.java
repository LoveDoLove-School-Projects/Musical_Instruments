
package entities;

public class productCategory {
     public enum PRODUCT_CATEGORIES {
        PIANO("PIANO"),
        GUITAR("GUITAR"),
        DRUM("DRUM"),
        VIOLIN("VIOLIN");
        private final String category;

        PRODUCT_CATEGORIES(String category) {
            this.category = category;
        }

        public String getCategory() {
            return category;
        }
    }
    
}
