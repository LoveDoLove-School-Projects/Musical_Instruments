package controllers.products;

import common.Constants;
import entities.Products;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ProductServlet extends HttpServlet {

    private static final SessionChecker sessionHandle = new SessionChecker();
    private static final Map<PRODUCT_CATEGORIES, String> PRODUCT_DETAILS;
    @PersistenceContext
    EntityManager entityManager;

    static {
        PRODUCT_DETAILS = new EnumMap<>(PRODUCT_CATEGORIES.class);
        PRODUCT_DETAILS.put(PRODUCT_CATEGORIES.PIANO, "pianoProductDetails");
        PRODUCT_DETAILS.put(PRODUCT_CATEGORIES.GUITAR, "guitarProductDetails");
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (PRODUCT_CATEGORIES category : PRODUCT_DETAILS.keySet()) {
            List<Products> products = (List<Products>) entityManager.createNamedQuery("Products.findByCategory").setParameter("category", category.getCategory()).getResultList();
            request.setAttribute(PRODUCT_DETAILS.get(category), products);
        }
        request.getRequestDispatcher(Constants.PRODUCT_JSP_URL).forward(request, response);
    }
}
