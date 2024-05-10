package controllers.admins;

import entities.Constants;
import entities.Products;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.SecurityLog;

public class DeleteProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(DeleteProductServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        try {
            userTransaction.begin();
            Products product = entityManager.find(Products.class, Integer.valueOf(productId));
            entityManager.remove(product);
            userTransaction.commit();
            SecurityLog.addInternalSecurityLog(request, "Product: " + product.getProductId() + " deleted successfully.");
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Product: " + product.getProductId() + " deleted successfully.", Constants.ADMIN_STAFF_SEARCH_PRODUCT_URL);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addInternalSecurityLog(request, "Failed to delete product: " + productId + ".");
            LOG.severe(ex.getMessage());
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to delete product: " + productId + ".", Constants.ADMIN_STAFF_SEARCH_PRODUCT_URL);
        }
    }
}
