package controllers.products;

import common.Constants;
import entities.ProductCategoryss;
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

public class ProductServlet extends HttpServlet {

    private static final Map<ProductCategoryss, String> PRODUCT_DETAILS;
    @PersistenceContext
    EntityManager entityManager;

    static {
        PRODUCT_DETAILS = new EnumMap<>(ProductCategoryss.class);
        PRODUCT_DETAILS.put(ProductCategoryss.PIANO, "pianoProductDetails");
        PRODUCT_DETAILS.put(ProductCategoryss.GUITAR, "guitarProductDetails");
        PRODUCT_DETAILS.put(ProductCategoryss.DRUM, "drumProductDetails");
        PRODUCT_DETAILS.put(ProductCategoryss.VIOLIN, "violinProductDetails");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (ProductCategoryss category : PRODUCT_DETAILS.keySet()) {
            List<Products> products = entityManager.createNamedQuery("Products.findByCategory").setParameter("category", category.getCategory()).getResultList();
//            if (products.isEmpty()) {
//                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "product not found", "/");
//                return;
//            }
            request.setAttribute(PRODUCT_DETAILS.get(category), products);
        }
        request.getRequestDispatcher(Constants.PRODUCT_JSP_URL).forward(request, response);
    }
}
