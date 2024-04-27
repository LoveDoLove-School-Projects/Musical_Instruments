package controllers.products;

import common.Common;
import common.Constants;
import entities.Products;
import features.SessionHandler;
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

    private static final SessionHandler sessionHandle =new SessionHandler();
    private static final Map<Common.PRODUCT_CATEGORIES, String> PRODUCT_DETAILS;
    @PersistenceContext
    EntityManager entityManager;

    static {
        PRODUCT_DETAILS = new EnumMap<>(Common.PRODUCT_CATEGORIES.class);
        PRODUCT_DETAILS.put(Common.PRODUCT_CATEGORIES.PIANO, "pianoProductDetails");
        PRODUCT_DETAILS.put(Common.PRODUCT_CATEGORIES.GUITAR, "guitarProductDetails");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (Common.PRODUCT_CATEGORIES category : PRODUCT_DETAILS.keySet()) {
            List<Products> products = (List<Products>) entityManager.createNamedQuery("Products.findByCategory").setParameter("category", category.getCategory()).getResultList();
            request.setAttribute(PRODUCT_DETAILS.get(category), products);
        }
        request.getRequestDispatcher(Constants.PRODUCT_JSP_URL).forward(request, response);
    }
}
