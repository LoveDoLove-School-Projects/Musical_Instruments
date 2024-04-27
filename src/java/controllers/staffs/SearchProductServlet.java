package controllers.staffs;

import entities.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import utilities.RedirectUtilities;

public class SearchProductServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SearchProductServlet.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");

        if (searchQuery.isBlank() || searchQuery.trim().isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the product ID!", "/pages/staffs/searchProduct.jsp");
            return;
        }

        try {
            List<Products> productList = entityManager.createNamedQuery("Products.findByProductId", Products.class).setParameter("productId", Integer.valueOf(searchQuery)).getResultList();
            if (productList == null || productList.isEmpty()) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Product not existed!", "/pages/staffs/searchProduct.jsp");
                return;
            }
            Products product = productList.get(0);
            HttpSession session = request.getSession(false);
            session.setAttribute("productDetails", product);
            RedirectUtilities.sendRedirect(request, response, "/pages/staffs/manageProduct.jsp");
        } catch (IOException | NumberFormatException ex) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please a valid product ID!", "/pages/staffs/searchProduct.jsp");
        }
    }

}
