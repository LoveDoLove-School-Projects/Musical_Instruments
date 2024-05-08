package controllers.products;

import entities.Constants;
import entities.ProductCategory;
import entities.Products;
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
import utilities.RedirectUtilities;

public class ProductServlet extends HttpServlet {

    private static final Map<ProductCategory, String> PRODUCT_DETAILS;
    @PersistenceContext
    EntityManager entityManager;

    static {
        PRODUCT_DETAILS = new EnumMap<>(ProductCategory.class);
        PRODUCT_DETAILS.put(ProductCategory.PIANO, "pianoProductDetails");
        PRODUCT_DETAILS.put(ProductCategory.GUITAR, "guitarProductDetails");
        PRODUCT_DETAILS.put(ProductCategory.DRUM, "drumProductDetails");
        PRODUCT_DETAILS.put(ProductCategory.VIOLIN, "violinProductDetails");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (ProductCategory category : PRODUCT_DETAILS.keySet()) {
            List<Products> products = entityManager.createNamedQuery("Products.findByCategory").setParameter("category", category.getCategory()).getResultList();
            if (products.isEmpty()) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "product not found", "/");
                return;
            }
            request.setAttribute(PRODUCT_DETAILS.get(category), products);
        }
        request.getRequestDispatcher(Constants.PRODUCT_JSP_URL).forward(request, response);
    }
}
