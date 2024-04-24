package controllers.products;

import domain.common.Constants;
import entities.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;

public class ViewProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("product_id");
        List<Products> productList = entityManager.createNamedQuery("Products.findByProductId").setParameter("productId", Integer.valueOf(productId)).getResultList();
        if (productList == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Product Not Found!", Constants.PRODUCT_URL);
            return;
        }
        Products products = productList.get(0);
        request.setAttribute("productDetails", products);
        request.getRequestDispatcher(Constants.VIEW_PRODUCT_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addProductToCart(request, response);
    }

    private void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("qty");
    }

    private void productNotFound() {

    }
}
