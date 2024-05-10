package controllers.carts;

import entities.Carts;
import entities.Constants;
import entities.Session;
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
import utilities.SessionUtilities;

public class DeleteCartServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(DeleteCartServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String cartId = request.getParameter("cartId");
        try {
            userTransaction.begin();
            Carts cartToDelete = entityManager.find(Carts.class, Integer.valueOf(cartId));
            if (cartToDelete == null) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "No this cart exist", Constants.CART_URL);
                return;
            }
            entityManager.remove(cartToDelete);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "Product " + cartToDelete.getProductName() + " has been delete from cart successfully.");
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Your cart has been delete successfull !", Constants.CART_URL);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "Product " + cartId + " has been delete from cart failed.");
            LOG.severe(ex.getMessage());
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Product in cart delete failed !", Constants.CART_URL);
        }
    }
}
