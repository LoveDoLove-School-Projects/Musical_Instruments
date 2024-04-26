package controllers.products;

import domain.common.Constants;
import domain.models.Session;
import entities.Products;
import features.SessionHandler;
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

    private static final SessionHandler sessionHandler = new SessionHandler();

    @PersistenceContext
    EntityManager entityManager;
   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request.getSession());
        if (session.isResult()) {
            String productId = request.getParameter("product_id");
            List<Products> productList = entityManager.createNamedQuery("Products.findByProductId").setParameter("productId", Integer.valueOf(productId)).getResultList();
            if (productList == null) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Product Not Found!", Constants.PRODUCT_URL);
                return;
            }
            Products products = productList.get(0);
            request.setAttribute("productDetails", products);
            request.getRequestDispatcher(Constants.VIEW_PRODUCT_JSP_URL).forward(request, response);
        } else {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.WARNING, "You must login first !", Constants.LOGIN_JSP_URL);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
