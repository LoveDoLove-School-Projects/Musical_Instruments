package controllers.products;

import entities.Constants;
import entities.Products;
import entities.Ratings;
import entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class ViewProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    public static final String VIEW_PRODUCT_JSP_URL = "/pages/products/viewProduct.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session != null) {
            int productId = Integer.parseInt(request.getParameter("product_id"));
            List<Products> productList = entityManager.createNamedQuery("Products.findByProductId").setParameter("productId", productId).getResultList();
            if (productList == null) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Product Not Found!", Constants.PRODUCT_URL);
                return;
            }

            List<Ratings> ratings = entityManager.createNamedQuery("Ratings.findByProductId").setParameter("productId", productId).getResultList();

            request.setAttribute("ratingList", ratings);
            Products products = productList.get(0);
            request.setAttribute("productDetails", products);
            request.getRequestDispatcher(VIEW_PRODUCT_JSP_URL).forward(request, response);
        } else {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.WARNING, "You must login first !", Constants.CUSTOMER_LOGIN_URL);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
