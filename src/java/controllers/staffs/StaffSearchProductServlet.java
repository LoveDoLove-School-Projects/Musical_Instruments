package controllers.staffs;

import entities.Constants;
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
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class StaffSearchProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher("/pages/staffs/searchProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        if (searchQuery == null || searchQuery.isBlank() || searchQuery.trim().isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the product ID!", Constants.ADMIN_STAFF_SEARCH_PRODUCT_URL);
            return;
        }
        if (!isInteger(searchQuery)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please enter a valid product ID!", Constants.ADMIN_STAFF_SEARCH_PRODUCT_URL);
            return;
        }
        try {
            List<Products> productList = entityManager.createNamedQuery("Products.findByProductId", Products.class).setParameter("productId", Integer.valueOf(searchQuery)).getResultList();
            if (productList == null || productList.isEmpty()) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Product not existed!", Constants.ADMIN_STAFF_SEARCH_PRODUCT_URL);
                return;
            }
            Products product = productList.get(0);
            HttpSession session = request.getSession();
            request.getSession().setAttribute("productDetails", product);
            session.setAttribute("productDetails", product);
            RedirectUtilities.sendRedirect(request, response, "/pages/staffs/manageProduct");
        } catch (NumberFormatException ex) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please enter a valid product ID!", Constants.ADMIN_STAFF_SEARCH_PRODUCT_URL);
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
